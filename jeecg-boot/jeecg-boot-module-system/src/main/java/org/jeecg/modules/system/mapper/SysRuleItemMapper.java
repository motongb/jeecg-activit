package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysRuleItem;

import java.util.List;

/**
 * @Description: 自定义规则项
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
public interface SysRuleItemMapper extends BaseMapper<SysRuleItem> {

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<SysRuleItem> selectByMainId(@Param("mainId") String mainId);
}
