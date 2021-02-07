package org.jeecg.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

/**
 * @Description: 合同付款约定
 * @Author: jeecg-boot
 * @Date: 2021-02-07
 * @Version: V1.0
 */
@Data
@TableName("contract_payment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_payment对象", description = "合同付款约定")
public class ContractPayment implements Serializable {
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
     * 合同id
     */
    @Excel(name = "合同id", width = 15)
    @ApiModelProperty(value = "合同id")
    private String contractId;
    /**
     * 款项内容
     */
    @Excel(name = "款项内容", width = 15)
    @ApiModelProperty(value = "款项内容")
    private String context;
    /**
     * 付款日期
     */
    @Excel(name = "付款日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "付款日期")
    private Date payDate;
    /**
     * 付款单位
     */
    @Excel(name = "付款单位", width = 15)
    @ApiModelProperty(value = "付款单位")
    private String payDept;
    /**
     * 收款方
     */
    @Excel(name = "收款方", width = 15)
    @ApiModelProperty(value = "收款方")
    private String payReceive;
    /**
     * 付款条件
     */
    @Excel(name = "付款条件", width = 15)
    @ApiModelProperty(value = "付款条件")
    private String payCondition;
    /**
     * 付款金额
     */
    @Excel(name = "付款金额", width = 15)
    @ApiModelProperty(value = "付款金额")
    private String payAmount;
    /**
     * 违约责任
     */
    @Excel(name = "违约责任", width = 15)
    @ApiModelProperty(value = "违约责任")
    private String duty;
}
