package org.jeecg.modules.contract.controller;

import cn.hutool.json.JSONUtil;
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
import org.jeecg.modules.activiti.service.IActZParamsService;
import org.jeecg.modules.contract.entity.ContractGeneral;
import org.jeecg.modules.contract.entity.ContractPurchase;
import org.jeecg.modules.contract.entity.vo.ContractGeneralVo;
import org.jeecg.modules.contract.service.IContractGeneralService;
import org.jeecg.modules.contract.service.IContractPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 一般采购合同
 * @Author: jeecg-boot
 * @Date: 2021-02-03
 * @Version: V1.0
 */
@Api(tags = "一般采购合同")
@RestController
@RequestMapping("/contract/contractGeneral")
@Slf4j
public class ContractGeneralController extends JeecgController<ContractGeneral, IContractGeneralService> {
    @Autowired
    private IContractGeneralService contractGeneralService;

    @Autowired
    private IContractPurchaseService contractPurchaseService;

    @Autowired
    private IActZParamsService actZParamsService;

    /**
     * 分页列表查询
     *
     * @param contractGeneral
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "一般采购合同-分页列表查询")
    @ApiOperation(value = "一般采购合同-分页列表查询", notes = "一般采购合同-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContractGeneral contractGeneral,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ContractGeneral> queryWrapper = QueryGenerator.initQueryWrapper(contractGeneral, req.getParameterMap());
        Page<ContractGeneral> page = new Page<ContractGeneral>(pageNo, pageSize);
        IPage<ContractGeneral> pageList = contractGeneralService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractGeneralVo
     * @return
     */
    @AutoLog(value = "一般采购合同-添加")
    @ApiOperation(value = "一般采购合同-添加", notes = "一般采购合同-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractGeneralVo contractGeneralVo) {
        contractPurchaseService.saveWithProcess(contractGeneralVo);
        ContractGeneral contractGeneral = contractGeneralVo.getSubForm();
        contractGeneral.setBaseId(contractGeneralVo.getId());
        contractGeneralService.save(contractGeneral);
        contractGeneralService.saveMoreItem(contractGeneralVo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractGeneralVo
     * @return
     */
    @AutoLog(value = "一般采购合同-编辑")
    @ApiOperation(value = "一般采购合同-编辑", notes = "一般采购合同-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractGeneralVo contractGeneralVo) {
        contractPurchaseService.saveWithProcess(contractGeneralVo);
        ContractGeneral contractGeneral = contractGeneralVo.getSubForm();
        contractGeneralService.updateById(contractGeneral);
        contractGeneralService.saveMoreItem(contractGeneralVo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "一般采购合同-通过id删除")
    @ApiOperation(value = "一般采购合同-通过id删除", notes = "一般采购合同-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractGeneralService.removeById(id);
        contractGeneralService.removeMoreItem(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "一般采购合同-批量删除")
    @ApiOperation(value = "一般采购合同-批量删除", notes = "一般采购合同-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractGeneralService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "一般采购合同-通过id查询")
    @ApiOperation(value = "一般采购合同-通过id查询", notes = "一般采购合同-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractPurchase contractPurchase = contractPurchaseService.getById(id);
        ContractGeneral contractGeneral = contractGeneralService.getOne(new LambdaQueryWrapper<ContractGeneral>().eq(ContractGeneral::getBaseId, id));
        if (contractPurchase == null) {
            return Result.error("未找到对应数据");
        }
        ContractGeneralVo contractGeneralVo = JSONUtil.toBean(JSONUtil.toJsonStr(contractPurchase), ContractGeneralVo.class);
        contractGeneralVo.setSubForm(contractGeneral);
        contractGeneralVo.setParams(actZParamsService.getActParams(id));
        contractPurchaseService.setMember(contractGeneralVo);
        return Result.OK(contractGeneralVo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractGeneral
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractGeneral contractGeneral) {
        return super.exportXls(request, contractGeneral, ContractGeneral.class, "一般采购合同");
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
        return super.importExcel(request, response, ContractGeneral.class);
    }

}
