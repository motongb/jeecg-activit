package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.entity.vo.ContractPurchaseVo;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
public interface IContractPurchaseService extends IService<ContractPurchase> {

    void saveWithProcess(ContractPurchaseVo contractPurchaseVo);

    /**
     * 设置合同签订对象
     *
     * @param contractPurchaseVo
     */
    void setMember(ContractPurchaseVo contractPurchaseVo, boolean translateDict);

    IPage<ContractPurchase> pageVo(Page<ContractPurchase> page, ContractPurchase contractPurchase);

    void saveMoreItem(ContractPurchaseVo contractPurchaseVo);

    void setMoreItem(ContractPurchaseVo contractPurchaseVo, boolean translateDict);

    void removeMoreItem(String contractId);

    ContractPurchaseVo getContractVoById(String contractId, boolean translateDict);

    void translateDict(ContractPurchase contractPurchase);
}
