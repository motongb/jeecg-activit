package org.jeecg.modules.contract.listener;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.event.ActivitiBackEvent;
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
public class ActivitiBackListener implements ApplicationListener<ActivitiBackEvent<ActBusiness>> {

    @Async
    @EventListener
    @Override
    public void onApplicationEvent(ActivitiBackEvent<ActBusiness> actBusinessActivitiBackEvent) {
        ActBusiness actBusiness = actBusinessActivitiBackEvent.getData();
        log.info("{}流程驳回", actBusiness.getTitle());
    }
}
