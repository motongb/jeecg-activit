package org.jeecg.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.contract.entity.CompanyBank;
import org.jeecg.modules.contract.mapper.CompanyBankMapper;
import org.jeecg.modules.contract.service.ICompanyBankService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description: 企业信息/合作方信息银行信息
 * @Author: jeecg-boot
 * @Date: 2021-02-09
 * @Version: V1.0
 */
@Service
public class CompanyBankServiceImpl extends ServiceImpl<CompanyBankMapper, CompanyBank> implements ICompanyBankService {

    @Override
    public void saveHandle(String companyId, List<CompanyBank> companyBanks) {
        if (CollectionUtils.isEmpty(companyBanks)) {
            return;
        }
        remove(new LambdaQueryWrapper<CompanyBank>().eq(CompanyBank::getCompanyId, companyId));
        companyBanks.forEach(item -> item.setCompanyId(companyId));
        saveBatch(companyBanks);
    }
}
