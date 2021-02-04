package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.activiti.entity.ActZParams;

import java.util.Map;

/**
 * @Description: 流程参数表
 * @Author: jeecg-boot
 * @Date:   2021-01-21
 * @Version: V1.0
 */
public interface IActZParamsService extends IService<ActZParams> {

    Map<String,Object> getActParams(String tableId);
}
