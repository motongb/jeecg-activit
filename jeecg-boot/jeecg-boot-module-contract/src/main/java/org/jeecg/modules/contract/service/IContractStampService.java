package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractStamp;

import java.util.Map;

/**
 * @Description: 合同用印记录表
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
public interface IContractStampService extends IService<ContractStamp> {

    void saveWithProcess(ContractStamp contractStamp);

    ContractStamp getVoById(String id);

    void updateContractStatus(String tableId, String tableName, String status);

    Map<String, Object> getContract(String tableId, String tableName);

    IPage<ContractStamp> pageVo(Page<ContractStamp> page, ContractStamp contractStamp);
}
