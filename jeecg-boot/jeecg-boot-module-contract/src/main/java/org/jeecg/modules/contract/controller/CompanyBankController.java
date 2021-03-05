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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.contract.entity.CompanyBank;
import org.jeecg.modules.contract.service.ICompanyBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 企业信息/合作方信息银行信息
 * @Author: jeecg-boot
 * @Date: 2021-02-09
 * @Version: V1.0
 */
@Api(tags = "企业信息-合作方信息银行信息")
@RestController
@RequestMapping("/contract/companyBank")
@Slf4j
public class CompanyBankController extends JeecgController<CompanyBank, ICompanyBankService> {
    @Autowired
    private ICompanyBankService companyBankService;

    /**
     * 分页列表查询
     *
     * @param companyBank
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息银行信息-分页列表查询")
    @ApiOperation(value = "企业信息/合作方信息银行信息-分页列表查询", notes = "企业信息/合作方信息银行信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(CompanyBank companyBank,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<CompanyBank> queryWrapper = QueryGenerator.initQueryWrapper(companyBank, req.getParameterMap());
        Page<CompanyBank> page = new Page<CompanyBank>(pageNo, pageSize);
        IPage<CompanyBank> pageList = companyBankService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param companyBank
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息银行信息-添加")
    @ApiOperation(value = "企业信息/合作方信息银行信息-添加", notes = "企业信息/合作方信息银行信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CompanyBank companyBank) {
        companyBankService.save(companyBank);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param companyBank
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息银行信息-编辑")
    @ApiOperation(value = "企业信息/合作方信息银行信息-编辑", notes = "企业信息/合作方信息银行信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CompanyBank companyBank) {
        companyBankService.updateById(companyBank);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息银行信息-通过id删除")
    @ApiOperation(value = "企业信息/合作方信息银行信息-通过id删除", notes = "企业信息/合作方信息银行信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyBankService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息银行信息-批量删除")
    @ApiOperation(value = "企业信息/合作方信息银行信息-批量删除", notes = "企业信息/合作方信息银行信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyBankService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "企业信息/合作方信息银行信息-通过id查询")
    @ApiOperation(value = "企业信息/合作方信息银行信息-通过id查询", notes = "企业信息/合作方信息银行信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyBank companyBank = companyBankService.getById(id);
        if (companyBank == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(companyBank);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companyBank
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyBank companyBank) {
        return super.exportXls(request, companyBank, CompanyBank.class, "企业信息/合作方信息银行信息");
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
        return super.importExcel(request, response, CompanyBank.class);
    }

}
