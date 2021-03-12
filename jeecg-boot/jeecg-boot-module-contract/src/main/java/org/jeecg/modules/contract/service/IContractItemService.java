package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractItem;

/**
 * @Description: 合同明细项
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
public interface IContractItemService extends IService<ContractItem> {
    void translateDict(ContractItem contractItem);
}
