package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.entity.ContractSeal;
import org.jeecg.modules.contract.entity.ContractStamp;
import org.jeecg.modules.contract.entity.vo.ContractSealVo;
import org.jeecg.modules.contract.mapper.ContractStampMapper;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.jeecg.modules.contract.service.IContractSealService;
import org.jeecg.modules.contract.service.IContractStampService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

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

    @Autowired
    private IContractMemberService contractMemberService;

    @Autowired
    private IContractSealService contractSealService;

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
        contractStamp.setRecord(getContract(contractStamp.getTableId(), contractStamp.getTableName()));
        return contractStamp;
    }

    @Override
    public void updateContractStatus(String tableId, String tableName, String status) {
        this.baseMapper.updateContractStatus(tableId, tableName, status);
    }

    @Override
    public Map<String, Object> getContract(String tableId, String tableName) {
        Map<String, Object> sourceMap = this.baseMapper.getContract(tableId, tableName);
        Map<String, Object> targetMap = new HashMap<>();
        Objects.requireNonNull(sourceMap, "数据丢失");
        // 字段转换
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = oConvertUtils.camelName(entry.getKey());
            targetMap.put(key, entry.getValue());
        }
        List<ContractMember> contractMembers = contractMemberService.list(new LambdaQueryWrapper<ContractMember>()
                .eq(ContractMember::getContractId, sourceMap.get("id")));
        for (ContractMember contractMember : contractMembers) {
            if (ContractConst.FIRST_MEMBER.equals(contractMember.getType())) {
                targetMap.put("firstMemberObj", contractMember);
            } else if (ContractConst.SECOND_MEMBER.equals(contractMember.getType())) {
                targetMap.put("secondMemberObj", contractMember);
            } else if (ContractConst.THIRD_MEMBER.equals(contractMember.getType())) {
                targetMap.put("thirdMemberObj", contractMember);
            }
        }
        return targetMap;
    }

    @Override
    public IPage<ContractStamp> pageVo(Page<ContractStamp> page, ContractStamp contractStamp) {
        String sql = QueryGenerator.installAuthJdbc(ContractStamp.class);
        IPage<ContractStamp> pageList = this.baseMapper.pageVo(page, contractStamp, sql);
        List<ContractStamp> contractStamps = pageList.getRecords();
        contractStamps.forEach(item -> {
            String stampTypes = item.getStampType();
            if (StringUtils.hasText(stampTypes)) {
                String[] ids = stampTypes.split(",");
                String[] values = item.getStampNum().split(",");
                List<ContractSeal> seals = contractSealService.listByIds(Arrays.asList(ids));
                List<ContractSealVo> sealVos = new ArrayList<>();
                int i = 0;
                for (ContractSeal seal : seals) {
                    sealVos.add(new ContractSealVo(seal.getId(), seal.getName(), values[i]));
                    i++;
                }
                item.setSealVos(sealVos);
            }
        });
        pageList.setRecords(contractStamps);
        return pageList;
    }
}
