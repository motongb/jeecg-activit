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
import org.jeecg.modules.contract.entity.ContractModel;
import org.jeecg.modules.contract.service.IContractModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 合同模板
 * @Author: jeecg-boot
 * @Date: 2021-03-05
 * @Version: V1.0
 */
@Api(tags = "合同模板")
@RestController
@RequestMapping("/contract/contractModel")
@Slf4j
public class ContractModelController extends JeecgController<ContractModel, IContractModelService> {
    @Autowired
    private IContractModelService contractModelService;

    /**
     * 分页列表查询
     *
     * @param contractModel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同模板-分页列表查询")
    @ApiOperation(value = "合同模板-分页列表查询", notes = "合同模板-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContractModel contractModel,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ContractModel> queryWrapper = QueryGenerator.initQueryWrapper(contractModel, req.getParameterMap());
        Page<ContractModel> page = new Page<ContractModel>(pageNo, pageSize);
        IPage<ContractModel> pageList = contractModelService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractModel
     * @return
     */
    @AutoLog(value = "合同模板-添加")
    @ApiOperation(value = "合同模板-添加", notes = "合同模板-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractModel contractModel) {
        contractModelService.save(contractModel);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractModel
     * @return
     */
    @AutoLog(value = "合同模板-编辑")
    @ApiOperation(value = "合同模板-编辑", notes = "合同模板-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractModel contractModel) {
        contractModelService.updateById(contractModel);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同模板-通过id删除")
    @ApiOperation(value = "合同模板-通过id删除", notes = "合同模板-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractModelService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同模板-批量删除")
    @ApiOperation(value = "合同模板-批量删除", notes = "合同模板-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractModelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同模板-通过id查询")
    @ApiOperation(value = "合同模板-通过id查询", notes = "合同模板-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractModel contractModel = contractModelService.getById(id);
        if (contractModel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contractModel);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractModel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractModel contractModel) {
        return super.exportXls(request, contractModel, ContractModel.class, "合同模板");
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
        return super.importExcel(request, response, ContractModel.class);
    }

}
