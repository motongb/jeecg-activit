package org.jeecg.modules.contract.service;

import org.jeecg.modules.contract.entity.ContractBase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
public interface IContractBaseService extends IService<ContractBase> {

	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);


}
