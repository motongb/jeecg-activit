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
 * @Description: 合同印章
 * @Author: jeecg-boot
 * @Date: 2021-03-31
 * @Version: V1.0
 */
@Data
@TableName("contract_seal")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_seal对象", description = "合同印章")
public class ContractSeal implements Serializable {
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
     * 印章名称
     */
    @Excel(name = "印章名称", width = 15)
    @ApiModelProperty(value = "印章名称")
    private String name;
    /**
     * 印章编号
     */
    @Excel(name = "印章编号", width = 15)
    @ApiModelProperty(value = "印章编号")
    private String sealCode;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String format;
    /**
     * 用印数
     */
    @Excel(name = "用印数", width = 15)
    @ApiModelProperty(value = "用印数")
    private Integer useNum;
    /**
     * 使用人
     */
    @Excel(name = "使用人", width = 15)
    @ApiModelProperty(value = "使用人")
    private String users;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 印章文件
     */
    @Excel(name = "印章文件", width = 15)
    @ApiModelProperty(value = "印章文件")
    private String fileUrl;
    /**
     * 所属
     */
    @Excel(name = "所属", width = 15)
    @ApiModelProperty(value = "所属")
    private String belong;
    /**
     * 所属类型
     */
    @Excel(name = "所属类型", width = 15)
    @ApiModelProperty(value = "所属类型")
    private Integer belongType;
    /**
     * 防伪码
     */
    @Excel(name = "防伪码", width = 15)
    @ApiModelProperty(value = "防伪码")
    private String fakeCode;
    /**
     * 所属名称
     */
    @Excel(name = "所属名称", width = 15)
    @ApiModelProperty(value = "所属名称")
    private String belongName;
    /**
     * 使用人
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "使用人")
    private String userNames;
}
