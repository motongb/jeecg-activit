package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.contract.entity.ContractPayment;
import org.jeecg.modules.contract.mapper.ContractPaymentMapper;
import org.jeecg.modules.contract.service.IContractPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 合同付款约定
 * @Author: jeecg-boot
 * @Date: 2021-02-07
 * @Version: V1.0
 */
@Service
public class ContractPaymentServiceImpl extends ServiceImpl<ContractPaymentMapper, ContractPayment> implements IContractPaymentService {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void translateDict(ContractPayment contractPayment) {

    }
}
