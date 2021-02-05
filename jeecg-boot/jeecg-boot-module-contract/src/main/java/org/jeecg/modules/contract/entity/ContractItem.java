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
 * @Description: 合同明细项
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Data
@TableName("contract_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_item对象", description = "合同明细项")
public class ContractItem implements Serializable {
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
     * 产品服务名称
     */
    @Excel(name = "产品服务名称", width = 15)
    @ApiModelProperty(value = "产品服务名称")
    private String name;
    /**
     * 行项目号
     */
    @Excel(name = "行项目号", width = 15)
    @ApiModelProperty(value = "行项目号")
    private String rowNo;
    /**
     * 型号规格
     */
    @Excel(name = "型号规格", width = 15)
    @ApiModelProperty(value = "型号规格")
    private String model;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 含税单价
     */
    @Excel(name = "含税单价", width = 15)
    @ApiModelProperty(value = "含税单价")
    private String price;
    /**
     * 税率
     */
    @Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private String rate;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Integer number;
    /**
     * 小计
     */
    @Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private String total;
    /**
     * 采购方式
     */
    @Excel(name = "采购方式", width = 15)
    @ApiModelProperty(value = "采购方式")
    private String purchaseWay;
}
