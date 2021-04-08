package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysRuleItem;
import org.jeecg.modules.system.mapper.SysRuleItemMapper;
import org.jeecg.modules.system.service.ISysRuleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 自定义规则项
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Service
public class SysRuleItemServiceImpl extends ServiceImpl<SysRuleItemMapper, SysRuleItem> implements ISysRuleItemService {

    @Autowired
    private SysRuleItemMapper sysRuleItemMapper;

    @Override
    public List<SysRuleItem> selectByMainId(String mainId) {
        return sysRuleItemMapper.selectByMainId(mainId);
    }
}
