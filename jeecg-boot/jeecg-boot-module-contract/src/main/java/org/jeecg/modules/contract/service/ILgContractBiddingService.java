package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.LgContractBidding;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 柳钢中标信息
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
public interface ILgContractBiddingService extends IService<LgContractBidding> {

    /**
     * 添加一对多
     */
    public void saveMain(LgContractBidding lgContractBidding, List<LgContractBiddingItem> lgContractBiddingItemList);

    /**
     * 修改一对多
     */
    public void updateMain(LgContractBidding lgContractBidding, List<LgContractBiddingItem> lgContractBiddingItemList);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

}
