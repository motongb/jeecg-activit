package org.jeecg.listener;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.WorkflowConstants;
import org.jeecg.event.ActivitiEvent;
import org.jeecg.factory.ActivitiEventFactory;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author motb
 * @date 2021/2/20 14:33
 * @description //任务驳回监听器
 **/
@Slf4j
@Component
public class ActivitiEventListener implements ApplicationListener<ActivitiEvent<ActBusiness>> {


    @Async
    @EventListener
    @Override
    public void onApplicationEvent(ActivitiEvent<ActBusiness> actBusinessActivitiEvent) {
        ActBusiness actBusiness = actBusinessActivitiEvent.getData();
        String event = (String) actBusinessActivitiEvent.getSource();
        log.info("{}流程驳回", actBusiness.getTitle());
        IActivitiEventListener activitiEventListener = ActivitiEventFactory.create(actBusiness.getTableName());
        switch (event) {
            //发起申请
            case WorkflowConstants.EVENT_APPLY:
                activitiEventListener.apply(actBusiness);
                break;
            // 撤销申请
            case WorkflowConstants.EVENT_CANCEL:
                activitiEventListener.cancel(actBusiness);
                break;
            //驳回至发起人
            case WorkflowConstants.EVENT_BACK:
                activitiEventListener.back(actBusiness);
                break;
            //审批通过
            case WorkflowConstants.EVENT_PASS:
                activitiEventListener.pass(actBusiness);
                break;
            // 审批完成
            case WorkflowConstants.EVENT_FINALIZED:
                activitiEventListener.finalized(actBusiness);
                break;
            default:
                break;
        }
    }
}
