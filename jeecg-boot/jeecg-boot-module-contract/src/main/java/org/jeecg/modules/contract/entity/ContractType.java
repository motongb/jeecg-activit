package org.jeecg.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.base.entity.TreeEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 合同类型
 * @Author: jeecg-boot
 * @Date: 2021-02-02
 * @Version: V1.0
 */
@Data
@TableName("contract_type")
@ApiModel(value = "contract_type对象", description = "合同类型")
public class ContractType extends TreeEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 编码
     */
    @Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String code;
    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private String sort;
    /**
     * 流程定义
     */
    @Excel(name = "流程定义", width = 15)
    @ApiModelProperty(value = "流程定义")
    private String processDef;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 角色
     */
    @Excel(name = "角色", width = 15)
    @ApiModelProperty(value = "角色")
    private String roles;
}
