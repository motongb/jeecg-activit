package org.jeecg.modules.contract.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.contract.entity.ContractFieldParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 合同字段参数
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
public interface ContractFieldParamsMapper extends BaseMapper<ContractFieldParams> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
