package org.jeecg.modules.activiti.web;

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
import org.jeecg.modules.activiti.entity.ActZForm;
import org.jeecg.modules.activiti.service.IActZFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 流程表单
 * @Author: jeecg-boot
 * @Date: 2021-04-20
 * @Version: V1.0
 */
@Api(tags = "流程表单")
@RestController
@RequestMapping("/activiti/actZForm")
@Slf4j
public class ActZFormController extends JeecgController<ActZForm, IActZFormService> {
    @Autowired
    private IActZFormService actZFormService;

    /**
     * 分页列表查询
     *
     * @param actZForm
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "流程表单-分页列表查询")
    @ApiOperation(value = "流程表单-分页列表查询", notes = "流程表单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ActZForm actZForm,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ActZForm> queryWrapper = QueryGenerator.initQueryWrapper(actZForm, req.getParameterMap());
        Page<ActZForm> page = new Page<ActZForm>(pageNo, pageSize);
        IPage<ActZForm> pageList = actZFormService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param actZForm
     * @return
     */
    @AutoLog(value = "流程表单-添加")
    @ApiOperation(value = "流程表单-添加", notes = "流程表单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ActZForm actZForm) {
        actZFormService.save(actZForm);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param actZForm
     * @return
     */
    @AutoLog(value = "流程表单-编辑")
    @ApiOperation(value = "流程表单-编辑", notes = "流程表单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ActZForm actZForm) {
        actZFormService.updateById(actZForm);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流程表单-通过id删除")
    @ApiOperation(value = "流程表单-通过id删除", notes = "流程表单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        actZFormService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "流程表单-批量删除")
    @ApiOperation(value = "流程表单-批量删除", notes = "流程表单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.actZFormService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流程表单-通过id查询")
    @ApiOperation(value = "流程表单-通过id查询", notes = "流程表单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ActZForm actZForm = actZFormService.getById(id);
        if (actZForm == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(actZForm);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param actZForm
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ActZForm actZForm) {
        return super.exportXls(request, actZForm, ActZForm.class, "流程表单");
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
        return super.importExcel(request, response, ActZForm.class);
    }

}
