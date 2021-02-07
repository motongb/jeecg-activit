package org.jeecg.modules.contract.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.contract.entity.ContractGeneral;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.entity.ContractPayment;

import java.util.List;

/**
 * @author motb
 * @date 2021/2/7 8:35
 * @description //TODO ContractGeneralVo
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ContractGeneralVo extends ContractPurchaseVo {

    /**
     * 附表
     */
    @ApiModelProperty(value = "附表")
    private ContractGeneral subForm;

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
