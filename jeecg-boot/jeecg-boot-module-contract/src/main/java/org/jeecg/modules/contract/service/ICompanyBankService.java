package org.jeecg.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.contract.entity.CompanyBank;

import java.util.List;

/**
 * @Description: 企业信息/合作方信息银行信息
 * @Author: jeecg-boot
 * @Date: 2021-02-09
 * @Version: V1.0
 */
public interface ICompanyBankService extends IService<CompanyBank> {

    void saveHandle(String companyId, List<CompanyBank> companyBanks);

}
