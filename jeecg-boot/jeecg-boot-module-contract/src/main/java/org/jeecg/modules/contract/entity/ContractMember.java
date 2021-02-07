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
 * @Description: 合同签订对象
 * @Author: jeecg-boot
 * @Date: 2021-02-07
 * @Version: V1.0
 */
@Data
@TableName("contract_member")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_member对象", description = "合同签订对象")
public class ContractMember implements Serializable {
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
     * 类型
     */
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 中文名称
     */
    @Excel(name = "中文名称", width = 15)
    @ApiModelProperty(value = "中文名称")
    private String nameCn;
    /**
     * 英文名称
     */
    @Excel(name = "英文名称", width = 15)
    @ApiModelProperty(value = "英文名称")
    private String nameEn;
    /**
     * 注册地址
     */
    @Excel(name = "注册地址", width = 15)
    @ApiModelProperty(value = "注册地址")
    private String address;
    /**
     * 联系人
     */
    @Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    /**
     * 联系人电话
     */
    @Excel(name = "联系人电话", width = 15)
    @ApiModelProperty(value = "联系人电话")
    private String contractPhone;
    /**
     * 法人
     */
    @Excel(name = "法人", width = 15)
    @ApiModelProperty(value = "法人")
    private String legal;
    /**
     * 信用代码
     */
    @Excel(name = "信用代码", width = 15)
    @ApiModelProperty(value = "信用代码")
    private String creditCode;
    /**
     * 开户行
     */
    @Excel(name = "开户行", width = 15)
    @ApiModelProperty(value = "开户行")
    private String bank;
    /**
     * 开户行号
     */
    @Excel(name = "开户行号", width = 15)
    @ApiModelProperty(value = "开户行号")
    private String bankNo;
    /**
     * 开户行名称
     */
    @Excel(name = "开户行名称", width = 15)
    @ApiModelProperty(value = "开户行名称")
    private String bankName;
    /**
     * 合同金额
     */
    @Excel(name = "合同金额", width = 15)
    @ApiModelProperty(value = "合同金额")
    private String amount;
    /**
     * 币种
     */
    @Excel(name = "币种", width = 15)
    @ApiModelProperty(value = "币种")
    private String coin;
    /**
     * 已付金额
     */
    @Excel(name = "已付金额", width = 15)
    @ApiModelProperty(value = "已付金额")
    private String payAmount;
    /**
     * 锁定金额
     */
    @Excel(name = "锁定金额", width = 15)
    @ApiModelProperty(value = "锁定金额")
    private String lockAmount;
    /**
     * 剩余金额
     */
    @Excel(name = "剩余金额", width = 15)
    @ApiModelProperty(value = "剩余金额")
    private String restAmount;
}
