package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 自定义编码表
 * @Author: jeecg-boot
 * @Date: 2021-04-19
 * @Version: V1.0
 */
@ApiModel(value = "sys_rule对象", description = "自定义编码表")
@Data
@TableName("sys_rule")
public class SysRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    /**
     * 表类型
     */
    @Excel(name = "表类型", width = 15, dicCode = "ol_form_biz_type")
    @Dict(dicCode = "ol_form_biz_type")
    @ApiModelProperty(value = "表类型")
    private String tableType;

    @TableField(exist = false)
    private String departName;

    @TableField(exist = false)
    private String fieldName;

    @TableField(exist = false)
    private String tableName;
}
