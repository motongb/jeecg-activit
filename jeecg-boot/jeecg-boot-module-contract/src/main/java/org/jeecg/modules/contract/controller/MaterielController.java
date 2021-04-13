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
import org.jeecg.modules.contract.entity.Materiel;
import org.jeecg.modules.contract.service.IMaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 物资表
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Api(tags = "物资表")
@RestController
@RequestMapping("/contract/materiel")
@Slf4j
public class MaterielController extends JeecgController<Materiel, IMaterielService> {
    @Autowired
    private IMaterielService materielService;

    /**
     * 分页列表查询
     *
     * @param materiel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "物资表-分页列表查询")
    @ApiOperation(value = "物资表-分页列表查询", notes = "物资表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Materiel materiel,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Materiel> queryWrapper = QueryGenerator.initQueryWrapper(materiel, req.getParameterMap());
        Page<Materiel> page = new Page<Materiel>(pageNo, pageSize);
        IPage<Materiel> pageList = materielService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param materiel
     * @return
     */
    @AutoLog(value = "物资表-添加")
    @ApiOperation(value = "物资表-添加", notes = "物资表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Materiel materiel) {
        materielService.save(materiel);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param materiel
     * @return
     */
    @AutoLog(value = "物资表-编辑")
    @ApiOperation(value = "物资表-编辑", notes = "物资表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Materiel materiel) {
        materielService.updateById(materiel);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物资表-通过id删除")
    @ApiOperation(value = "物资表-通过id删除", notes = "物资表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        materielService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物资表-批量删除")
    @ApiOperation(value = "物资表-批量删除", notes = "物资表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.materielService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物资表-通过id查询")
    @ApiOperation(value = "物资表-通过id查询", notes = "物资表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Materiel materiel = materielService.getById(id);
        if (materiel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(materiel);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param materiel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Materiel materiel) {
        return super.exportXls(request, materiel, Materiel.class, "物资表");
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
        return super.importExcel(request, response, Materiel.class);
    }

}
