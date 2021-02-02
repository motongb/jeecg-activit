package org.jeecg.modules.contract.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.ContractBase;
import org.jeecg.modules.contract.service.IContractBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 采购合同基础表
 * @Author: jeecg-boot
 * @Date:   2021-02-02
 * @Version: V1.0
 */
@Api(tags="采购合同基础表")
@RestController
@RequestMapping("/contract/contractBase")
@Slf4j
public class ContractBaseController extends JeecgController<ContractBase, IContractBaseService> {

	@Autowired
	private IContractBaseService contractBaseService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param contractBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购合同基础表-分页列表查询")
	@ApiOperation(value="采购合同基础表-分页列表查询", notes="采购合同基础表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ContractBase contractBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ContractBase> queryWrapper = QueryGenerator.initQueryWrapper(contractBase, req.getParameterMap());
		Page<ContractBase> page = new Page<ContractBase>(pageNo, pageSize);
		IPage<ContractBase> pageList = contractBaseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param contractBase
     * @return
     */
    @AutoLog(value = "采购合同基础表-添加")
    @ApiOperation(value="采购合同基础表-添加", notes="采购合同基础表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractBase contractBase) {
        contractBaseService.save(contractBase);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param contractBase
     * @return
     */
    @AutoLog(value = "采购合同基础表-编辑")
    @ApiOperation(value="采购合同基础表-编辑", notes="采购合同基础表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractBase contractBase) {
        contractBaseService.updateById(contractBase);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "采购合同基础表-通过id删除")
    @ApiOperation(value="采购合同基础表-通过id删除", notes="采购合同基础表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        contractBaseService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "采购合同基础表-批量删除")
    @ApiOperation(value="采购合同基础表-批量删除", notes="采购合同基础表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.contractBaseService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractBase contractBase) {
        return super.exportXls(request, contractBase, ContractBase.class, "采购合同基础表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ContractBase.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	




}
