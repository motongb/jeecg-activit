package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractPurchase;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
public interface IContractPurchaseService extends IService<ContractPurchase> {

    void saveWithProcess(ContractPurchase contractPurchase);

    /**
     * 设置合同签订对象
     *
     * @param contractPurchase
     */
    void setMember(ContractPurchase contractPurchase, boolean translateDict);

    IPage<ContractPurchase> pageVo(Page<ContractPurchase> page, ContractPurchase contractPurchase);

    void saveMoreItem(ContractPurchase contractPurchase);

    void setMoreItem(ContractPurchase contractPurchase, boolean translateDict);

    void removeMoreItem(String contractId);

    ContractPurchase getContractVoById(String contractId, boolean translateDict);

    void translateDict(ContractPurchase contractPurchase);
}
