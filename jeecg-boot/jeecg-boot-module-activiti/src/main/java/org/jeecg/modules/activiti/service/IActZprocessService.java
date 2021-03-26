package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.activiti.entity.ActZprocess;
import org.jeecg.modules.activiti.entity.HistoricIdentityLinkDo;

import java.util.List;

/**
 * @Description: 流程定义扩展表
 * @Author: pmc
 * @Date: 2020-03-22
 * @Version: V1.0
 */
public interface IActZprocessService extends IService<ActZprocess> {

    List<ActZprocess> queryNewestProcess(String processKey);

    boolean canComplete(String taskId, String nodeId, String procDefId,String proInstId);

    void addHistoryIdentityLink(HistoricIdentityLinkDo historicIdentityLinkDo);
}
