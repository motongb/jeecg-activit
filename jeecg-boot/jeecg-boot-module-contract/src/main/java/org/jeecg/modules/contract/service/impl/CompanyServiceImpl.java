package org.jeecg.modules.contract.service.impl;

import org.jeecg.modules.contract.entity.Company;
import org.jeecg.modules.contract.mapper.CompanyMapper;
import org.jeecg.modules.contract.service.ICompanyService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 企业信息/合作方信息
 * @Author: jeecg-boot
 * @Date:   2021-02-08
 * @Version: V1.0
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

}
