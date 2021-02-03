package org.jeecg.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.contract.entity.ContractType;

/**
 * @Description: 合同类型
 * @Author: jeecg-boot
 * @Date: 2021-02-02
 * @Version: V1.0
 */
public interface ContractTypeMapper extends BaseMapper<ContractType> {

    /**
     * 编辑节点状态
     *
     * @param id
     * @param status
     */
    void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
