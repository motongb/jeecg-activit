package org.jeecg.modules.contract.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.entity.vo.ContractPurchaseVo;
import org.jeecg.modules.contract.mapper.ContractPurchaseMapper;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.jeecg.modules.contract.utils.ContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

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

    @Override
    public boolean saveWithProcess(ContractPurchaseVo contractPurchaseVo) {
        ContractPurchase contractPurchase = JSONUtil.toBean(JSONUtil.toJsonStr(contractPurchaseVo), ContractPurchase.class);
        // 新增
        if (StringUtils.isEmpty(contractPurchase.getId())) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            contractPurchase.setUserId(sysUser.getUsername());
            contractPurchase.setStatus(ContractConst.STATUS_START);
            save(contractPurchase);
            contractPurchaseVo.setId(contractPurchase.getId());
            Map<String, Object> processData = contractPurchaseVo.getProcessData();
            processData.put("tableId", contractPurchaseVo.getId());
            actBusinessService.saveBusiness(true, processData, contractPurchaseVo.getParams());
        } else {
            updateById(contractPurchase);
            actBusinessService.saveBusiness(false, contractPurchaseVo.getProcessData(), contractPurchaseVo.getParams());
        }
        return true;
    }
}
