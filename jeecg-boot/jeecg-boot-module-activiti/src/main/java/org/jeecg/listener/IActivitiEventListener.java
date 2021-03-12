package org.jeecg.listener;

import org.jeecg.modules.activiti.entity.ActBusiness;

/**
 * @author motb
 * @date 2021/3/11 15:18
 * @description //TODO IActivitiListenerService
 **/
public interface IActivitiEventListener {

    /**
     * 发起申请
     *
     * @param actBusiness
     */
    void apply(ActBusiness actBusiness);

    /**
     * 撤销申请
     *
     * @param actBusiness
     */
    void cancel(ActBusiness actBusiness);

    /**
     * 驳回至发起人
     *
     * @param actBusiness
     */
    void back(ActBusiness actBusiness);

    /**
     * 审批同意
     *
     * @param actBusiness
     */
    void pass(ActBusiness actBusiness);

    /**
     * 审批完成
     *
     * @param actBusiness
     */
    void finalized(ActBusiness actBusiness);
}
