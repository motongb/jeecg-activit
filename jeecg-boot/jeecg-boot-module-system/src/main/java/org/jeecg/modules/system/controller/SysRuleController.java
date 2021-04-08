package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysRule;
import org.jeecg.modules.system.entity.SysRuleItem;
import org.jeecg.modules.system.entity.vo.SysRulePage;
import org.jeecg.modules.system.service.ISysRuleItemService;
import org.jeecg.modules.system.service.ISysRuleService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 自定义编码表
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Api(tags = "自定义编码表")
@RestController
@RequestMapping("/contract/sysRule")
@Slf4j
public class SysRuleController {
    @Autowired
    private ISysRuleService sysRuleService;
    @Autowired
    private ISysRuleItemService sysRuleItemService;

    /**
     * 分页列表查询
     *
     * @param sysRule
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "自定义编码表-分页列表查询")
    @ApiOperation(value = "自定义编码表-分页列表查询", notes = "自定义编码表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "system/SysRuleList")
    public Result<?> queryPageList(SysRule sysRule,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<SysRule> queryWrapper = QueryGenerator.initQueryWrapper(sysRule, req.getParameterMap());
        Page<SysRule> page = new Page<>(pageNo, pageSize);
        IPage<SysRule> pageList = sysRuleService.pageVo(page, sysRule);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysRulePage
     * @return
     */
    @AutoLog(value = "自定义编码表-添加")
    @ApiOperation(value = "自定义编码表-添加", notes = "自定义编码表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysRulePage sysRulePage) {
        SysRule sysRule = new SysRule();
        BeanUtils.copyProperties(sysRulePage, sysRule);
        sysRuleService.saveMain(sysRule, sysRulePage.getSysRuleItemList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysRulePage
     * @return
     */
    @AutoLog(value = "自定义编码表-编辑")
    @ApiOperation(value = "自定义编码表-编辑", notes = "自定义编码表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysRulePage sysRulePage) {
        SysRule sysRule = new SysRule();
        BeanUtils.copyProperties(sysRulePage, sysRule);
        SysRule sysRuleEntity = sysRuleService.getById(sysRule.getId());
        if (sysRuleEntity == null) {
            return Result.error("未找到对应数据");
        }
        sysRuleService.updateMain(sysRule, sysRulePage.getSysRuleItemList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "自定义编码表-通过id删除")
    @ApiOperation(value = "自定义编码表-通过id删除", notes = "自定义编码表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysRuleService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "自定义编码表-批量删除")
    @ApiOperation(value = "自定义编码表-批量删除", notes = "自定义编码表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysRuleService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "自定义编码表-通过id查询")
    @ApiOperation(value = "自定义编码表-通过id查询", notes = "自定义编码表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysRule sysRule = sysRuleService.getById(id);
        if (sysRule == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysRule);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "自定义规则项-通过主表ID查询")
    @ApiOperation(value = "自定义规则项-通过主表ID查询", notes = "自定义规则项-通过主表ID查询")
    @GetMapping(value = "/querySysRuleItemByMainId")
    public Result<?> querySysRuleItemListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SysRuleItem> sysRuleItemList = sysRuleItemService.selectByMainId(id);
        IPage<SysRuleItem> page = new Page<>();
        page.setRecords(sysRuleItemList);
        page.setTotal(sysRuleItemList.size());
        return Result.OK(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysRule
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysRule sysRule) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<SysRule> queryWrapper = QueryGenerator.initQueryWrapper(sysRule, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<SysRule> queryList = sysRuleService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<SysRule> sysRuleList = new ArrayList<SysRule>();
        if (oConvertUtils.isEmpty(selections)) {
            sysRuleList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            sysRuleList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SysRulePage> pageList = new ArrayList<SysRulePage>();
        for (SysRule main : sysRuleList) {
            SysRulePage vo = new SysRulePage();
            BeanUtils.copyProperties(main, vo);
            List<SysRuleItem> sysRuleItemList = sysRuleItemService.selectByMainId(main.getId());
            vo.setSysRuleItemList(sysRuleItemList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "自定义编码表列表");
        mv.addObject(NormalExcelConstants.CLASS, SysRulePage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("自定义编码表数据", "导出人:" + sysUser.getRealname(), "自定义编码表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SysRulePage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysRulePage.class, params);
                for (SysRulePage page : list) {
                    SysRule po = new SysRule();
                    BeanUtils.copyProperties(page, po);
                    sysRuleService.saveMain(po, page.getSysRuleItemList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
