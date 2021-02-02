package org.jeecg.modules.contract.service.impl;

import org.jeecg.modules.contract.entity.ContractBase;
import org.jeecg.modules.contract.mapper.ContractBaseMapper;
import org.jeecg.modules.contract.service.IContractBaseService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Service
public class ContractBaseServiceImpl extends ServiceImpl<ContractBaseMapper, ContractBase> implements IContractBaseService {

	@Autowired
	private ContractBaseMapper contractBaseMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		contractBaseMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			contractBaseMapper.deleteById(id);
		}
	}
	
}
