package org.jeecg.modules.contract.entity;

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
 * @Description: 柳钢合同中标项
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@ApiModel(value = "lg_contract_bidding_item对象", description = "柳钢合同中标项")
@Data
@TableName("lg_contract_bidding_item")
public class LgContractBiddingItem implements Serializable {
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
     * 中标id
     */
    @ApiModelProperty(value = "中标id")
    private String biddingId;
    /**
     * 采购计划编号
     */
    @Excel(name = "采购计划编号", width = 15)
    @ApiModelProperty(value = "采购计划编号")
    private String planId;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String itemName;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码")
    private String itemCode;
    /**
     * 计量单位
     */
    @Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    private String unit;
    /**
     * 物料组
     */
    @Excel(name = "物料组", width = 15)
    @ApiModelProperty(value = "物料组")
    private String groups;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Integer count;
    /**
     * 单价
     */
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private String cost;
    /**
     * 金额
     */
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private String amount;
    /**
     * 价格单位
     */
    @Excel(name = "价格单位", width = 15)
    @ApiModelProperty(value = "价格单位")
    private String costUnit;
    /**
     * 交货期
     */
    @Excel(name = "交货期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "交货期")
    private Date deadline;
    /**
     * 订货依据及参数要求
     */
    @Excel(name = "订货依据及参数要求", width = 15)
    @ApiModelProperty(value = "订货依据及参数要求")
    private String standard;
    /**
     * 品牌
     */
    @Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 申报单位
     */
    @Excel(name = "申报单位", width = 15)
    @ApiModelProperty(value = "申报单位")
    private String applyDept;
    /**
     * 申报人
     */
    @Excel(name = "申报人", width = 15)
    @ApiModelProperty(value = "申报人")
    private String applyPerson;
    /**
     * 需求工厂
     */
    @Excel(name = "需求工厂", width = 15)
    @ApiModelProperty(value = "需求工厂")
    private String factory;
}
