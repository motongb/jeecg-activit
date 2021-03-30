package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.contract.entity.ContractStamp;
import org.jeecg.modules.contract.mapper.ContractStampMapper;
import org.jeecg.modules.contract.service.IContractStampService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 合同用印记录表
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
@Service
@Transactional
public class ContractStampServiceImpl extends ServiceImpl<ContractStampMapper, ContractStamp> implements IContractStampService {

    @Autowired
    private IActBusinessService actBusinessService;

    @Override
    public void saveWithProcess(ContractStamp contractStamp) {
        // 新增
        if (StringUtils.isEmpty(contractStamp.getId())) {
            // 设置用印状态为发起用印
            contractStamp.setStatus(ContractConst.STAMP_STATUS_1);
            this.baseMapper.insert(contractStamp);
            Map<String, Object> processData = contractStamp.getProcessData();
            processData.put("tableId", contractStamp.getId());
            actBusinessService.saveBusiness(true, processData, contractStamp.getParams());
            // 更新合同状态为发起用印
            this.baseMapper.updateContractStatus(contractStamp.getTableId(), contractStamp.getTableName(), ContractConst.STATUS_STAMP_START);
        } else { // 修改
            this.baseMapper.updateById(contractStamp);
            Map<String, Object> processData = contractStamp.getProcessData();
            processData.put("tableId", contractStamp.getId());
            actBusinessService.saveBusiness(false, processData, contractStamp.getParams());
        }
    }

    @Override
    public ContractStamp getVoById(String id) {
        ContractStamp contractStamp = this.baseMapper.selectById(id);
        Assert.notNull(contractStamp, "记录不存在");
        // 查询合同数据
        Map<String, Object> sourceMap = this.baseMapper.getContract(contractStamp.getTableId(), contractStamp.getTableName());
        Map<String, Object> targetMap = new HashMap<>();
        if (Objects.nonNull(sourceMap)) {
            // 字段转换
            for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                String key = oConvertUtils.camelName(entry.getKey());
                targetMap.put(key, entry.getValue());
            }
            contractStamp.setRecord(targetMap);
        }
        return contractStamp;
    }

    @Override
    public boolean updateContractStatus(String tableId, String tableName, String status) {
        return this.baseMapper.updateContractStatus(tableId, tableName, status);
    }
}
