package org.jeecg.modules.activiti.web;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.config.ICustomProcessDiagramGenerator;
import org.jeecg.config.WorkflowConstants;
import org.jeecg.modules.activiti.entity.ActZprocess;
import org.jeecg.modules.activiti.service.Impl.ActZprocessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/activiti/models")
@Slf4j
public class ActivitiModelController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActZprocessServiceImpl actZprocessService;

    @RequestMapping("/modelListData")
    @ResponseBody
    public Result modelListData(HttpServletRequest request) {
        log.info("-------------模型列表-------------");
        ModelQuery modelQuery = repositoryService.createModelQuery();
        String keyWord = request.getParameter("keyWord");//搜索关键字
        if (StrUtil.isNotBlank(keyWord)) {
            modelQuery.modelNameLike("%" + keyWord + "%");
        }
        List<Model> models = modelQuery.orderByCreateTime().desc().list();

        return Result.ok(models);
    }

    @RequestMapping("/create")
    public void newModel(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            //初始化一个空模型
            Model model = repositoryService.newModel();
            //设置一些默认信息
            int revision = 1;
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String key = request.getParameter("key");
            if (StrUtil.isBlank(name)) {
                name = "new-process";
            }
            if (StrUtil.isBlank(description)) {
                description = "description";
            }
            if (StrUtil.isBlank(key)) {
                key = "processKey";
            }


            ObjectNode modelNode = objectMapper.createObjectNode();
            modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

            model.setName(name);
            model.setKey(key);
            model.setMetaInfo(modelNode.toString());

            repositoryService.saveModel(model);
            String id = model.getId();

            //完善ModelEditorSource
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace",
                    "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
            response.sendRedirect(request.getContextPath() + "/activiti/modeler.html?modelId=" + id);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("模型创建失败！");
        }

    }

    @RequestMapping("/delete/{id}")
    public @ResponseBody
    Result deleteModel(@PathVariable("id") String id) {
        repositoryService.deleteModel(id);
        return Result.ok("删除成功。");
    }


    @RequestMapping("/deployment/{id}")
    public @ResponseBody
    Result deploy(@PathVariable("id") String id) {

        // 获取模型
        Model modelData = repositoryService.getModel(id);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

        if (bytes == null) {
            return Result.error("模型数据为空，请先成功设计流程并保存");
        }

        try {
            JsonNode modelNode = new ObjectMapper().readTree(bytes);

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (model.getProcesses().size() == 0) {
                return Result.error("模型不符要求，请至少设计一条主线流程");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            // 部署发布模型流程
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();
            String metaInfo = modelData.getMetaInfo();
            JSONObject metaInfoMap = JSON.parseObject(metaInfo);
            // 设置流程分类 保存扩展流程至数据库
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
            for (ProcessDefinition pd : list) {
                ActZprocess actZprocess = new ActZprocess();
                actZprocess.setId(pd.getId());
                actZprocess.setName(modelData.getName());
                actZprocess.setProcessKey(modelData.getKey());
                actZprocess.setDeploymentId(deployment.getId());
                actZprocess.setDescription(metaInfoMap.getString(ModelDataJsonConstants.MODEL_DESCRIPTION));
                actZprocess.setVersion(pd.getVersion());
                actZprocess.setDiagramName(pd.getDiagramResourceName());
                actZprocessService.setAllOldByProcessKey(modelData.getKey());
                actZprocess.setLatest(true);
                actZprocessService.save(actZprocess);
            }
        } catch (Exception e) {
            String err = e.toString();
            log.error(e.getMessage(), e);
            if (err.indexOf("NCName") > -1) {
                return Result.error("部署失败：流程设计中的流程名称不能为空，不能为数字以及特殊字符开头！");
            }
            if (err.indexOf("PRIMARY") > -1) {
                return Result.error("部署失败：该模型已发布，key唯一！");
            }
            return Result.error("部署失败！");
        }

        return Result.ok("部署成功");
    }

    /*获取高亮实时流程图*/
//    @RequestMapping(value = "/getHighlightImg/{id}", method = RequestMethod.GET)
    public void getHighlightImg(@PathVariable String id, HttpServletResponse response) {
        InputStream inputStream = null;
        ProcessInstance pi = null;
        String picName = "";
        // 查询历史
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
        if (hpi.getEndTime() != null) {
            // 已经结束流程获取原图
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(hpi.getProcessDefinitionId()).singleResult();
            picName = pd.getDiagramResourceName();
            inputStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
        } else {
            pi = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());

            List<String> highLightedActivities = new ArrayList<String>();
            // 高亮任务节点
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(id).list();
            for (Task task : tasks) {
                highLightedActivities.add(task.getTaskDefinitionKey());
            }

            List<String> highLightedFlows = new ArrayList<String>();
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            //"宋体"
            inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, highLightedFlows,
                    "宋体", "宋体", "宋体", null, 1.0);
            picName = pi.getName() + ".png";
        }
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(picName, "UTF-8"));
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error(e.toString());
            throw new JeecgBootException("读取流程图片失败");
        }
    }

    /*获取高亮实时流程图*/
    @RequestMapping(value = "/getHighlightImg/{pProcessInstanceId}")
    public void readResource(@PathVariable String pProcessInstanceId, HttpServletResponse response) throws Exception {
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String processDefinitionId = "";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(pProcessInstanceId).singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pProcessInstanceId).singleResult();
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        String resourceName = pd.getDiagramResourceName();

        if (resourceName.endsWith(".png") && StringUtils.hasText(pProcessInstanceId)) {
            getActivitiProccessImage(pProcessInstanceId, response);
            //ProcessDiagramGenerator.generateDiagram(pde, "png", getRuntimeService().getActiveActivityIds(processInstanceId));
        } else {
            // 通过接口读取
            InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }
    }

    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
        //logger.info("[开始]-获取流程图图像");
        try {
            //  获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(pProcessInstanceId).singleResult();

            if (historicProcessInstance == null) {
                //throw new BusinessException("获取流程实例ID[" + pProcessInstanceId + "]对应的历史流程实例失败！");
            } else {
                // 获取流程定义
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

                // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(pProcessInstanceId).orderByHistoricActivityInstanceId().asc().list();

                // 已执行的节点ID集合
                List<String> executedActivityIdList = new ArrayList<String>();
                int index = 1;
                //logger.info("获取已经执行的节点ID");
                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                    executedActivityIdList.add(activityInstance.getActivityId());

                    //logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
                    index++;
                }

                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

                // 已执行的线集合
                List<String> flowIds = new ArrayList<String>();
                // 获取流程走过的线 (getHighLightedFlows是下面的方法)
                flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);

//                // 获取流程图图像字符流
//                ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//                //配置字体
//                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,"宋体","微软雅黑","黑体",null,2.0);

                Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(pProcessInstanceId).list()
                        .stream().map(e -> e.getActivityId()).collect(Collectors.toSet());

                ICustomProcessDiagramGenerator diagramGenerator = (ICustomProcessDiagramGenerator) processEngineConfiguration.getProcessDiagramGenerator();
                InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
                        flowIds, "宋体", "宋体", "宋体", null, 1.0, new Color[]{WorkflowConstants.COLOR_NORMAL, WorkflowConstants.COLOR_CURRENT}, currIds);

                response.setContentType("image/png");
                OutputStream os = response.getOutputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                imageStream.close();
            }
            //logger.info("[完成]-获取流程图图像");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //logger.error("【异常】-获取流程图失败！" + e.getMessage());
            //throw new BusinessException("获取流程图失败！" + e.getMessage());
        }
    }

    private List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());


            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
            FlowNode sameActivityImpl1 = null;

            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
            HistoricActivityInstance activityImp2_;

            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
                activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

                if (activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                {

                } else {
                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
                    break;
                }

            }
            sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

            for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }

        }
        return highFlows;

    }

    /**
     * 导出部署流程资源
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportResource(@RequestParam String id, HttpServletResponse response) {

        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(id).singleResult();

        String resourceName = pd.getDiagramResourceName();
        InputStream inputStream = repositoryService.getResourceAsStream(pd.getDeploymentId(),
                resourceName);

        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(resourceName, "UTF-8"));
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

}
