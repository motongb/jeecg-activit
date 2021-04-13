package org.jeecg.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;

import java.util.List;

/**
 * @Description: 柳钢合同中标项
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
public interface LgContractBiddingItemMapper extends BaseMapper<LgContractBiddingItem> {

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<LgContractBiddingItem> selectByMainId(@Param("mainId") String mainId);
}
