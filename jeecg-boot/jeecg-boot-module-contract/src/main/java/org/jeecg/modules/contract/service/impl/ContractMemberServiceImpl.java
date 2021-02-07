package org.jeecg.modules.contract.service.impl;

import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.mapper.ContractMemberMapper;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 合同签订对象
 * @Author: jeecg-boot
 * @Date:   2021-02-07
 * @Version: V1.0
 */
@Service
public class ContractMemberServiceImpl extends ServiceImpl<ContractMemberMapper, ContractMember> implements IContractMemberService {

}
