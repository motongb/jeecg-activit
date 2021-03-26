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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.ContractFieldParams;
import org.jeecg.modules.contract.service.IContractFieldParamsService;
import org.jeecg.common.util.TreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 合同字段参数
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
@Api(tags = "合同字段参数")
@RestController
@RequestMapping("/contract/contractFieldParams")
@Slf4j
public class ContractFieldParamsController extends JeecgController<ContractFieldParams, IContractFieldParamsService> {
    @Autowired
    private IContractFieldParamsService contractFieldParamsService;

    /**
     * 树结构
     *
     * @return
     */
    @AutoLog(value = "合同字段参数-树结构")
    @ApiOperation(value = "合同字段参数-树结构", notes = "合同字段参数-树结构")
    @GetMapping(value = "/tree")
    public Result<?> tree() {
        List<ContractFieldParams> contractFieldParamsList = contractFieldParamsService.list();
        return Result.OK(TreeBuilder.build(contractFieldParamsList, "0"));
    }

    /**
     * 分页列表查询
     *
     * @param contractFieldParams
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同字段参数-分页列表查询")
    @ApiOperation(value = "合同字段参数-分页列表查询", notes = "合同字段参数-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<?> queryPageList(ContractFieldParams contractFieldParams,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<ContractFieldParams> queryWrapper = QueryGenerator.initQueryWrapper(contractFieldParams, req.getParameterMap());
            List<ContractFieldParams> list = contractFieldParamsService.queryTreeListNoPage(queryWrapper);
            IPage<ContractFieldParams> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            String parentId = contractFieldParams.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            contractFieldParams.setPid(null);
            QueryWrapper<ContractFieldParams> queryWrapper = QueryGenerator.initQueryWrapper(contractFieldParams, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<ContractFieldParams> page = new Page<ContractFieldParams>(pageNo, pageSize);
            IPage<ContractFieldParams> pageList = contractFieldParamsService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * 获取子数据
     *
     * @param contractFieldParams
     * @param req
     * @return
     */
    @AutoLog(value = "合同字段参数-获取子数据")
    @ApiOperation(value = "合同字段参数-获取子数据", notes = "合同字段参数-获取子数据")
    @GetMapping(value = "/childList")
    public Result<?> queryPageList(ContractFieldParams contractFieldParams, HttpServletRequest req) {
        QueryWrapper<ContractFieldParams> queryWrapper = QueryGenerator.initQueryWrapper(contractFieldParams, req.getParameterMap());
        List<ContractFieldParams> list = contractFieldParamsService.list(queryWrapper);
        IPage<ContractFieldParams> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return
     */
    @AutoLog(value = "合同字段参数-批量获取子数据")
    @ApiOperation(value = "合同字段参数-批量获取子数据", notes = "合同字段参数-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<ContractFieldParams> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<ContractFieldParams> list = contractFieldParamsService.list(queryWrapper);
            IPage<ContractFieldParams> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param contractFieldParams
     * @return
     */
    @AutoLog(value = "合同字段参数-添加")
    @ApiOperation(value = "合同字段参数-添加", notes = "合同字段参数-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractFieldParams contractFieldParams) {
        contractFieldParamsService.addContractFieldParams(contractFieldParams);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractFieldParams
     * @return
     */
    @AutoLog(value = "合同字段参数-编辑")
    @ApiOperation(value = "合同字段参数-编辑", notes = "合同字段参数-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractFieldParams contractFieldParams) {
        contractFieldParamsService.updateContractFieldParams(contractFieldParams);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同字段参数-通过id删除")
    @ApiOperation(value = "合同字段参数-通过id删除", notes = "合同字段参数-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractFieldParamsService.deleteContractFieldParams(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同字段参数-批量删除")
    @ApiOperation(value = "合同字段参数-批量删除", notes = "合同字段参数-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractFieldParamsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同字段参数-通过id查询")
    @ApiOperation(value = "合同字段参数-通过id查询", notes = "合同字段参数-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractFieldParams contractFieldParams = contractFieldParamsService.getById(id);
        if (contractFieldParams == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contractFieldParams);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractFieldParams
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractFieldParams contractFieldParams) {
        return super.exportXls(request, contractFieldParams, ContractFieldParams.class, "合同字段参数");
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
        return super.importExcel(request, response, ContractFieldParams.class);
    }

}
