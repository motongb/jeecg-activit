package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.mapper.ContractMemberMapper;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 合同签订对象
 * @Author: jeecg-boot
 * @Date: 2021-02-07
 * @Version: V1.0
 */
@Service
public class ContractMemberServiceImpl extends ServiceImpl<ContractMemberMapper, ContractMember> implements IContractMemberService {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void translateDict(ContractMember contractMember) {
        contractMember.setCoin(sysBaseAPI.translateDict(ContractConst.DICT_COIN, contractMember.getCoin()));
    }
}
