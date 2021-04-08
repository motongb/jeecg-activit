package org.jeecg.modules.contract.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.contract.entity.ContractStamp;
import org.jeecg.modules.contract.service.IContractSealService;
import org.jeecg.modules.contract.service.IContractStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 合同用印记录表
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
@Api(tags = "合同用印记录表")
@RestController
@RequestMapping("/contract/contractStamp")
@Slf4j
public class ContractStampController extends JeecgController<ContractStamp, IContractStampService> {
    @Autowired
    private IContractStampService contractStampService;

    /**
     * 分页列表查询
     *
     * @param contractStamp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同用印记录表-分页列表查询")
    @ApiOperation(value = "合同用印记录表-分页列表查询", notes = "合同用印记录表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "contract/manage/ContractStampList")
    public Result<?> queryPageList(ContractStamp contractStamp,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<ContractStamp> queryWrapper = QueryGenerator.initQueryWrapper(contractStamp, req.getParameterMap());
        Page<ContractStamp> page = new Page<>(pageNo, pageSize);
        IPage<ContractStamp> pageList = contractStampService.pageVo(page, contractStamp);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractStamp
     * @return
     */
    @AutoLog(value = "合同用印记录表-添加")
    @ApiOperation(value = "合同用印记录表-添加", notes = "合同用印记录表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractStamp contractStamp) {
        contractStampService.saveWithProcess(contractStamp);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractStamp
     * @return
     */
    @AutoLog(value = "合同用印记录表-编辑")
    @ApiOperation(value = "合同用印记录表-编辑", notes = "合同用印记录表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractStamp contractStamp) {
        contractStampService.saveWithProcess(contractStamp);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同用印记录表-通过id删除")
    @ApiOperation(value = "合同用印记录表-通过id删除", notes = "合同用印记录表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractStampService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同用印记录表-批量删除")
    @ApiOperation(value = "合同用印记录表-批量删除", notes = "合同用印记录表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractStampService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同用印记录表-通过id查询")
    @ApiOperation(value = "合同用印记录表-通过id查询", notes = "合同用印记录表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(contractStampService.getVoById(id));
    }

    /**
     * 通过tableId,tableName查询合同
     *
     * @param tableId
     * @param tableName
     * @return
     */
    @AutoLog(value = "合同用印记录表-通过tableId,tableName查询合同")
    @ApiOperation(value = "合同用印记录表-通过tableId,tableName查询合同", notes = "合同用印记录表-通过tableId,tableName查询合同")
    @GetMapping(value = "/getContractData")
    public Result<?> getContractData(@RequestParam String tableId, @RequestParam String tableName) {
        return Result.OK(contractStampService.getContract(tableId, tableName));
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractStamp
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractStamp contractStamp) {
        return super.exportXls(request, contractStamp, ContractStamp.class, "合同用印记录表");
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
        return super.importExcel(request, response, ContractStamp.class);
    }

}
