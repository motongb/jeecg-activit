package org.jeecg.modules.contract.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.entity.vo.ContractPurchaseVo;
import org.jeecg.modules.contract.mapper.ContractPurchaseMapper;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
@Service
public class ContractPurchaseServiceImpl extends ServiceImpl<ContractPurchaseMapper, ContractPurchase> implements IContractPurchaseService {

    @Autowired
    private IActBusinessService actBusinessService;

    @Autowired
    private IContractMemberService contractMemberService;

    @Override
    public void saveWithProcess(ContractPurchaseVo contractPurchaseVo) {
        ContractPurchase contractPurchase = JSONUtil.toBean(JSONUtil.toJsonStr(contractPurchaseVo), ContractPurchase.class);
        // 新增
        if (StringUtils.isEmpty(contractPurchaseVo.getId())) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            contractPurchase.setUserId(sysUser.getUsername());
            contractPurchase.setStatus(ContractConst.STATUS_START);
            save(contractPurchase);
            contractPurchaseVo.setId(contractPurchase.getId());
            Map<String, Object> processData = contractPurchaseVo.getProcessData();
            processData.put("tableId", contractPurchaseVo.getId());
            actBusinessService.saveBusiness(true, processData, contractPurchaseVo.getParams());
        } else { //编辑
            updateById(contractPurchase);
            Map<String, Object> processData = contractPurchaseVo.getProcessData();
            processData.put("tableId", contractPurchaseVo.getId());
            actBusinessService.saveBusiness(false, processData, contractPurchaseVo.getParams());
        }
        // 我方
        ContractMember firstMember = contractPurchaseVo.getFirstMemberObj();
        if (Objects.nonNull(firstMember)) {
            firstMember.setContractId(contractPurchaseVo.getId());
            contractMemberService.saveOrUpdate(firstMember);
        }
        // 乙方
        ContractMember secondMember = contractPurchaseVo.getSecondMemberObj();
        if (Objects.nonNull(secondMember)) {
            secondMember.setContractId(contractPurchaseVo.getId());
            contractMemberService.saveOrUpdate(secondMember);
        }
        // 丙方
        ContractMember thirdMember = contractPurchaseVo.getThirdMemberObj();
        if ("1".equals(contractPurchaseVo.getMemberUse()) && Objects.nonNull(thirdMember)) {
            thirdMember.setContractId(contractPurchaseVo.getId());
            contractMemberService.saveOrUpdate(thirdMember);
        }
    }

    @Override
    public void setMember(ContractPurchaseVo contractPurchaseVo) {
        List<ContractMember> contractMembers = contractMemberService.list(new LambdaQueryWrapper<ContractMember>()
                .eq(ContractMember::getContractId, contractPurchaseVo.getId()));
        for (ContractMember contractMember : contractMembers) {
            if (ContractConst.FIRST_MEMBER.equals(contractMember.getType())) {
                contractPurchaseVo.setFirstMemberObj(contractMember);
            } else if (ContractConst.SECOND_MEMBER.equals(contractMember.getType())) {
                contractPurchaseVo.setSecondMemberObj(contractMember);
            } else if (ContractConst.THIRD_MEMBER.equals(contractMember.getType())) {
                contractPurchaseVo.setThirdMemberObj(contractMember);
            }
        }

    }

    @Override
    public IPage<ContractPurchase> pageVo(Page<ContractPurchase> page, ContractPurchase contractPurchase) {
        return this.baseMapper.pageVo(page, contractPurchase);
    }
}
