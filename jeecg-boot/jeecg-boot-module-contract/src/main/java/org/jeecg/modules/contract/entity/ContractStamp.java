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
import java.util.Map;

/**
 * @Description: 合同用印记录表
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
@Data
@TableName("contract_stamp")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_stamp对象", description = "合同用印记录表")
public class ContractStamp implements Serializable {
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
     * 数据id
     */
    @Excel(name = "数据id", width = 15)
    @ApiModelProperty(value = "数据id")
    private String tableId;
    /**
     * 表名
     */
    @Excel(name = "表名", width = 15)
    @ApiModelProperty(value = "表名")
    private String tableName;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 类型编码
     */
    @Excel(name = "类型编码", width = 15)
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 印章类型
     */
    @Excel(name = "印章类型", width = 15)
    @ApiModelProperty(value = "印章类型")
    private String stampType;
    /**
     * 印章份数
     */
    @Excel(name = "印章份数", width = 15)
    @ApiModelProperty(value = "印章份数")
    private String stampNum;
    /**
     * 盖章文件
     */
    @Excel(name = "盖章文件", width = 15)
    @ApiModelProperty(value = "盖章文件")
    private String fileIds;
    /**
     * 合同名称
     */
    @Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    /**
     * 流程数据
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "流程数据")
    private Map<String, Object> processData;

    /**
     * 流程参数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "流程参数")
    private Map<String, Object> params;

    /**
     * 合同数据
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "合同数据")
    private Map<String, Object> record;
}
