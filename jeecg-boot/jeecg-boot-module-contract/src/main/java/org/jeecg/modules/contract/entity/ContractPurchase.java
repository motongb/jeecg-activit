package org.jeecg.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class ContractPurchase implements Serializable,ContractCovert {
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
     * 合同名称
     */
    @Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String name;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String code;
    /**
     * 类型编码
     */
    @Excel(name = "类型编码", width = 15)
    @ApiModelProperty(value = "类型编码")
    private String typeCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "类型名称")
    private String typeName;
    /**
     * 我方
     */
    @Excel(name = "我方", width = 15)
    @ApiModelProperty(value = "我方")
    private String firstMember;

    @TableField(exist = false)
    @ApiModelProperty(value = "我方名称")
    private String firstMemberName;
    /**
     * 乙方
     */
    @Excel(name = "乙方", width = 15)
    @ApiModelProperty(value = "乙方")
    private String secondMember;

    @TableField(exist = false)
    @ApiModelProperty(value = "乙方名称")
    private String secondMemberName;
    /**
     * 丙方
     */
    @Excel(name = "丙方", width = 15)
    @ApiModelProperty(value = "丙方")
    private String thirdMember;

    @TableField(exist = false)
    @ApiModelProperty(value = "丙方名称")
    private String thirdMemberName;
    /**
     * 用户id
     */
    @Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建人名称")
    private String userName;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 签署方数
     */
    @Excel(name = "签署方数", width = 15)
    @ApiModelProperty(value = "签署方数")
    private String memberUse;

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
    private String amount;
    /**
     * 合同金额大写
     */
    @Excel(name = "合同金额大写", width = 15)
    @ApiModelProperty(value = "合同金额大写")
    private String amountLarge;

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


    @TableField(exist = false)
    @ApiModelProperty(value = "流程定义")
    private String processDef;
}
