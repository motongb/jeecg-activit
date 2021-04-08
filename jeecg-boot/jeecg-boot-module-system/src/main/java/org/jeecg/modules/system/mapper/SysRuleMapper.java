package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysRule;

import java.util.Map;

/**
 * @Description: 自定义编码表
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
public interface SysRuleMapper extends BaseMapper<SysRule> {

    String getLastCode(Map<String, Object> map);

    IPage<SysRule> pageVo(Page<SysRule> page, @Param("query") SysRule sysRule, @Param("sql") String sql);
}
