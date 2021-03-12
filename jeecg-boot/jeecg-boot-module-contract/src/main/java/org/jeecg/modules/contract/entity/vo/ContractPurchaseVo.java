package org.jeecg.modules.contract.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.entity.ContractPayment;
import org.jeecg.modules.contract.entity.ContractPurchase;

import java.util.List;
import java.util.Map;

/**
 * @author motb
 * @date 2021/2/4 14:44
 * @description //TODO ContractPurchaseVo
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ContractPurchaseVo extends ContractPurchase {
    /**
     * 我方信息
     */
    @ApiModelProperty(value = "我方信息")
    private ContractMember firstMemberObj = new ContractMember();
    /**
     * 乙方信息
     */
    @ApiModelProperty(value = "乙方信息")
    private ContractMember secondMemberObj = new ContractMember();
    /**
     * 丙方信息
     */
    @ApiModelProperty(value = "丙方信息")
    private ContractMember thirdMemberObj = new ContractMember();

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

    /**
     * 合同付款约定
     */
    @ApiModelProperty(value = "合同付款约定")
    private List<ContractPayment> contractPayments;

}
