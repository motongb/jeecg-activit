package org.jeecg.modules.contract.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
@Api(tags = "采购合同基础表")
@RestController
@RequestMapping("/contract/contractPurchase")
@Slf4j
public class ContractPurchaseController extends JeecgController<ContractPurchase, IContractPurchaseService> {
    @Autowired
    private IContractPurchaseService contractPurchaseService;

    /**
     * 分页列表查询
     *
     * @param contractPurchase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "采购合同基础表-分页列表查询")
    @ApiOperation(value = "采购合同基础表-分页列表查询", notes = "采购合同基础表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContractPurchase contractPurchase,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<ContractPurchase> queryWrapper = QueryGenerator.initQueryWrapper(contractPurchase, req.getParameterMap());
        Page<ContractPurchase> page = new Page<>(pageNo, pageSize);
        IPage<ContractPurchase> pageList = contractPurchaseService.pageVo(page, contractPurchase);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractPurchase
     * @return
     */
    @AutoLog(value = "采购合同基础表-添加")
    @ApiOperation(value = "采购合同基础表-添加", notes = "采购合同基础表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractPurchase contractPurchase) {
        contractPurchaseService.save(contractPurchase);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractPurchase
     * @return
     */
    @AutoLog(value = "采购合同基础表-编辑")
    @ApiOperation(value = "采购合同基础表-编辑", notes = "采购合同基础表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractPurchase contractPurchase) {
        contractPurchaseService.updateById(contractPurchase);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购合同基础表-通过id删除")
    @ApiOperation(value = "采购合同基础表-通过id删除", notes = "采购合同基础表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractPurchaseService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "采购合同基础表-批量删除")
    @ApiOperation(value = "采购合同基础表-批量删除", notes = "采购合同基础表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractPurchaseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购合同基础表-通过id查询")
    @ApiOperation(value = "采购合同基础表-通过id查询", notes = "采购合同基础表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractPurchase contractPurchase = contractPurchaseService.getById(id);
        if (contractPurchase == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contractPurchase);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractPurchase
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractPurchase contractPurchase) {
        return super.exportXls(request, contractPurchase, ContractPurchase.class, "采购合同基础表");
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
        return super.importExcel(request, response, ContractPurchase.class);
    }

}
