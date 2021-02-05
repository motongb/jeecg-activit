package org.jeecg.modules.contract.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.contract.entity.ContractItem;

import java.util.List;
import java.util.Map;

/**
 * @author motb
 * @date 2021/2/4 14:44
 * @description //TODO ContractPurchaseVo
 **/
@Data
public class ContractPurchaseVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String name;
    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String code;
    /**
     * 类型编码
     */
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    /**
     * 我方
     */
    @ApiModelProperty(value = "我方")
    private String firstMember;
    /**
     * 乙方
     */
    @ApiModelProperty(value = "乙方")
    private String secondMember;
    /**
     * 丙方
     */
    @ApiModelProperty(value = "丙方")
    private String thirdMember;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 签署方数
     */
    @ApiModelProperty(value = "签署方数")
    private String memberUse;

    /**
     * 附表
     */
    @ApiModelProperty(value = "附表")
    private Map<String, Object> subForm;

    /**
     * 流程数据
     */
    @ApiModelProperty(value = "流程数据")
    private Map<String, Object> processData;

    /**
     * 流程参数
     */
    @ApiModelProperty(value = "流程参数")
    private Map<String, Object> params;

    /**
     * 合同明细项
     */
    @ApiModelProperty(value = "合同明细项")
    private List<ContractItem> contractItems;
}
