package org.jeecg.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 合同字段参数
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
@Data
@TableName("contract_field_params")
@ApiModel(value = "contract_field_params对象", description = "合同字段参数")
public class ContractFieldParams extends TreeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段名称
     */
    @Excel(name = "字段名称", width = 15)
    @ApiModelProperty(value = "字段名称")
    private String name;
    /**
     * 字段key
     */
    @Excel(name = "字段key", width = 15)
    @ApiModelProperty(value = "字段key")
    private String fieldKey;
    /**
     * 类型
     */
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String type;

    @TableField(exist = false)
    private Integer tableIndex;
}
