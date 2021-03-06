package org.jeecg.modules.activiti.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.activiti.entity.*;
import org.jeecg.modules.activiti.mapper.ActBusinessMapper;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.activiti.service.IActZParamsService;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 流程业务扩展表
 * @Author: pmc
 * @Date: 2020-03-30
 * @Version: V1.0
 */
@Service
public class ActBusinessServiceImpl extends ServiceImpl<ActBusinessMapper, ActBusiness> implements IActBusinessService {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ActZprocessServiceImpl actZprocessService;

    @Autowired
    ISysBaseAPI sysBaseAPI;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IActZParamsService actZParamsService;

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    /**
     * 查询我的流程列表
     *
     * @param request
     * @return
     */
    public Page<ActBusiness> approveList(HttpServletRequest request, ActBusiness param) {
        // 按时间排序
        LambdaQueryWrapper<ActBusiness> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(ActBusiness::getCreateTime);
        if (StrUtil.isNotBlank(param.getTitle())) {
            queryWrapper.like(ActBusiness::getTitle, param.getTitle());
        }
        if (param.getStatus() != null) {
            queryWrapper.eq(ActBusiness::getStatus, param.getStatus());
        }
        // 流程定义key
        String procDefKey = request.getParameter("procDefKey");
        if (StrUtil.isNotBlank(procDefKey)) {
            queryWrapper.in(ActBusiness::getProcDefId, procDefKey);
        }

        if (param.getResult() != null) {
            queryWrapper.eq(ActBusiness::getResult, param.getResult());
        }
        String createTimeBegin = request.getParameter("createTime_begin");
        if (StrUtil.isNotBlank(createTimeBegin)) {
            queryWrapper.ge(ActBusiness::getCreateTime, createTimeBegin);
        }
        String createTimeEnd = request.getParameter("createTime_end");
        if (StrUtil.isNotBlank(createTimeEnd)) {
            queryWrapper.le(ActBusiness::getCreateTime, createTimeEnd);
        }

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(ActBusiness::getUserId, loginUser.getUsername());

        //流程类型
        String type = request.getParameter("type");
        if (StrUtil.isNotBlank(type)) {
            List<String> actBusinessIdsByType = this.listByTypeApp(type);
            if (actBusinessIdsByType.isEmpty()) { // 没有符合的 目的是上下面的查询条件也查不到
                queryWrapper.in(ActBusiness::getId, Lists.newArrayList(""));
            } else {
                queryWrapper.in(ActBusiness::getId, actBusinessIdsByType);
            }
        }
        int pageNum = StringUtils.hasText(request.getParameter("pageNumber")) ? Integer.parseInt(request.getParameter("pageNumber")) : 1;
        int pageSize = StringUtils.hasText(request.getParameter("pageSize")) ? Integer.parseInt(request.getParameter("pageSize")) : 9999;
        Page<ActBusiness> page = new Page<>(pageNum, pageSize);
        page = this.page(page, queryWrapper);
        List<ActBusiness> actBusinessList = page.getRecords();

        // 是否需要业务数据
        String needData = request.getParameter("needData");
        actBusinessList.forEach(e -> {
            if (StrUtil.isNotBlank(e.getProcDefId())) {
                // 获取流程定义表中 路由名称和流程名称
                ActZprocess actProcess = actZprocessService.getBaseMapper().selectVoById(e.getProcDefId());
                e.setRouteName(actProcess.getRouteName());
                e.setProcessName(actProcess.getName());
                e.setFormType(actProcess.getFormType());
                e.setFormCode(actProcess.getFormCode());
            }
            // 流程正在处理中时
            if (ActivitiConstant.STATUS_DEALING.equals(e.getStatus())) {
                // 关联当前任务 查询当前待办
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(e.getProcInstId()).list();
                if (taskList != null && taskList.size() == 1) {
                    e.setCurrTaskName(taskList.get(0).getName());
                } else if (taskList != null && taskList.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < taskList.size() - 1; i++) {
                        sb.append(taskList.get(i).getName() + "、");
                    }
                    sb.append(taskList.get(taskList.size() - 1).getName());
                    e.setCurrTaskName(sb.toString());
                }
                // 查询审批历史，如果有的话，禁止撤回操作
                List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(e.getProcInstId()).finished().list();
                if (list.isEmpty()) {
                    e.setProcInstStatus(ActivitiConstant.PROC_INST_NOT_APPROVE);
                } else {
                    e.setProcInstStatus(ActivitiConstant.PROC_INST_APPROVE);
                }
            }
            if (StrUtil.equals(needData, "true")) { // 需要业务数据
                Map<String, Object> applyForm = this.getApplyForm(e.getTableId(), e.getTableName());
                e.setDataMap(applyForm);
            }
        });
        page.setRecords(actBusinessList);
        return page;

    }

    public List<ActBusiness> findByProcDefId(String id) {
        return this.list(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getProcDefId, id));
    }

    /**
     * 保存业务表单数据到数据库表
     * <br>该方法相对通用，复杂业务单独定制，套路类似
     *
     * @param tableId 业务表中的数据id
     * @return 如果之前数据库没有 返回 true
     */
    public boolean saveApplyForm(String tableId, Map<String, Object> paramsData) {
        String tableName = (String) paramsData.get("tableName");
        Set<String> mainFields = new LinkedHashSet<>((List<String>) paramsData.get("mainFields"));
        Map<String, Object> busiData = this.baseMapper.getBusiData(tableId, tableName);
        Map<String, Object> mainData = (Map<String, Object>) paramsData.get("model");
        if (MapUtil.isEmpty(busiData)) { //没有，新增逻辑
            insertVbObjData(tableId, tableName, mainFields, mainData);
        } else { //有，修改
            updateVbObjData(tableId, tableName, mainFields, mainData);
        }
        // 子表
        List<Map<String, Object>> subFormMeta = (List<Map<String, Object>>) paramsData.get("subFormMeta");
        if (!CollectionUtils.isEmpty(subFormMeta)) {
            for (Map<String, Object> subForm : subFormMeta) {
                String subTableName = (String) subForm.get("tableName");
                Set<String> fields = new LinkedHashSet<>((List<String>) subForm.get("fields"));
                String type = (String) subForm.get("type");
                String pField = (String) subForm.get("pField");
                fields.add(pField);
                Object subData = paramsData.get(subTableName);
                if (Objects.isNull(subData) || CollectionUtils.isEmpty((Collection<?>) subData)) {
                    continue;
                }
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                // 先删后插
                this.baseMapper.deleteSubData(tableId, subTableName, pField);
                if ("object".equals(type)) {
                    insertVbObjData(IdWorker.getIdStr(), subTableName, fields, (Map<String, Object>) subData);
                }
                if ("list".equals(type)) {
                    String fieldVB = "(id,create_by,create_time,sys_org_code," + String.join(",", fields) + ")";
                    List<List<Object>> valuesList = new ArrayList<>();
                    List<Map<String, Object>> subList = (List<Map<String, Object>>) subData;
                    for (Map<String, Object> data : subList) {
                        List<Object> values = new ArrayList<>();
                        values.add(IdWorker.getIdStr());
                        values.add(sysUser.getUsername());
                        values.add(DateUtils.formatDate());
                        values.add(sysUser.getOrgCode());
                        fields.forEach(f -> values.add(pField.equals(f) ? tableId : data.get(f)));
                        valuesList.add(values);
                    }
                    this.baseMapper.insertBatch(subTableName, fieldVB, valuesList);
                }
            }
        }
        // 设置参数
        Map<String, Object> actZParams = (Map<String, Object>) paramsData.get("params");
        handleParams(actZParams, tableId);
        return MapUtil.isEmpty(busiData);
    }

    public void updateVbObjData(String tableId, String tableName, Set<String> fileds, Map<String, Object> paramsData) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("update_by", sysUser.getUsername());
        valueMap.put("update_time", DateUtils.formatDate());
        fileds.forEach(f -> valueMap.put(f, paramsData.get(f)));
        this.baseMapper.updateBusiData(tableName, tableId, valueMap);
    }

    public void insertVbObjData(String tableId, String tableName, Set<String> fields, Map<String, Object> paramsData) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        fields.add("id");
        fields.add("create_by");
        fields.add("create_time");
        fields.add("sys_org_code");
        paramsData.put("id", tableId);
        paramsData.put("create_by", sysUser.getUsername());
        paramsData.put("create_time", DateUtils.formatDate());
        paramsData.put("sys_org_code", sysUser.getOrgCode());
        String fieldVB = String.format("(%s)", String.join(",", fields));
        List<Object> values = fields.stream().map(m -> paramsData.get(m)).collect(Collectors.toList());
        this.baseMapper.insertBatch(tableName, fieldVB, new ArrayList<List<Object>>() {{
            add(values);
        }});
    }

    public Map<String, Object> getApplyForm(String tableId, String tableName) {
        Map<String, Object> busiData = this.getBusiData(tableId, tableName);
        Object createBy = busiData.get("createBy");
        if (createBy != null) {
            String depName = sysBaseAPI.getDepartNamesByUsername(createBy.toString()).get(0);
            busiData.put("createByDept", depName);
            LoginUser userByName = sysBaseAPI.getUserByName(createBy.toString());
            busiData.put("createByName", userByName.getRealname());
            busiData.put("createByAvatar", userByName.getAvatar());
        }
        // 设置额外自定义参数
        busiData.put("params", actZParamsService.getActParams(tableId));
        return busiData;
    }

    public void deleteBusiness(String tableName, String tableId) {
        this.baseMapper.deleteBusiData(tableId, tableName);
    }

    /**
     * 通过类型和任务id查找用户id
     */
    public List<String> findUserIdByTypeAndTaskId(String type, String taskId) {
        return baseMapper.findUserIdByTypeAndTaskId(type, taskId);
    }

    public void insertHI_IDENTITYLINK(String id, String type, String userId, String taskId, String procInstId) {
        this.baseMapper.insertHI_IDENTITYLINK(id, type, userId, taskId, procInstId);
    }

    public List<String> selectIRunIdentity(String taskId, String type) {
        return baseMapper.selectIRunIdentity(taskId, type);
    }

    /**
     * 修改业务表的流程字段
     */
    public void updateBusinessStatus(String tableName, String tableId, String actStatus) {
        try {
            baseMapper.updateBusinessStatus(tableName, tableId, actStatus);
        } catch (Exception e) {
            // 业务表需要有 act_status字段，没有会报错，不管他
            //e.printStackTrace();
            log.warn(e.getMessage());
        }
    }

    /**
     * 获取业务表单数据并驼峰转换
     */
    public Map<String, Object> getBusiData(String tableId, String tableName) {
        Map<String, Object> busiData = this.baseMapper.getBusiData(tableId, tableName);
        if (busiData == null) return null;
        HashMap<String, Object> map = Maps.newHashMap();
        for (String key : busiData.keySet()) {
            String camelName = oConvertUtils.camelName(key);
            map.put(camelName, busiData.get(key));
        }
        return map;
    }

    public List<String> listByTypeApp(String type) {
        return this.baseMapper.listByTypeApp(type);
    }


    /**
     * 获取登陆人的已办
     *
     * @param req
     * @param name       流程名
     * @param categoryId 流程类型
     * @param priority   优先级别
     * @return
     */
    public Page<HistoricTaskVo> getHistoricTaskVos(HttpServletRequest req, String name, String categoryId, Integer priority) {
        List<HistoricTaskVo> list = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = loginUser.getUsername();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().or().taskCandidateUser(userId).
                taskAssignee(userId).endOr().finished();

        // 多条件搜索
        query.orderByTaskCreateTime().desc();
        if (StrUtil.isNotBlank(name)) {
            query.taskNameLike("%" + name + "%");
        }
        if (StrUtil.isNotBlank(categoryId)) {
            query.taskCategory(categoryId);
        }
        if (priority != null) {
            query.taskPriority(priority);
        }
        String searchVal = req.getParameter("searchVal");
        if (StrUtil.isNotBlank(searchVal)) {
            //搜索标题、申请人
            List<LoginUser> usersByName = getBaseMapper().getUsersByName(searchVal);
            List<String> uNames = null;
            if (usersByName.size() == 0) {
                uNames = Lists.newArrayList("");
            } else {
                uNames = usersByName.stream().map(u -> u.getUsername()).collect(Collectors.toList());
            }
            List<ActBusiness> businessList = this.list(new LambdaQueryWrapper<ActBusiness>()
                    .like(ActBusiness::getTitle, searchVal) //标题查询
                    .or().in(ActBusiness::getUserId, uNames)
            );
            if (CollectionUtils.isEmpty(businessList)) {
                query.processInstanceIdIn(Lists.newArrayList(""));
            } else {
                // 定义id
                List<String> pids = businessList.stream().filter(act -> act.getProcInstId() != null).map(act -> act.getProcInstId()).collect(Collectors.toList());
                query.processInstanceIdIn(pids);
            }
        }
        String type = req.getParameter("type");
        if (StrUtil.isNotBlank(type)) {
            List<String> deploymentIdList = this.getBaseMapper().deployment_idListByType(type);
            if (CollectionUtils.isEmpty(deploymentIdList)) {
                query.deploymentIdIn(Lists.newArrayList(""));
            } else {
                query.deploymentIdIn(deploymentIdList);
            }
        }
        String createTimeEnd = req.getParameter("createTime_end");
        if (StrUtil.isNotBlank(createTimeEnd)) {
            Date end = DateUtil.parse(createTimeEnd);
            query.taskCreatedBefore(DateUtil.endOfDay(end));
        }
        // 流程定义key
        String procDefKey = req.getParameter("procDefKey");
        if (StrUtil.isNotBlank(procDefKey)) {
            query.processDefinitionId(procDefKey);
        }

        int pageNum = StringUtils.hasText(req.getParameter("pageNumber")) ? Integer.parseInt(req.getParameter("pageNumber")) : 1;
        int pageSize = StringUtils.hasText(req.getParameter("pageSize")) ? Integer.parseInt(req.getParameter("pageSize")) : 9999;
        List<HistoricTaskInstance> taskList = query.listPage(pageNum, pageSize);
        Page<HistoricTaskVo> page = new Page<>(pageNum, pageSize, query.count());
        // 是否需要业务数据
        String needData = req.getParameter("needData");
        // 转换vo
        List<ComboModel> allUser = sysBaseAPI.queryAllUserBackCombo();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboModel::getUsername, ComboModel::getTitle));
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            // 关联委托人
            if (StrUtil.isNotBlank(htv.getOwner())) {
                htv.setOwner(userMap.get(htv.getOwner()));
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(htv.getProcInstId());
            for (HistoricIdentityLink hik : identityLinks) {
                // 关联发起人
                if ("starter".equals(hik.getType()) && StrUtil.isNotBlank(hik.getUserId())) {
                    htv.setApplyer(userMap.get(hik.getUserId()));
                }
            }
            // 关联审批意见
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if (!CollectionUtils.isEmpty(comments)) {
                htv.setComment(comments.get(0).getFullMessage());
            }
            // 关联流程信息
            ActZprocess actProcess = actZprocessService.getBaseMapper().selectVoById(htv.getProcDefId());
            if (actProcess != null) {
                htv.setProcessName(actProcess.getName());
                htv.setRouteName(actProcess.getRouteName());
                htv.setFormCode(actProcess.getFormCode());
                htv.setFormType(actProcess.getFormType());
            }
            // 关联业务key
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(htv.getProcInstId()).singleResult();
            htv.setBusinessKey(hpi.getBusinessKey());
            ActBusiness actBusiness = this.getById(hpi.getBusinessKey());
            if (actBusiness != null) {
                htv.setTableId(actBusiness.getTableId());
                htv.setTableName(actBusiness.getTableName());
                htv.setTitle(actBusiness.getTitle());
                htv.setStatus(actBusiness.getStatus());
                htv.setResult(actBusiness.getResult());
                htv.setApplyTime(actBusiness.getApplyTime());
                if (StrUtil.equals(needData, "true")) { // 需要业务数据
                    Map<String, Object> applyForm = this.getApplyForm(actBusiness.getTableId(), actBusiness.getTableName());
                    htv.setDataMap(applyForm);
                }
            }
            list.add(htv);
        });
        page.setRecords(list);
        return page;
    }


    @Override
    public ActBusiness saveBusiness(boolean isNew, Map<String, Object> processData, Map<String, Object> params) {
        ActBusiness actBusiness = new ActBusiness();
        String procDefId = (String) processData.get("procDefId");
        String procDeTitle = (String) processData.get("title");
        String tableName = (String) processData.get("tableName");
        String tableId = (String) processData.get("tableId");
        String dept = (String) processData.get("dept");
        if (isNew) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            actBusiness.setId(UUIDGenerator.generate());
            actBusiness.setUserId(sysUser.getUsername());
            actBusiness.setTableId(tableId);
            actBusiness.setTableName(tableName);
            actBusiness.setProcDefId(procDefId);
            actBusiness.setTitle(procDeTitle);
            actBusiness.setDept(dept);
            save(actBusiness);
        } else {
            actBusiness.setTitle(procDeTitle);
            update(actBusiness, new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getTableId, tableId));
        }
        handleParams(params, tableId);
        return actBusiness;
    }

    @Override
    public Map<String, Object> getFormData(String formCode, String dataId) {
        OnlCgformHead cgformHead = onlCgformHeadService.getById(formCode);
        Map<String, Object> dataMap = this.baseMapper.getBusiData(dataId, cgformHead.getTableName());
        if (StringUtils.hasText(cgformHead.getSubTableStr())) {
            String[] subTables = cgformHead.getSubTableStr().split(",");
            for (String subTable : subTables) {
                OnlCgformHead subCgform = onlCgformHeadService.getOne(new LambdaQueryWrapper<OnlCgformHead>().eq(OnlCgformHead::getTableName, subTable));
                OnlCgformField foreignField = onlCgformFieldService.getOne(new LambdaQueryWrapper<OnlCgformField>()
                        .eq(OnlCgformField::getCgformHeadId, subCgform.getId())
                        .eq(OnlCgformField::getMainTable, cgformHead.getTableName())
                );
                List<Map<String, Object>> itemList = this.baseMapper.getList(foreignField.getDbFieldName(),
                        (String) dataMap.get(foreignField.getMainField()),
                        subTable);
                dataMap.put(subTable, itemList);
            }
        }
        return dataMap;
    }

    private void handleParams(Map<String, Object> params, String tableId) {
        if (Objects.nonNull(params)) {
            actZParamsService.remove(new LambdaQueryWrapper<ActZParams>().eq(ActZParams::getPid, tableId));
            params.forEach((k, v) -> {
                ActZParams zParams = new ActZParams();
                zParams.setPid(tableId);
                zParams.setParamsKey(k);
                zParams.setParamsVal((String) v);
                actZParamsService.save(zParams);
            });
        }
    }
}
