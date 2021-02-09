package org.jeecg.modules.contract.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.contract.entity.Company;
import org.jeecg.modules.contract.entity.CompanyBank;
import org.jeecg.modules.contract.service.ICompanyBankService;
import org.jeecg.modules.contract.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 企业信息/合作方信息
 * @Author: jeecg-boot
 * @Date: 2021-02-08
 * @Version: V1.0
 */
@Api(tags = "企业信息/合作方信息")
@RestController
@RequestMapping("/contract/company")
@Slf4j
public class CompanyController extends JeecgController<Company, ICompanyService> {
    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICompanyBankService companyBankService;

    /**
     * 分页列表查询
     *
     * @param company
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息-分页列表查询")
    @ApiOperation(value = "企业信息/合作方信息-分页列表查询", notes = "企业信息/合作方信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Company company,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        Page<Company> page = new Page<>(pageNo, pageSize);
        if (StringUtils.hasText(company.getNameCn())) {
            queryWrapper.like("name_cn", company.getNameCn());
        }
        IPage<Company> pageList = companyService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param company
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息-添加")
    @ApiOperation(value = "企业信息/合作方信息-添加", notes = "企业信息/合作方信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Company company) {
        companyService.save(company);
        List<CompanyBank> companyBanks = company.getCompanyBanks();
        companyBankService.saveHandle(company.getId(), companyBanks);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param company
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息-编辑")
    @ApiOperation(value = "企业信息/合作方信息-编辑", notes = "企业信息/合作方信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Company company) {
        companyService.updateById(company);
        List<CompanyBank> companyBanks = company.getCompanyBanks();
        companyBankService.saveHandle(company.getId(), companyBanks);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息-通过id删除")
    @ApiOperation(value = "企业信息/合作方信息-通过id删除", notes = "企业信息/合作方信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息-批量删除")
    @ApiOperation(value = "企业信息/合作方信息-批量删除", notes = "企业信息/合作方信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息-通过id查询")
    @ApiOperation(value = "企业信息/合作方信息-通过id查询", notes = "企业信息/合作方信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Company company = companyService.getById(id);
        if (company == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(company);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param company
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Company company) {
        return super.exportXls(request, company, Company.class, "企业信息/合作方信息");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Company.class);
    }

}
