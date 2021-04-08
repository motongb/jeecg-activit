package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.IMyBaseAPI;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysRule;
import org.jeecg.modules.system.entity.SysRuleItem;
import org.jeecg.modules.system.mapper.SysRuleItemMapper;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author motb
 * @date 2021/4/2 18:09
 * @description //TODO MyBaseApiImpl
 **/
@Slf4j
@Service
public class MyBaseApiImpl implements IMyBaseAPI {

    @Autowired
    private ISysRuleService sysRuleService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISysDepartService sysDepartService;

    // 重置流水号
    private static final String ITEM_TYPE_5 = "5";

    @Override
    public String getAutoRuleCode(String field, String tableName) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysRuleItemMapper sysRuleItemMapper = sysRuleService.getSysRuleItemMapper();
        SysRule sysRule = sysRuleService.getOne(new LambdaQueryWrapper<SysRule>()
                .eq(SysRule::getRuleField, field)
                .eq(SysRule::getRuleTable, tableName)
                .eq(SysRule::getSysOrgCode, user.getOrgCode())
                .orderByDesc(SysRule::getCreateTime));
        if (sysRule == null) {
            throw new JeecgBootException("该部门无可用编码规则");
        }
        List<SysRuleItem> sysRuleItems = sysRuleItemMapper.selectByMainId(sysRule.getId());
        List<SysRuleItem> resetFlowRuleItems = sysRuleItems.stream().filter(m -> ITEM_TYPE_5.equals(m.getItemType())).collect(Collectors.toList());
        Map<String, Object> params = new HashMap<>();
        params.put("field", sysRule.getRuleField());
        params.put("tableName", sysRule.getRuleTable());
        params.put("orgCode", user.getOrgCode());
        if (!CollectionUtils.isEmpty(resetFlowRuleItems)) {
            params.put("type", resetFlowRuleItems.get(0).getItemVal());
        }
        String lastCode = sysRuleService.getLastCode(params);
        StringBuilder code = new StringBuilder();
        if (StringUtils.isEmpty(lastCode)) {
            for (SysRuleItem sysRuleItem : sysRuleItems) {
                switch (sysRuleItem.getItemType()) {
                    case "1": // 日期
                        code.append(getDateTime(sysRuleItem.getItemVal()));
                        break;
                    case "2": // 部门编码
                        code.append(getDept(Integer.valueOf(sysRuleItem.getItemVal())));
                        break;
                    case "3": // 字符串常量
                        code.append(sysRuleItem.getItemVal());
                        break;
                    case "4": // 流水号
                        code.append(flowNumber(Integer.valueOf(sysRuleItem.getItemVal())));
                        break;
                    default:
                        break;
                }
            }
        } else {
            return YouBianCodeUtil.getNextYouBianCode(lastCode);
        }
        return code.toString();
    }

    /**
     * 日期
     *
     * @param format
     * @return
     */
    private String getDateTime(String format) {
        return DateUtils.formatDate(format);
    }

    /**
     * 部门编码
     *
     * @param level
     * @return
     */
    private String getDept(Integer level) {
        if (level == null || level < 1) {
            throw new JeecgBootException("部门级别不能小于1");
        }
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = sysDepartService.getOne(new LambdaQueryWrapper<SysDepart>().eq(SysDepart::getOrgCode, user.getOrgCode()));
        if (sysDepart == null) {
            throw new JeecgBootException("规则错误,部门不存在");
        }
        LinkedList<SysDepart> sysDepartModels = new LinkedList<>();
        sysDepartModels.addFirst(sysDepart);
        getParentOrg(sysDepart, sysDepartModels);
        String orgCode = getDeptCode(sysDepartModels, level);
        log.info("部门编码：{}", orgCode);
        return orgCode;
    }

    /**
     * 获取父级列表
     *
     * @param sysDepart
     * @param sysDepartModels
     * @return
     */
    private List<SysDepart> getParentOrg(SysDepart sysDepart, LinkedList<SysDepart> sysDepartModels) {
        if (StringUtils.isEmpty(sysDepart.getParentId())) {
            return sysDepartModels;
        }
        SysDepart parent = sysDepartService.getById(sysDepart.getParentId());
        sysDepartModels.addFirst(parent);
        return getParentOrg(parent, sysDepartModels);
    }

    /**
     * 获取部门编码
     *
     * @param sysDepartModels
     * @param level
     * @return
     */
    private String getDeptCode(List<SysDepart> sysDepartModels, Integer level) {
        if (level > sysDepartModels.size()) {
            return sysDepartModels.get(0).getOrgCode();
        }
        return sysDepartModels.get(level - 1).getOrgCode();
    }

    /**
     * 流水号
     *
     * @param number
     * @return
     */
    private String flowNumber(Integer number) {
        if (number == null || number < 1) {
            throw new JeecgBootException("流水号不能小于1");
        }
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, number).forEach(i -> sb.append("0"));
        return YouBianCodeUtil.getNextYouBianCode(sb.toString());
    }
}
