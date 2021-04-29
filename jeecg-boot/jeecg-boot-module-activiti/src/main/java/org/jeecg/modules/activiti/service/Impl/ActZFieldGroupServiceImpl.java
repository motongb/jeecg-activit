package org.jeecg.modules.activiti.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.activiti.entity.ActZFieldGroup;
import org.jeecg.modules.activiti.mapper.ActZFieldGroupMapper;
import org.jeecg.modules.activiti.service.IActZFieldGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 流程表字段组
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
@Service
public class ActZFieldGroupServiceImpl extends ServiceImpl<ActZFieldGroupMapper, ActZFieldGroup> implements IActZFieldGroupService {

    @Autowired
    private ActZFieldGroupMapper actZFieldGroupMapper;

    @Override
    public List<ActZFieldGroup> selectByMainId(String mainId) {
        return actZFieldGroupMapper.selectByMainId(mainId);
    }

    @Override
    public void deleteByMainId(String mainId) {
        actZFieldGroupMapper.deleteByMainId(mainId);
    }
}
