package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 自定义规则项
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@ApiModel(value = "sys_rule对象", description = "自定义编码表")
@Data
@TableName("sys_rule_item")
public class SysRuleItem implements Serializable {
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
     * 规则表id
     */
    @ApiModelProperty(value = "规则表id")
    private String pid;
    /**
     * 规则项类型
     */
    @Excel(name = "规则项类型", width = 15)
    @ApiModelProperty(value = "规则项类型")
    private String itemType;
    /**
     * 项值
     */
    @Excel(name = "项值", width = 15)
    @ApiModelProperty(value = "项值")
    private String itemVal;
    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
}
