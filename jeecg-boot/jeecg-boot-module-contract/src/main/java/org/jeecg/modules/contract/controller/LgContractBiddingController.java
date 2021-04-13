package org.jeecg.modules.contract.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.LgContractBidding;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;
import org.jeecg.modules.contract.service.ILgContractBiddingItemService;
import org.jeecg.modules.contract.service.ILgContractBiddingService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 柳钢中标信息
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Api(tags = "柳钢中标信息")
@RestController
@RequestMapping("/contract/lgContractBidding")
@Slf4j
public class LgContractBiddingController extends JeecgController<LgContractBidding, ILgContractBiddingService> {

    @Autowired
    private ILgContractBiddingService lgContractBiddingService;

    @Autowired
    private ILgContractBiddingItemService lgContractBiddingItemService;


    /*---------------------------------主表处理-begin-------------------------------------*/

    /**
     * 分页列表查询
     *
     * @param lgContractBidding
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "柳钢中标信息-分页列表查询")
    @ApiOperation(value = "柳钢中标信息-分页列表查询", notes = "柳钢中标信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LgContractBidding lgContractBidding,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<LgContractBidding> queryWrapper = QueryGenerator.initQueryWrapper(lgContractBidding, req.getParameterMap());
        Page<LgContractBidding> page = new Page<LgContractBidding>(pageNo, pageSize);
        IPage<LgContractBidding> pageList = lgContractBiddingService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param lgContractBidding
     * @return
     */
    @AutoLog(value = "柳钢中标信息-添加")
    @ApiOperation(value = "柳钢中标信息-添加", notes = "柳钢中标信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LgContractBidding lgContractBidding) {
        lgContractBiddingService.save(lgContractBidding);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param lgContractBidding
     * @return
     */
    @AutoLog(value = "柳钢中标信息-编辑")
    @ApiOperation(value = "柳钢中标信息-编辑", notes = "柳钢中标信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LgContractBidding lgContractBidding) {
        lgContractBiddingService.updateById(lgContractBidding);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "柳钢中标信息-通过id删除")
    @ApiOperation(value = "柳钢中标信息-通过id删除", notes = "柳钢中标信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        lgContractBiddingService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "柳钢中标信息-批量删除")
    @ApiOperation(value = "柳钢中标信息-批量删除", notes = "柳钢中标信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.lgContractBiddingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LgContractBidding lgContractBidding) {
        return super.exportXls(request, lgContractBidding, LgContractBidding.class, "柳钢中标信息");
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LgContractBidding.class);
    }
    /*---------------------------------主表处理-end-------------------------------------*/


    /*--------------------------------子表处理-柳钢合同中标项-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "柳钢合同中标项-通过主表ID查询")
    @ApiOperation(value = "柳钢合同中标项-通过主表ID查询", notes = "柳钢合同中标项-通过主表ID查询")
    @GetMapping(value = "/listLgContractBiddingItemByMainId")
    public Result<?> listLgContractBiddingItemByMainId(LgContractBiddingItem lgContractBiddingItem,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        QueryWrapper<LgContractBiddingItem> queryWrapper = QueryGenerator.initQueryWrapper(lgContractBiddingItem, req.getParameterMap());
        Page<LgContractBiddingItem> page = new Page<LgContractBiddingItem>(pageNo, pageSize);
        IPage<LgContractBiddingItem> pageList = lgContractBiddingItemService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param lgContractBiddingItem
     * @return
     */
    @AutoLog(value = "柳钢合同中标项-添加")
    @ApiOperation(value = "柳钢合同中标项-添加", notes = "柳钢合同中标项-添加")
    @PostMapping(value = "/addLgContractBiddingItem")
    public Result<?> addLgContractBiddingItem(@RequestBody LgContractBiddingItem lgContractBiddingItem) {
        lgContractBiddingItemService.save(lgContractBiddingItem);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param lgContractBiddingItem
     * @return
     */
    @AutoLog(value = "柳钢合同中标项-编辑")
    @ApiOperation(value = "柳钢合同中标项-编辑", notes = "柳钢合同中标项-编辑")
    @PutMapping(value = "/editLgContractBiddingItem")
    public Result<?> editLgContractBiddingItem(@RequestBody LgContractBiddingItem lgContractBiddingItem) {
        lgContractBiddingItemService.updateById(lgContractBiddingItem);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "柳钢合同中标项-通过id删除")
    @ApiOperation(value = "柳钢合同中标项-通过id删除", notes = "柳钢合同中标项-通过id删除")
    @DeleteMapping(value = "/deleteLgContractBiddingItem")
    public Result<?> deleteLgContractBiddingItem(@RequestParam(name = "id", required = true) String id) {
        lgContractBiddingItemService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "柳钢合同中标项-批量删除")
    @ApiOperation(value = "柳钢合同中标项-批量删除", notes = "柳钢合同中标项-批量删除")
    @DeleteMapping(value = "/deleteBatchLgContractBiddingItem")
    public Result<?> deleteBatchLgContractBiddingItem(@RequestParam(name = "ids", required = true) String ids) {
        this.lgContractBiddingItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportLgContractBiddingItem")
    public ModelAndView exportLgContractBiddingItem(HttpServletRequest request, LgContractBiddingItem lgContractBiddingItem) {
        // Step.1 组装查询条件
        QueryWrapper<LgContractBiddingItem> queryWrapper = QueryGenerator.initQueryWrapper(lgContractBiddingItem, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<LgContractBiddingItem> pageList = lgContractBiddingItemService.list(queryWrapper);
        List<LgContractBiddingItem> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "柳钢合同中标项"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, LgContractBiddingItem.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("柳钢合同中标项报表", "导出人:" + sysUser.getRealname(), "柳钢合同中标项"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importLgContractBiddingItem/{mainId}")
    public Result<?> importLgContractBiddingItem(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<LgContractBiddingItem> list = ExcelImportUtil.importExcel(file.getInputStream(), LgContractBiddingItem.class, params);
                for (LgContractBiddingItem temp : list) {
                    temp.setBiddingId(mainId);
                }
                long start = System.currentTimeMillis();
                lgContractBiddingItemService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-柳钢合同中标项-end----------------------------------------------*/


}
