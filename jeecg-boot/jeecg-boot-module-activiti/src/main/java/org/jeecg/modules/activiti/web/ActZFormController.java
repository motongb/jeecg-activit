package org.jeecg.modules.activiti.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.activiti.entity.ActZFieldGroup;
import org.jeecg.modules.activiti.entity.ActZForm;
import org.jeecg.modules.activiti.entity.vo.ActZFormPage;
import org.jeecg.modules.activiti.service.IActZFieldGroupService;
import org.jeecg.modules.activiti.service.IActZFormService;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 流程表单
 * @Author: jeecg-boot
 * @Date: 2021-04-20
 * @Version: V1.0
 */
@Api(tags = "流程表单")
@RestController
@RequestMapping("/process/actZForm")
@Slf4j
public class ActZFormController extends JeecgController<ActZForm, IActZFormService> {
    @Autowired
    private IActZFormService actZFormService;
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    private IActZFieldGroupService actZFieldGroupService;


    @GetMapping(value = "/subTable")
    public Result<?> querySubTableByMain(@RequestParam String metaId) {
        OnlCgformHead cgformHead = onlCgformHeadService.getById(metaId);
        List<OnlCgformHead> subTable = new ArrayList<>();
        if (StringUtils.hasText(cgformHead.getSubTableStr())) {
            String[] subStr = cgformHead.getSubTableStr().split(",");
            for (String sub : subStr) {
                subTable.add(onlCgformHeadService.getOne(new LambdaQueryWrapper<OnlCgformHead>().eq(OnlCgformHead::getTableName, sub)));
            }
        }
        return Result.OK(subTable);
    }

    /**
     * 字段组
     *
     * @param actZFieldGroup
     * @return
     */
    @AutoLog(value = "字段组-添加")
    @ApiOperation(value = "字段组-添加", notes = "字段组-添加")
    @PostMapping(value = "/actZFieldGroup")
    public Result<?> add(@RequestBody ActZFieldGroup actZFieldGroup) {
        actZFieldGroupService.save(actZFieldGroup);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "字段组-编辑")
    @ApiOperation("字段组-编辑")
    @PutMapping(value = "/actZFieldGroup")
    public Result<?> edit(@RequestBody ActZFieldGroup actZFieldGroup) {
        actZFieldGroupService.updateById(actZFieldGroup);
        return Result.OK();
    }

    @AutoLog(value = "字段组-删除")
    @ApiOperation("字段组删除")
    @DeleteMapping(value = "/actZFieldGroup")
    public Result<?> deleteGroup(@RequestParam("ids") String ids) {
        actZFieldGroupService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流程表字段组通过主表ID查询")
    @ApiOperation(value = "流程表字段组主表ID查询", notes = "流程表字段组-通主表ID查询")
    @GetMapping(value = "/queryActZFieldGroupByMainId")
    public Result<?> queryActZFieldGroupListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ActZFieldGroup> actZFieldGroupList = actZFieldGroupService.selectByMainId(id);
        return Result.OK(actZFieldGroupList);
    }

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
        actZFieldGroupService.deleteByMainId(id);
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
        List<String> idList = Arrays.asList(ids.split(","));
        this.actZFormService.removeByIds(idList);
        idList.forEach(v -> actZFieldGroupService.deleteByMainId(v));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过code查询
     *
     * @param code
     * @return
     */
    @AutoLog(value = "流程表单-通过id查询")
    @ApiOperation(value = "流程表单-通过id查询", notes = "流程表单-通过id查询")
    @GetMapping(value = "/queryByCode/{code}")
    public Result<?> queryByCode(@PathVariable("code") String code) {
        ActZForm actZForm = actZFormService.getOne(new LambdaQueryWrapper<ActZForm>().eq(ActZForm::getCode, code));
        if (actZForm == null) {
            return Result.error("未找到对应数据");
        }
        ActZFormPage actZFormPage = new ActZFormPage();
        BeanUtils.copyProperties(actZForm, actZFormPage);
        actZFormPage.setActZFieldGroupList(actZFieldGroupService.selectByMainId(actZForm.getId()));
        return Result.OK(actZFormPage);
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
