package org.jeecg.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.contract.entity.ContractStamp;

import java.util.Map;

/**
 * @Description: 合同用印记录表
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
public interface ContractStampMapper extends BaseMapper<ContractStamp> {

    Map<String, Object> getContract(@Param("tableId") String tableId, @Param("tableName") String tableName);

    boolean updateContractStatus(@Param("tableId") String tableId, @Param("tableName") String tableName, @Param("status") String status);

    IPage<ContractStamp> pageVo(Page<ContractStamp> page, @Param("query") ContractStamp contractStamp, @Param("sql") String sql);
}
