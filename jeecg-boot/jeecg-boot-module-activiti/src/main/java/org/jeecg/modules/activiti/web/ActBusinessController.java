package org.jeecg.modules.activiti.web;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.config.WorkflowConstants;
import org.jeecg.event.ActivitiEvent;
import org.jeecg.modules.activiti.entity.*;
import org.jeecg.modules.activiti.service.IActZParamsService;
import org.jeecg.modules.activiti.service.Impl.ActBusinessServiceImpl;
import org.jeecg.modules.activiti.service.Impl.ActZprocessServiceImpl;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author PanMeiCheng
 * @version 1.0
 * @date 2020-04-02
 */
@RestController
@RequestMapping("/actBusiness")
@Slf4j
@Transactional
@Api(tags = "流程")
public class ActBusinessController {
    @Autowired
    ActBusinessServiceImpl actBusinessService;
    @Autowired
    ActZprocessServiceImpl actZprocessService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    ISysBaseAPI sysBaseAPI;
    @Autowired
    IActZParamsService actZParamsService;

    /*添加申请草稿状态*/
    @AutoLog(value = "流程-添加申请草稿状态")
    @ApiOperation(value = "流程-添加申请草稿状态", notes = "业务表单参数数据一并传过来！")
    @PostMapping(value = "/add")
    public Result add(@RequestBody Map<String, Object> paramsData) {
        String procDefId = (String) paramsData.get("procDefId");
        String procDeTitle = (String) paramsData.get("procDeTitle");
        String tableName = (String) paramsData.get("tableName");
        String dept = (String) paramsData.get("dept");
        /*保存业务表单数据到数据库表*/
        String tableId = IdWorker.getIdStr();
        //如果前端上传了id
        String id = (String) paramsData.get("id");
        if (id != null && !id.equals("")) {
            tableId = id;
        }
        boolean isNew = actBusinessService.saveApplyForm(tableId, paramsData);
        ActBusiness actBusiness = new ActBusiness();
        if (isNew) {
            // 新增数据 保存至我的申请业务
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String username = sysUser.getUsername();
            actBusiness.setId(UUIDGenerator.generate());
            actBusiness.setUserId(username);
            actBusiness.setTableId(tableId);
            actBusiness.setProcDefId(procDefId);
            actBusiness.setDept(dept);
            String title = (String) paramsData.get(ActivitiConstant.titleKey);
            if (StrUtil.isNotBlank(title)) {
                actBusiness.setTitle(title);
            } else {
                actBusiness.setTitle(procDeTitle);
            }
            actBusiness.setTableName(tableName);
            actBusinessService.save(actBusiness);
        } else {
            actBusiness = actBusinessService.getOne(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getTableId, tableId).last("limit 1"));
        }
        return Result.ok(actBusiness);
    }

    /*获取业务表单信息*/
    @AutoLog(value = "流程-获取业务表单信息")
    @ApiOperation(value = "流程-获取业务表单信息", notes = "获取业务表单信息")
    @RequestMapping(value = "/getForm", method = RequestMethod.GET)
    public Result getForm(@ApiParam(value = "业务表数据id", required = true) String tableId,
                          @ApiParam(value = "业务表名", required = true) String tableName) {
        if (StrUtil.isBlank(tableName)) {
            return Result.error("参数缺省！");
        }
//        Map<String, Object> applyForm = actBusinessService.getApplyForm(tableId, tableName);
        Map<String, Object> applyForm = actBusinessService.getBaseMapper().getBusiData(tableId, tableName);
        return Result.OK(applyForm);
    }

    @ApiOperation("获取表单数据")
    @GetMapping("/getFormData/{code}/{id}")
    public Result<Object> getFormData(@PathVariable("code") String code, @PathVariable("id") String id) {
        return Result.OK(actBusinessService.getFormData(code, id));
    }

    /*修改业务表单信息*/
    @AutoLog(value = "流程-修改业务表单信息")
    @ApiOperation(value = "流程-修改业务表单信息", notes = "业务表单参数数据一并传过来!")
    @RequestMapping(value = "/editForm", method = RequestMethod.POST)
    public Result editForm(@RequestBody Map<String, Object> paramsData) {
        String id = (String) paramsData.get("id");
        Assert.notNull(id, "require id");
        /*保存业务表单数据到数据库表*/
        actBusinessService.saveApplyForm(id, paramsData);

        LambdaQueryWrapper<ActBusiness> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActBusiness::getTableId, id);
        queryWrapper.eq(ActBusiness::getTableName, paramsData.get("tableName"));
        ActBusiness actBusiness = actBusinessService.getOne(queryWrapper);

        Map<String, String> map = new HashMap<>();
        map.put("id", actBusiness.getId());

        return Result.OK(map);
    }

    /*通过id删除草稿状态申请*/
    @AutoLog(value = "流程-通过id删除草稿状态申请")
    @ApiOperation(value = "流程-通过id删除草稿状态申请", notes = "通过id删除草稿状态申请")
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    public Result delByIds(@ApiParam(value = "流程扩展表id，多个,号相连", required = true) String ids) {

        for (String id : ids.split(",")) {
            ActBusiness actBusiness = actBusinessService.getById(id);
            if (actBusiness.getStatus() != ActivitiConstant.STATUS_TO_APPLY) {
                return Result.error("删除失败, 仅能删除草稿状态的申请");
            }
            // 删除关联业务表
            actBusinessService.removeById(id);
            actZParamsService.remove(new LambdaQueryWrapper<ActZParams>().eq(ActZParams::getPid, actBusiness.getTableId()));
            // 发布删除事件
            ApplicationContextUtil.getContext().publishEvent(new ActivitiEvent<>(WorkflowConstants.EVENT_DELETE, actBusiness));
            actBusinessService.deleteBusiness(actBusiness.getTableName(), actBusiness.getTableId());
        }
        return Result.OK("删除成功");
    }

    /*提交申请 启动流程*/
    @AutoLog(value = "流程-提交申请 启动流程")
    @ApiOperation(value = "流程-提交申请 启动流程", notes = "提交申请 启动流程。")
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public Result apply(ActBusiness act) {
        ActBusiness actBusiness = actBusinessService.getById(act.getId());
        if (actBusiness == null) {
            return Result.error("actBusiness表中该id不存在");
        }
        String tableId = actBusiness.getTableId();
        String tableName = actBusiness.getTableName();
        act.setTableId(tableId);
        Map<String, Object> busiData = actBusinessService.getBusiData(tableId, tableName);

        if (MapUtil.isNotEmpty(busiData) && busiData.get(ActivitiConstant.titleKey) != null) {
            //如果表单里有 标题  更新一下
            actBusiness.setTitle(busiData.get(ActivitiConstant.titleKey) + "");
        }
        String processInstanceId = actZprocessService.startProcess(act);
        actBusiness.setProcInstId(processInstanceId);
        actBusiness.setStatus(ActivitiConstant.STATUS_DEALING);
        actBusiness.setResult(ActivitiConstant.RESULT_DEALING);
        actBusiness.setApplyTime(new Date());
        actBusinessService.updateById(actBusiness);
        ApplicationContextUtil.getContext().publishEvent(new ActivitiEvent<>(WorkflowConstants.EVENT_APPLY, actBusiness));
        return Result.OK("操作成功");
    }

    /*撤回申请*/
    @AutoLog(value = "流程-撤回申请")
    @ApiOperation(value = "流程-撤回申请", notes = "撤回申请")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Result<Object> cancel(@ApiParam(value = "流程扩展表id", required = true) @RequestParam String id,
                                 @ApiParam(value = "流程实例id", required = true) @RequestParam String procInstId,
                                 @ApiParam(value = "撤销理由原因说明", required = false) @RequestParam(required = false) String reason) {

        if (StrUtil.isBlank(reason)) {
            reason = "";
        }
        runtimeService.deleteProcessInstance(procInstId, "canceled-" + reason);
        ActBusiness actBusiness = actBusinessService.getById(id);
        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
        actBusinessService.updateById(actBusiness);
        //发布撤回申请事件
        ApplicationContextUtil.getContext().publishEvent(new ActivitiEvent<>(WorkflowConstants.EVENT_CANCEL, actBusiness));
        return Result.OK("操作成功");
    }

    /**/
    @AutoLog(value = "流程-流程列表")
    @ApiOperation(value = "流程-流程列表", notes = "流程列表，登录用户的流程列表")
    @RequestMapping(value = "/listData", method = RequestMethod.GET)
    public Result listData(ActBusiness param, HttpServletRequest request) {
        return Result.OK(actBusinessService.approveList(request, param));
    }

    @AutoLog(value = "流程-查询流程类型")
    @ApiOperation(value = "流程-查询流程类型", notes = "流程类型")
    @RequestMapping(value = "/actZProcess", method = RequestMethod.GET)
    public Result listData(HttpServletRequest request) {
        List<ActZprocess> list = actZprocessService.list();
        return Result.OK(list);
    }

    @AutoLog(value = "流程-查询申请列表 与 已办列表的合集")
    @ApiOperation(value = "流程-查询申请列表 与 已办列表的合集", notes = "查询申请列表 与 已办列表的合集")
    @RequestMapping(value = "/doAndApplyList", method = RequestMethod.GET)
    public Result doAndApplyList(ActBusiness param,
                                 String name,
                                 String categoryId,
                                 Integer priority,
                                 HttpServletRequest request) {
        List<ActDoAndApplyVo> list = new ArrayList<>();

        // 查询审批列表
        List<ActBusiness> actBusinesses = actBusinessService.approveList(request, param).getRecords();

        // 查询已办列表
        List<HistoricTaskVo> doneList = actBusinessService.getHistoricTaskVos(request, name, categoryId, priority).getRecords();

        // 复制
        try {
            // 我的申请列表
            for (ActBusiness actBusiness : actBusinesses) {
                ActDoAndApplyVo actDoAndApplyVo = new ActDoAndApplyVo();
                BeanUtils.copyProperties(actDoAndApplyVo, actBusiness);
                actDoAndApplyVo.setType("1");
                list.add(actDoAndApplyVo);
            }

            // 我的已办流程
            for (HistoricTaskVo historicTaskVo : doneList) {
                ActDoAndApplyVo actDoAndApplyVo = new ActDoAndApplyVo();
                actDoAndApplyVo.setType("2");
                BeanUtils.copyProperties(actDoAndApplyVo, historicTaskVo);

                // 关联当前任务 查询当前待办
                List<Task> runTask = taskService.createTaskQuery().processInstanceId(historicTaskVo.getProcInstId()).list();
                if (runTask != null && runTask.size() == 1) {
                    actDoAndApplyVo.setCurrTaskName(runTask.get(0).getName());
                } else if (runTask != null && runTask.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < runTask.size() - 1; i++) {
                        sb.append(runTask.get(i).getName() + "、");
                    }
                    sb.append(runTask.get(runTask.size() - 1).getName());
                    actDoAndApplyVo.setCurrTaskName(sb.toString());
                }
                list.add(actDoAndApplyVo);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return Result.ok(list);
    }

    @ApiOperation("修订状态")
    @GetMapping("/updateFix")
    public Result<?> updateFix(String id, Integer status) {
        ActBusiness actBusiness = actBusinessService.getById(id);
        Assert.notNull(actBusiness, "记录不存在");
        // 修改状态
        actBusiness.setNeedEdit(status);
        actBusinessService.updateById(actBusiness);
        // 已修订，通知当前节点审批人
        if (ActivitiConstant.FIX_STATUS_2.equals(status)) {
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(actBusiness.getProcInstId()).list();
            for (Task task : taskList) {
                List<String> candidates = actBusinessService.findUserIdByTypeAndTaskId(ActivitiConstant.EXECUTOR_candidate, task.getId());
                candidates.forEach(item -> {
                    MessageDTO messageDTO = new MessageDTO(actBusiness.getUserId(), item, actBusiness.getTitle(), "已修订");
                    sysBaseAPI.sendSysAnnouncement(messageDTO);
                });
            }
        }
        return Result.OK();
    }
}
