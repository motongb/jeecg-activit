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
import org.jeecg.modules.contract.entity.LgContractPurchase;
import org.jeecg.modules.contract.service.ILgContractPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 柳钢采购合同表
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Api(tags = "柳钢采购合同表")
@RestController
@RequestMapping("/contract/lgContractPurchase")
@Slf4j
public class LgContractPurchaseController extends JeecgController<LgContractPurchase, ILgContractPurchaseService> {
    @Autowired
    private ILgContractPurchaseService lgContractPurchaseService;

    /**
     * 分页列表查询
     *
     * @param lgContractPurchase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "柳钢采购合同表-分页列表查询")
    @ApiOperation(value = "柳钢采购合同表-分页列表查询", notes = "柳钢采购合同表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LgContractPurchase lgContractPurchase,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<LgContractPurchase> queryWrapper = QueryGenerator.initQueryWrapper(lgContractPurchase, req.getParameterMap());
        Page<LgContractPurchase> page = new Page<LgContractPurchase>(pageNo, pageSize);
        IPage<LgContractPurchase> pageList = lgContractPurchaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param lgContractPurchase
     * @return
     */
    @AutoLog(value = "柳钢采购合同表-添加")
    @ApiOperation(value = "柳钢采购合同表-添加", notes = "柳钢采购合同表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LgContractPurchase lgContractPurchase) {
        lgContractPurchaseService.save(lgContractPurchase);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param lgContractPurchase
     * @return
     */
    @AutoLog(value = "柳钢采购合同表-编辑")
    @ApiOperation(value = "柳钢采购合同表-编辑", notes = "柳钢采购合同表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LgContractPurchase lgContractPurchase) {
        lgContractPurchaseService.updateById(lgContractPurchase);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "柳钢采购合同表-通过id删除")
    @ApiOperation(value = "柳钢采购合同表-通过id删除", notes = "柳钢采购合同表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        lgContractPurchaseService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "柳钢采购合同表-批量删除")
    @ApiOperation(value = "柳钢采购合同表-批量删除", notes = "柳钢采购合同表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.lgContractPurchaseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "柳钢采购合同表-通过id查询")
    @ApiOperation(value = "柳钢采购合同表-通过id查询", notes = "柳钢采购合同表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LgContractPurchase lgContractPurchase = lgContractPurchaseService.getById(id);
        if (lgContractPurchase == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(lgContractPurchase);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param lgContractPurchase
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LgContractPurchase lgContractPurchase) {
        return super.exportXls(request, lgContractPurchase, LgContractPurchase.class, "柳钢采购合同表");
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
        return super.importExcel(request, response, LgContractPurchase.class);
    }

}
