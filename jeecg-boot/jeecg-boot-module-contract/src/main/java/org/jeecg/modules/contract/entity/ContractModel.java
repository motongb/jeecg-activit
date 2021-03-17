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
 * @Description: 合同模板
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
@Data
@TableName("contract_model")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "contract_model对象", description = "合同模板")
public class ContractModel implements Serializable {
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
     * 模板名称
     */
    @Excel(name = "模板名称", width = 15)
    @ApiModelProperty(value = "模板名称")
    private String name;
    /**
     * 模板文件id
     */
    @Excel(name = "模板文件id", width = 15)
    @ApiModelProperty(value = "模板文件id")
    private String fileId;
    /**
     * 编码
     */
    @Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String modelKey;
    /**
     * 参数id
     */
    @Excel(name = "参数id", width = 15)
    @ApiModelProperty(value = "参数id")
    private String fieldId;

    @ApiModelProperty(value = "合同字段参数")
    private String paramsFields;
}
