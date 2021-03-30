package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.contract.entity.ContractCovertMember;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.entity.ContractPayment;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.mapper.ContractPurchaseMapper;
import org.jeecg.modules.contract.service.IContractItemService;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.jeecg.modules.contract.service.IContractPaymentService;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
@Transactional
public class ContractPurchaseServiceImpl extends ServiceImpl<ContractPurchaseMapper, ContractPurchase> implements IContractPurchaseService {

    @Autowired
    private IActBusinessService actBusinessService;

    @Autowired
    private IContractMemberService contractMemberService;

    @Autowired
    private IContractItemService contractItemService;

    @Autowired
    private IContractPaymentService contractPaymentService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void saveWithProcess(ContractPurchase contractPurchase) {
        // 新增
        if (StringUtils.isEmpty(contractPurchase.getId())) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            contractPurchase.setUserId(sysUser.getUsername());
            contractPurchase.setStatus(ContractConst.STATUS_START);
            save(contractPurchase);
            Map<String, Object> processData = contractPurchase.getProcessData();
            processData.put("tableId", contractPurchase.getId());
            actBusinessService.saveBusiness(true, processData, contractPurchase.getParams());
        } else { //编辑
            updateById(contractPurchase);
            Map<String, Object> processData = contractPurchase.getProcessData();
            processData.put("tableId", contractPurchase.getId());
            actBusinessService.saveBusiness(false, processData, contractPurchase.getParams());
        }
        // 我方
        ContractCovertMember firstMember = contractPurchase.getFirstMemberObj();
        if (Objects.nonNull(firstMember)) {
            firstMember.setContractId(contractPurchase.getId());
            contractMemberService.saveOrUpdate(firstMember);
        }
        // 乙方
        ContractCovertMember secondMember = contractPurchase.getSecondMemberObj();
        if (Objects.nonNull(secondMember)) {
            secondMember.setContractId(contractPurchase.getId());
            contractMemberService.saveOrUpdate(secondMember);
        }
        // 丙方
        ContractCovertMember thirdMember = contractPurchase.getThirdMemberObj();
        if (ContractConst.MEMBER_USE_1.equals(contractPurchase.getMemberUse()) && Objects.nonNull(thirdMember)) {
            thirdMember.setContractId(contractPurchase.getId());
            contractMemberService.saveOrUpdate(thirdMember);
        }
    }

    @Override
    public void setMember(ContractPurchase contractPurchase, boolean translateDict) {
        List<ContractCovertMember> contractMembers = contractMemberService.list(new LambdaQueryWrapper<ContractCovertMember>()
                .eq(ContractCovertMember::getContractId, contractPurchase.getId()));
        for (ContractCovertMember contractMember : contractMembers) {
            if (translateDict) {
                contractMemberService.translateDict(contractMember);
            }
            if (ContractConst.FIRST_MEMBER.equals(contractMember.getType())) {
                contractPurchase.setFirstMemberObj(contractMember);
                contractPurchase.setFirstMemberName(contractMember.getNameCn());
            } else if (ContractConst.SECOND_MEMBER.equals(contractMember.getType())) {
                contractPurchase.setSecondMemberObj(contractMember);
                contractPurchase.setSecondMemberName(contractMember.getNameCn());
            } else if (ContractConst.THIRD_MEMBER.equals(contractMember.getType())) {
                contractPurchase.setThirdMemberObj(contractMember);
                contractPurchase.setThirdMemberName(contractMember.getNameCn());
            }
        }

    }

    @Override
    public IPage<ContractPurchase> pageVo(Page<ContractPurchase> page, ContractPurchase contractPurchase) {
        return this.baseMapper.pageVo(page, contractPurchase);
    }

    @Override
    public void saveMoreItem(ContractPurchase contractPurchase) {
        // 合同明细项
        List<ContractItem> contractItems = contractPurchase.getContractItems();
        if (!CollectionUtils.isEmpty(contractItems)) {
            contractItemService.remove(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractPurchase.getId()));
            contractItems.forEach(item -> item.setContractId(contractPurchase.getId()));
            contractItemService.saveBatch(contractItems);
        }
        // 合同付款约定
        List<ContractPayment> contractPayments = contractPurchase.getContractPayments();
        if (!CollectionUtils.isEmpty(contractPayments)) {
            contractPaymentService.remove(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractPurchase.getId()));
            contractPayments.forEach(item -> item.setContractId(contractPurchase.getId()));
            contractPaymentService.saveBatch(contractPayments);
        }
    }

    @Override
    public void setMoreItem(ContractPurchase contractPurchase, boolean translateDict) {
        // 合同明细项
        List<ContractItem> contractItems = contractItemService.list(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractPurchase.getId()));
        if (translateDict) {
            contractItems.forEach(contractItem -> contractItemService.translateDict(contractItem));
        }
        contractPurchase.setContractItems(contractItems);
        // 合同付款约定
        List<ContractPayment> contractPayments = contractPaymentService.list(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractPurchase.getId()));
        if (translateDict) {
            contractPayments.forEach(contractPayment -> contractPaymentService.translateDict(contractPayment));
        }
        contractPurchase.setContractPayments(contractPayments);
    }

    @Override
    public void removeMoreItem(String contractId) {
        contractItemService.remove(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractId));
        contractPaymentService.remove(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractId));
    }

    @Override
    public ContractPurchase getContractVoById(String contractId, boolean translateDict) {
        ContractPurchase contractPurchase = getById(contractId);
        if (translateDict) {
            translateDict(contractPurchase);
        }
        setMember(contractPurchase, translateDict);
        setMoreItem(contractPurchase, translateDict);
        return contractPurchase;
    }

    @Override
    public void translateDict(ContractPurchase contractPurchase) {
        contractPurchase.setPurchaseType(sysBaseAPI.translateDict(ContractConst.DICT_PURCHASE_WAY, contractPurchase.getPurchaseType()));
    }
}
