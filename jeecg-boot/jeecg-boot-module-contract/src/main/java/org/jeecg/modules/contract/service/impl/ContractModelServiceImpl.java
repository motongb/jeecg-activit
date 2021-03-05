package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.ContractModel;
import org.jeecg.modules.contract.mapper.ContractModelMapper;
import org.jeecg.modules.contract.service.IContractModelService;
import org.springframework.stereotype.Service;

/**
 * @Description: 合同模板
 * @Author: jeecg-boot
 * @Date: 2021-03-05
 * @Version: V1.0
 */
@Service
public class ContractModelServiceImpl extends ServiceImpl<ContractModelMapper, ContractModel> implements IContractModelService {

}
