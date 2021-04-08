package org.jeecg.modules.system.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.system.entity.SysRuleItem;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 自定义编码表
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Data
@ApiModel(value = "sys_rulePage对象", description = "自定义编码表")
public class SysRulePage {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称", width = 15)
    @ApiModelProperty(value = "规则名称")
    private String name;
    /**
     * 规则字段
     */
    @Excel(name = "规则字段", width = 15)
    @ApiModelProperty(value = "规则字段")
    private String ruleField;
    /**
     * 规则表
     */
    @Excel(name = "规则表", width = 15)
    @ApiModelProperty(value = "规则表")
    private String ruleTable;

    @ExcelCollection(name = "自定义规则项")
    @ApiModelProperty(value = "自定义规则项")
    private List<SysRuleItem> sysRuleItemList;

    //    public SysRulePage(SysRule sysRule, List<SysRuleItem> sysRuleItemList) {
//        this.id = sysRule.getId();
//        this.createBy = sysRule.getCreateBy();
//        this.createTime = sysRule.getCreateTime();
//        this.updateBy = sysRule.getUpdateBy();
//        this.updateTime = sysRule.getUpdateTime();
//        this.sysOrgCode = sysRule.getSysOrgCode();
//        this.name = sysRule.getName();
//        this.ruleField = sysRule.getRuleField();
//        this.ruleTable = sysRule.getRuleTable();
//        this.sysRuleItemList = sysRuleItemList;
//    }
    public SysRulePage() {
    }
}
