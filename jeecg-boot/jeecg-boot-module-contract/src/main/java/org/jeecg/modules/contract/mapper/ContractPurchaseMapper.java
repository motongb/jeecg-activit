package org.jeecg.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.contract.entity.ContractPurchase;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
public interface ContractPurchaseMapper extends BaseMapper<ContractPurchase> {

    IPage<ContractPurchase> pageVo(Page<ContractPurchase> page, @Param("query") ContractPurchase contractPurchase);
}
