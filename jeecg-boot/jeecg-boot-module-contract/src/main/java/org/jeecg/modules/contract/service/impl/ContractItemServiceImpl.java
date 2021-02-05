package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.mapper.ContractItemMapper;
import org.jeecg.modules.contract.service.IContractItemService;
import org.springframework.stereotype.Service;

/**
 * @Description: 合同明细项
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Service
public class ContractItemServiceImpl extends ServiceImpl<ContractItemMapper, ContractItem> implements IContractItemService {

}
