package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysRule;
import org.jeecg.modules.system.entity.SysRuleItem;
import org.jeecg.modules.system.mapper.SysRuleItemMapper;

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
public interface ISysRuleService extends IService<SysRule> {

    /**
     * 添加一对多
     */
    void saveMain(SysRule sysRule, List<SysRuleItem> sysRuleItemList);

    /**
     * 修改一对多
     */
    void updateMain(SysRule sysRule, List<SysRuleItem> sysRuleItemList);

    /**
     * 删除一对多
     */
    void delMain(String id);

    /**
     * 批量删除一对多
     */
    void delBatchMain(Collection<? extends Serializable> idList);

    /**
     * 规则项mapper
     *
     * @return
     */
    SysRuleItemMapper getSysRuleItemMapper();

    /**
     * 查询最后一个编号
     *
     * @param params
     * @return
     */
    String getLastCode(Map<String, Object> params);

    /**
     * 分页
     *
     * @param page
     * @param sysRule
     * @return
     */
    IPage<SysRule> pageVo(Page<SysRule> page, SysRule sysRule);
}
