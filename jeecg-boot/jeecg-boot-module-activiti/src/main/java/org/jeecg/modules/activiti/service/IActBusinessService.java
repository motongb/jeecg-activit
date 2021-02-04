package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.activiti.entity.ActBusiness;

import java.util.Map;

/**
 * @Description: 流程业务扩展表
 * @Author: pmc
 * @Date: 2020-03-30
 * @Version: V1.0
 */
public interface IActBusinessService extends IService<ActBusiness> {

    ActBusiness saveBusiness(boolean isNew, Map<String, Object> processData, Map<String, Object> params);
}
