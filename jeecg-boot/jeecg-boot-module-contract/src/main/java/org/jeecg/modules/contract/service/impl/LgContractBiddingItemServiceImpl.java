package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;
import org.jeecg.modules.contract.mapper.LgContractBiddingItemMapper;
import org.jeecg.modules.contract.service.ILgContractBiddingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 柳钢合同中标项
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Service
public class LgContractBiddingItemServiceImpl extends ServiceImpl<LgContractBiddingItemMapper, LgContractBiddingItem> implements ILgContractBiddingItemService {

    @Autowired
    private LgContractBiddingItemMapper lgContractBiddingItemMapper;

    @Override
    public List<LgContractBiddingItem> selectByMainId(String mainId) {
        return lgContractBiddingItemMapper.selectByMainId(mainId);
    }
}
