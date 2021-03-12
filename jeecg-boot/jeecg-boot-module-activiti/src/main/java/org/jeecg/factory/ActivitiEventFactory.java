package org.jeecg.factory;

import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.WorkflowConstants;
import org.jeecg.listener.IActivitiEventListener;
import org.jeecg.listener.DefaultActivitiEventListenerImpl;
import org.springframework.util.StringUtils;

/**
 * @author motb
 * @date 2021/3/11 16:32
 * @description //TODO ActivitiContractFactory
 **/
public class ActivitiEventFactory {

    private ActivitiEventFactory() {
    }

    public static IActivitiEventListener create(String tableName) {
        String beanName = WorkflowConstants.ActivitiListenerInstanceMap.get(tableName);
        if (StringUtils.isEmpty(beanName)) {
            return new DefaultActivitiEventListenerImpl();
        }
        return (IActivitiEventListener) SpringContextUtils.getBean(beanName);
    }

}
