package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.ContractGeneral;
import org.jeecg.modules.contract.entity.vo.ContractGeneralVo;

/**
 * @Description: 一般采购合同
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
public interface IContractGeneralService extends IService<ContractGeneral> {


    void saveMoreItem(ContractGeneralVo contractGeneralVo);

    void setMoreItem(ContractGeneralVo contractGeneralVo);

    void removeMoreItem(String contractId);
}
