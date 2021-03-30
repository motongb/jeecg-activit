package org.jeecg.modules.contract.handle;

import org.jeecg.listener.IActivitiEventListener;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.contract.entity.ContractStamp;
import org.jeecg.modules.contract.service.IContractStampService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author motb
 * @date 2021/3/30 16:43
 * @description //TODO ContractStampActivitiEventHandler
 **/
@Component
@Transactional
public class ContractStampActivitiEventHandler implements IActivitiEventListener {

    @Autowired
    private IContractStampService contractStampService;

    @Override
    public void apply(ActBusiness actBusiness) {
        ContractStamp contractStamp = contractStampService.getById(actBusiness.getTableId());
        contractStamp.setStatus(ContractConst.STAMP_STATUS_2);
        contractStampService.updateContractStatus(contractStamp.getTableId(), contractStamp.getTableName(), ContractConst.STATUS_STAMP_SIGNING);
        contractStampService.updateById(contractStamp);
    }

    @Override
    public void cancel(ActBusiness actBusiness) {
        ContractStamp contractStamp = contractStampService.getById(actBusiness.getTableId());
        contractStamp.setStatus(ContractConst.STAMP_STATUS_1);
        contractStampService.updateContractStatus(contractStamp.getTableId(), contractStamp.getTableName(), ContractConst.STATUS_STAMP_START);
        contractStampService.updateById(contractStamp);
    }

    @Override
    public void back(ActBusiness actBusiness) {
        ContractStamp contractStamp = contractStampService.getById(actBusiness.getTableId());
        contractStamp.setStatus(ContractConst.STAMP_STATUS_4);
        contractStampService.updateContractStatus(contractStamp.getTableId(), contractStamp.getTableName(), ContractConst.STATUS_STAMP_SIGNING);
        contractStampService.updateById(contractStamp);
    }

    @Override
    public void pass(ActBusiness actBusiness) {

    }

    @Override
    public void finalized(ActBusiness actBusiness) {
        ContractStamp contractStamp = contractStampService.getById(actBusiness.getTableId());
        contractStamp.setStatus(ContractConst.STAMP_STATUS_3);
        contractStampService.updateContractStatus(contractStamp.getTableId(), contractStamp.getTableName(), ContractConst.STATUS_STAMP_COMPLETE);
        contractStampService.updateById(contractStamp);
    }

    @Override
    public void delete(ActBusiness actBusiness) {
        ContractStamp contractStamp = contractStampService.getById(actBusiness.getTableId());
        contractStampService.updateContractStatus(contractStamp.getTableId(), contractStamp.getTableName(), ContractConst.STATUS_FINALIZED);
    }
}
