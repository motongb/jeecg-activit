package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.ContractGeneral;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.entity.ContractPayment;
import org.jeecg.modules.contract.entity.vo.ContractGeneralVo;
import org.jeecg.modules.contract.mapper.ContractGeneralMapper;
import org.jeecg.modules.contract.service.IContractGeneralService;
import org.jeecg.modules.contract.service.IContractItemService;
import org.jeecg.modules.contract.service.IContractPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description: 一般采购合同
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
@Service
public class ContractGeneralServiceImpl extends ServiceImpl<ContractGeneralMapper, ContractGeneral> implements IContractGeneralService {

    @Autowired
    private IContractItemService contractItemService;

    @Autowired
    private IContractPaymentService contractPaymentService;


    @Override
    public void saveMoreItem(ContractGeneralVo contractGeneralVo) {
        // 合同明细项
        List<ContractItem> contractItems = contractGeneralVo.getContractItems();
        if (!CollectionUtils.isEmpty(contractItems)) {
            contractItemService.remove(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractGeneralVo.getId()));
            contractItems.forEach(item -> item.setContractId(contractGeneralVo.getId()));
            contractItemService.saveBatch(contractItems);
        }
        // 合同付款约定
        List<ContractPayment> contractPayments = contractGeneralVo.getContractPayments();
        if (!CollectionUtils.isEmpty(contractPayments)) {
            contractPaymentService.remove(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractGeneralVo.getId()));
            contractPayments.forEach(item -> item.setContractId(contractGeneralVo.getId()));
            contractPaymentService.saveBatch(contractPayments);
        }
    }

    @Override
    public void setMoreItem(ContractGeneralVo contractGeneralVo) {
        // 合同明细项
        List<ContractItem> contractItems = contractItemService.list(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractGeneralVo.getId()));
        contractGeneralVo.setContractItems(contractItems);
        // 合同付款约定
        List<ContractPayment> contractPayments = contractPaymentService.list(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractGeneralVo.getId()));
        contractGeneralVo.setContractPayments(contractPayments);
    }

    @Override
    public void removeMoreItem(String contractId) {
        contractItemService.remove(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractId));
        contractPaymentService.remove(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractId));
    }

}
