package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysRuleItem;

import java.util.List;

/**
 * @Description: 自定义规则项
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
public interface ISysRuleItemService extends IService<SysRuleItem> {

    public List<SysRuleItem> selectByMainId(String mainId);
}
