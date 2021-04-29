package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.activiti.entity.ActZFieldGroup;

import java.util.List;

/**
 * @Description: 流程表字段组
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
public interface IActZFieldGroupService extends IService<ActZFieldGroup> {

    List<ActZFieldGroup> selectByMainId(String mainId);

    void deleteByMainId(String mainId);
}
