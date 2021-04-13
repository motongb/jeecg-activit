package org.jeecg.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
@Data
@TableName("contract_purchase")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_purchase对象", description = "采购合同基础表")
public class ContractPurchase extends BaseContract implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 是否使用模板
     */
    @ApiModelProperty(value = "是否使用模板：1-是，0-否")
    private String useModel;

    /**
     * 原模板id
     */
    @ApiModelProperty(value = "原模版id")
    private String sourceModel;

    /**
     * 合同文件
     */
    @Excel(name = "合同文件", width = 15)
    @ApiModelProperty(value = "合同文件")
    private String fileContract;

    /**
     * 模板文件
     */
    @Excel(name = "模板文件", width = 15)
    @ApiModelProperty(value = "模板文件")
    private String fileModel;

    /**
     * 项目相关合同
     */
    @Excel(name = "项目相关合同", width = 15)
    @ApiModelProperty(value = "项目相关合同")
    private String relateProject;
    /**
     * 相关项目
     */
    @Excel(name = "相关项目", width = 15)
    @ApiModelProperty(value = "相关项目")
    private String project;
    /**
     * 是否子合同
     */
    @Excel(name = "是否子合同", width = 15)
    @ApiModelProperty(value = "是否子合同")
    private String isSub;
    /**
     * 主合同id
     */
    @Excel(name = "主合同id", width = 15)
    @ApiModelProperty(value = "主合同id")
    private String parentId;
    /**
     * 预算相关合同
     */
    @Excel(name = "预算相关合同", width = 15)
    @ApiModelProperty(value = "预算相关合同")
    private String budget;
    /**
     * 成本中心
     */
    @Excel(name = "成本中心", width = 15)
    @ApiModelProperty(value = "成本中心")
    private String costCenter;
    /**
     * 密级
     */
    @Excel(name = "密级", width = 15)
    @ApiModelProperty(value = "密级")
    private String isSecret;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 采购方式
     */
    @Excel(name = "采购方式", width = 15)
    @ApiModelProperty(value = "采购方式")
    private String purchaseType;
    /**
     * 招标编号
     */
    @Excel(name = "招标编号", width = 15)
    @ApiModelProperty(value = "招标编号")
    private String biddingId;
    /**
     * 相关框架协议
     */
    @Excel(name = "相关框架协议", width = 15)
    @ApiModelProperty(value = "相关框架协议")
    private String protocol;
    /**
     * 采购订单号
     */
    @Excel(name = "采购订单号", width = 15)
    @ApiModelProperty(value = "采购订单号")
    private String purchaseId;
    /**
     * 合同金额
     */
    @Excel(name = "合同金额", width = 15)
    @ApiModelProperty(value = "合同金额")
    private String amount = "0";
    /**
     * 合同金额大写
     */
    @Excel(name = "合同金额大写", width = 15)
    @ApiModelProperty(value = "合同金额大写")
    private String amountLarge = "零元整";

    /**
     * 合同影像文件
     */
    @Excel(name = "合同影像文件", width = 15)
    @ApiModelProperty(value = "合同影像文件")
    private String filePdf;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private String fileAttach;

    /********vo 字段******/

    /**
     * 合同明细项
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "合同明细项")
    private List<ContractItem> contractItems;

    /**
     * 合同付款约定
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "合同付款约定")
    private List<ContractPayment> contractPayments;

}
