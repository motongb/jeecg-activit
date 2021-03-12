package org.jeecg.modules.contract.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.contract.entity.ContractItem;
import org.jeecg.modules.contract.entity.ContractMember;
import org.jeecg.modules.contract.entity.ContractPayment;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.entity.vo.ContractPurchaseVo;
import org.jeecg.modules.contract.mapper.ContractPurchaseMapper;
import org.jeecg.modules.contract.service.IContractItemService;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.jeecg.modules.contract.service.IContractPaymentService;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void setMember(ContractPurchaseVo contractPurchaseVo, boolean translateDict) {
        List<ContractMember> contractMembers = contractMemberService.list(new LambdaQueryWrapper<ContractMember>()
                .eq(ContractMember::getContractId, contractPurchaseVo.getId()));
        for (ContractMember contractMember : contractMembers) {
            if (translateDict) {
                contractMemberService.translateDict(contractMember);
            }
            if (ContractConst.FIRST_MEMBER.equals(contractMember.getType())) {
                contractPurchaseVo.setFirstMemberObj(contractMember);
                contractPurchaseVo.setFirstMemberName(contractMember.getNameCn());
            } else if (ContractConst.SECOND_MEMBER.equals(contractMember.getType())) {
                contractPurchaseVo.setSecondMemberObj(contractMember);
                contractPurchaseVo.setSecondMemberName(contractMember.getNameCn());
            } else if (ContractConst.THIRD_MEMBER.equals(contractMember.getType())) {
                contractPurchaseVo.setThirdMemberObj(contractMember);
                contractPurchaseVo.setThirdMemberName(contractMember.getNameCn());
            }
        }

    }

    @Override
    public IPage<ContractPurchase> pageVo(Page<ContractPurchase> page, ContractPurchase contractPurchase) {
        return this.baseMapper.pageVo(page, contractPurchase);
    }

    @Override
    public void saveMoreItem(ContractPurchaseVo contractPurchaseVo) {
        // 合同明细项
        List<ContractItem> contractItems = contractPurchaseVo.getContractItems();
        if (!CollectionUtils.isEmpty(contractItems)) {
            contractItemService.remove(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractPurchaseVo.getId()));
            contractItems.forEach(item -> item.setContractId(contractPurchaseVo.getId()));
            contractItemService.saveBatch(contractItems);
        }
        // 合同付款约定
        List<ContractPayment> contractPayments = contractPurchaseVo.getContractPayments();
        if (!CollectionUtils.isEmpty(contractPayments)) {
            contractPaymentService.remove(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractPurchaseVo.getId()));
            contractPayments.forEach(item -> item.setContractId(contractPurchaseVo.getId()));
            contractPaymentService.saveBatch(contractPayments);
        }
    }

    @Override
    public void setMoreItem(ContractPurchaseVo contractPurchaseVo, boolean translateDict) {
        // 合同明细项
        List<ContractItem> contractItems = contractItemService.list(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractPurchaseVo.getId()));
        if (translateDict) {
            contractItems.forEach(contractItem -> contractItemService.translateDict(contractItem));
        }
        contractPurchaseVo.setContractItems(contractItems);
        // 合同付款约定
        List<ContractPayment> contractPayments = contractPaymentService.list(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractPurchaseVo.getId()));
        if (translateDict) {
            contractPayments.forEach(contractPayment -> contractPaymentService.translateDict(contractPayment));
        }
        contractPurchaseVo.setContractPayments(contractPayments);
    }

    @Override
    public void removeMoreItem(String contractId) {
        contractItemService.remove(new LambdaQueryWrapper<ContractItem>().eq(ContractItem::getContractId, contractId));
        contractPaymentService.remove(new LambdaQueryWrapper<ContractPayment>().eq(ContractPayment::getContractId, contractId));
    }

    @Override
    public ContractPurchaseVo getContractVoById(String contractId, boolean translateDict) {
        ContractPurchase contractPurchase = getById(contractId);
        if (translateDict) {
            translateDict(contractPurchase);
        }
        ContractPurchaseVo contractPurchaseVo = JSONUtil.toBean(JSONUtil.toJsonStr(contractPurchase), ContractPurchaseVo.class);
        setMember(contractPurchaseVo, translateDict);
        setMoreItem(contractPurchaseVo, translateDict);
        return contractPurchaseVo;
    }

    @Override
    public void translateDict(ContractPurchase contractPurchase) {
        contractPurchase.setPurchaseType(sysBaseAPI.translateDict(ContractConst.DICT_PURCHASE_WAY, contractPurchase.getPurchaseType()));
    }
}
