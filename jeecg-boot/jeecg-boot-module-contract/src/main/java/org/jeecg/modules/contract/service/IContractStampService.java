package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractStamp;

/**
 * @Description: 合同用印记录表
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
public interface IContractStampService extends IService<ContractStamp> {

    void saveWithProcess(ContractStamp contractStamp);

    ContractStamp getVoById(String id);

    boolean updateContractStatus(String tableId, String tableName, String status);
}
