package org.jeecg.modules.contract.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Data
@TableName("contract_base")
@ApiModel(value="contract_base对象", description="采购合同基础表")
public class ContractBase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**合同名称*/
    @Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String name;
	/**合同编号*/
    @Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String code;
	/**类型编码*/
    @Excel(name = "类型编码", width = 15)
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
	/**我方*/
    @Excel(name = "我方", width = 15)
    @ApiModelProperty(value = "我方")
    private String firstMember;
	/**他方*/
    @Excel(name = "他方", width = 15)
    @ApiModelProperty(value = "他方")
    private String secondMember;
	/**用户id*/
    @Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userId;
	/**合同状态*/
    @Excel(name = "合同状态", width = 15)
    @ApiModelProperty(value = "合同状态")
    private Integer status;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
}
