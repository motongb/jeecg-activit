package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;

import java.util.List;

/**
 * @Description: 柳钢合同中标项
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
public interface ILgContractBiddingItemService extends IService<LgContractBiddingItem> {

    public List<LgContractBiddingItem> selectByMainId(String mainId);
}
