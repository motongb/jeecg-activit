package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysRule;
import org.jeecg.modules.system.entity.SysRuleItem;
import org.jeecg.modules.system.mapper.SysRuleItemMapper;
import org.jeecg.modules.system.mapper.SysRuleMapper;
import org.jeecg.modules.system.service.ISysRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 自定义编码表
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Service
public class SysRuleServiceImpl extends ServiceImpl<SysRuleMapper, SysRule> implements ISysRuleService {

    @Autowired
    private SysRuleMapper sysRuleMapper;
    @Autowired
    private SysRuleItemMapper sysRuleItemMapper;

    @Override
    @Transactional
    public void saveMain(SysRule sysRule, List<SysRuleItem> sysRuleItemList) {
        sysRuleMapper.insert(sysRule);
        if (sysRuleItemList != null && sysRuleItemList.size() > 0) {
            for (SysRuleItem entity : sysRuleItemList) {
                //外键设置
                entity.setPid(sysRule.getId());
                sysRuleItemMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(SysRule sysRule, List<SysRuleItem> sysRuleItemList) {
        sysRuleMapper.updateById(sysRule);

        //1.先删除子表数据
        sysRuleItemMapper.deleteByMainId(sysRule.getId());

        //2.子表数据重新插入
        if (sysRuleItemList != null && sysRuleItemList.size() > 0) {
            for (SysRuleItem entity : sysRuleItemList) {
                //外键设置
                entity.setPid(sysRule.getId());
                sysRuleItemMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        sysRuleItemMapper.deleteByMainId(id);
        sysRuleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            sysRuleItemMapper.deleteByMainId(id.toString());
            sysRuleMapper.deleteById(id);
        }
    }

    @Override
    public SysRuleItemMapper getSysRuleItemMapper() {
        return this.sysRuleItemMapper;
    }

    @Override
    public String getLastCode(Map<String, Object> params) {
        return this.sysRuleMapper.getLastCode(params);
    }

    @Override
    public IPage<SysRule> pageVo(Page<SysRule> page, SysRule sysRule) {
        String sql = QueryGenerator.installAuthJdbc(SysRule.class);
        return this.sysRuleMapper.pageVo(page, sysRule, sql);
    }

}
