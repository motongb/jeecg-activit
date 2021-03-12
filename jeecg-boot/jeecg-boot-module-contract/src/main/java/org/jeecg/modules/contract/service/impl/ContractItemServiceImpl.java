package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.mapper.ContractItemMapper;
import org.jeecg.modules.contract.service.IContractItemService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 合同明细项
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Service
public class ContractItemServiceImpl extends ServiceImpl<ContractItemMapper, ContractItem> implements IContractItemService {
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void translateDict(ContractItem contractItem) {
        contractItem.setUnit(sysBaseAPI.translateDict(ContractConst.DICT_ITEM_UNIT, contractItem.getUnit()));
        contractItem.setPurchaseWay(sysBaseAPI.translateDict(ContractConst.DICT_PURCHASE_WAY, contractItem.getPurchaseWay()));
    }
}
