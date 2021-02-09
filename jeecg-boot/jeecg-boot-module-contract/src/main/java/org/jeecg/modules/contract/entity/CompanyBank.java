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
 * @Description: 企业信息/合作方信息银行信息
 * @Author: jeecg-boot
 * @Date: 2021-02-09
 * @Version: V1.0
 */
@Data
@TableName("company_bank")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "company_bank对象", description = "企业信息/合作方信息银行信息")
public class CompanyBank implements Serializable {
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
     * 企业id
     */
    @Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private String companyId;
    /**
     * 银行代码
     */
    @Excel(name = "银行代码", width = 15)
    @ApiModelProperty(value = "银行代码")
    private String bankCode;
    /**
     * 开户行
     */
    @Excel(name = "开户行", width = 15)
    @ApiModelProperty(value = "开户行")
    private String bank;
    /**
     * 账户名称
     */
    @Excel(name = "账户名称", width = 15)
    @ApiModelProperty(value = "账户名称")
    private String bankName;
    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    @ApiModelProperty(value = "账号")
    private String bankNo;
    /**
     * 类型
     */
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 结算方式
     */
    @Excel(name = "结算方式", width = 15)
    @ApiModelProperty(value = "结算方式")
    private String settleWay;
}
