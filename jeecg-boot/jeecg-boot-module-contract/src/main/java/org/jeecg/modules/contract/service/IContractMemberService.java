package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractMember;

/**
 * @Description: 合同签订对象
 * @Author: jeecg-boot
 * @Date: 2021-02-07
 * @Version: V1.0
 */
public interface IContractMemberService extends IService<ContractMember> {

    void translateDict(ContractMember contractMember);
}
