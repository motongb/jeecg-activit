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
 * @Description: 企业信息/合作方信息
 * @Author: jeecg-boot
 * @Date: 2021-02-08
 * @Version: V1.0
 */
@Data
@TableName("company")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "company对象", description = "企业信息/合作方信息")
public class Company implements Serializable {
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
     * 编码
     */
    @Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String code;
    /**
     * 级别
     */
    @Excel(name = "级别", width = 15)
    @ApiModelProperty(value = "级别")
    private String level;
    /**
     * 类型
     */
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 黑名单
     */
    @Excel(name = "黑名单", width = 15)
    @ApiModelProperty(value = "黑名单")
    private String black;
    /**
     * 服务范围
     */
    @Excel(name = "服务范围", width = 15)
    @ApiModelProperty(value = "服务范围")
    private String serviceRange;
    /**
     * 行业
     */
    @Excel(name = "行业", width = 15)
    @ApiModelProperty(value = "行业")
    private String business;
    /**
     * 国家
     */
    @Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
    private String country;
    /**
     * 区域
     */
    @Excel(name = "区域", width = 15)
    @ApiModelProperty(value = "区域")
    private String region;
    /**
     * 地址
     */
    @Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 简介
     */
    @Excel(name = "简介", width = 15)
    @ApiModelProperty(value = "简介")
    private String context;
    /**
     * 法人
     */
    @Excel(name = "法人", width = 15)
    @ApiModelProperty(value = "法人")
    private String legal;
    /**
     * 经营范围
     */
    @Excel(name = "经营范围", width = 15)
    @ApiModelProperty(value = "经营范围")
    private String register;
    /**
     * 信用代码
     */
    @Excel(name = "信用代码", width = 15)
    @ApiModelProperty(value = "信用代码")
    private String creditCode;
    /**
     * 注册资本
     */
    @Excel(name = "注册资本", width = 15)
    @ApiModelProperty(value = "注册资本")
    private String capital;
    /**
     * 注册时间
     */
    @Excel(name = "注册时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "注册时间")
    private Date registerTime;
    /**
     * 存续状态
     */
    @Excel(name = "存续状态", width = 15)
    @ApiModelProperty(value = "存续状态")
    private String liveStatus;
    /**
     * 单位电话
     */
    @Excel(name = "单位电话", width = 15)
    @ApiModelProperty(value = "单位电话")
    private String link;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 传真
     */
    @Excel(name = "传真", width = 15)
    @ApiModelProperty(value = "传真")
    private String fax;
    /**
     * 省
     */
    @Excel(name = "省", width = 15)
    @ApiModelProperty(value = "省")
    private String province;
    /**
     * 市
     */
    @Excel(name = "市", width = 15)
    @ApiModelProperty(value = "市")
    private String city;
    /**
     * 区(县)
     */
    @Excel(name = "区(县)", width = 15)
    @ApiModelProperty(value = "区(县)")
    private String area;
    /**
     * 联系人
     */
    @Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
}
