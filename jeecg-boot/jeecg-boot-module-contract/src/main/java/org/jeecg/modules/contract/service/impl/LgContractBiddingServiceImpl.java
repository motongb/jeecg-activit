package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.LgContractBidding;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;
import org.jeecg.modules.contract.mapper.LgContractBiddingItemMapper;
import org.jeecg.modules.contract.mapper.LgContractBiddingMapper;
import org.jeecg.modules.contract.service.ILgContractBiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 柳钢中标信息
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Service
public class LgContractBiddingServiceImpl extends ServiceImpl<LgContractBiddingMapper, LgContractBidding> implements ILgContractBiddingService {

    @Autowired
    private LgContractBiddingMapper lgContractBiddingMapper;
    @Autowired
    private LgContractBiddingItemMapper lgContractBiddingItemMapper;

    @Override
    @Transactional
    public void saveMain(LgContractBidding lgContractBidding, List<LgContractBiddingItem> lgContractBiddingItemList) {
        lgContractBiddingMapper.insert(lgContractBidding);
        if (lgContractBiddingItemList != null && lgContractBiddingItemList.size() > 0) {
            for (LgContractBiddingItem entity : lgContractBiddingItemList) {
                //外键设置
                entity.setBiddingId(lgContractBidding.getId());
                lgContractBiddingItemMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(LgContractBidding lgContractBidding, List<LgContractBiddingItem> lgContractBiddingItemList) {
        lgContractBiddingMapper.updateById(lgContractBidding);

        //1.先删除子表数据
        lgContractBiddingItemMapper.deleteByMainId(lgContractBidding.getId());

        //2.子表数据重新插入
        if (lgContractBiddingItemList != null && lgContractBiddingItemList.size() > 0) {
            for (LgContractBiddingItem entity : lgContractBiddingItemList) {
                //外键设置
                entity.setBiddingId(lgContractBidding.getId());
                lgContractBiddingItemMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        lgContractBiddingItemMapper.deleteByMainId(id);
        lgContractBiddingMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            lgContractBiddingItemMapper.deleteByMainId(id.toString());
            lgContractBiddingMapper.deleteById(id);
        }
    }

}
