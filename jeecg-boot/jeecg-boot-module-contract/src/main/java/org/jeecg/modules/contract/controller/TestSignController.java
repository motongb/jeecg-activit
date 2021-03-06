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
import org.jeecg.modules.contract.entity.TestSign;
import org.jeecg.modules.contract.service.ITestSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 会签流程表单
 * @Author: jeecg-boot
 * @Date: 2021-01-26
 * @Version: V1.0
 */
@Api(tags = "会签流程表单")
@RestController
@RequestMapping("/sign/testSign")
@Slf4j
public class TestSignController extends JeecgController<TestSign, ITestSignService> {

    @Autowired
    private ITestSignService testSignService;

    /**
     * 分页列表查询
     *
     * @param testSign
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "会签流程表单-分页列表查询")
    @ApiOperation(value = "会签流程表单-分页列表查询", notes = "会签流程表单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TestSign testSign,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TestSign> queryWrapper = QueryGenerator.initQueryWrapper(testSign, req.getParameterMap());
        Page<TestSign> page = new Page<>(pageNo, pageSize);
        IPage<TestSign> pageList = testSignService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param testSign
     * @return
     */
    @AutoLog(value = "会签流程表单-添加")
    @ApiOperation(value = "会签流程表单-添加", notes = "会签流程表单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TestSign testSign) {
        testSignService.save(testSign);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param testSign
     * @return
     */
    @AutoLog(value = "会签流程表单-编辑")
    @ApiOperation(value = "会签流程表单-编辑", notes = "会签流程表单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TestSign testSign) {
        testSignService.updateById(testSign);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "会签流程表单-通过id删除")
    @ApiOperation(value = "会签流程表单-通过id删除", notes = "会签流程表单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        testSignService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "会签流程表单-批量删除")
    @ApiOperation(value = "会签流程表单-批量删除", notes = "会签流程表单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.testSignService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "会签流程表单-通过id查询")
    @ApiOperation(value = "会签流程表单-通过id查询", notes = "会签流程表单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TestSign testSign = testSignService.getById(id);
        if (testSign == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(testSign);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param testSign
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestSign testSign) {
        return super.exportXls(request, testSign, TestSign.class, "会签流程表单");
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
        return super.importExcel(request, response, TestSign.class);
    }

}
