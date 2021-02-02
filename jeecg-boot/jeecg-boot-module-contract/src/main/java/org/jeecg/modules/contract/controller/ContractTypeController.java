package org.jeecg.modules.contract.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.ContractType;
import org.jeecg.modules.contract.service.IContractTypeService;
import org.jeecg.modules.contract.utils.TreeBuilder;
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
 * @Description: 合同类型
 * @Author: jeecg-boot
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Api(tags = "合同类型")
@RestController
@RequestMapping("/contract/contractType")
@Slf4j
public class ContractTypeController extends JeecgController<ContractType, IContractTypeService> {
    @Autowired
    private IContractTypeService contractTypeService;

    @Autowired
    private ISysBaseAPI iSysBaseAPI;

    /**
     * 树结构
     *
     * @return
     */
    @AutoLog(value = "合同类型-树结构")
    @ApiOperation(value = "合同类型-树结构", notes = "合同类型-树结构")
    @GetMapping(value = "/tree")
    public Result<?> tree(@ApiParam("过滤角色") @RequestParam(required = false, defaultValue = "false") Boolean roles) {
        List<ContractType> contractTypeList = contractTypeService.list();
        if (BooleanUtil.isTrue(roles)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            List<String> userRoles = iSysBaseAPI.getRolesByUsername(sysUser.getUsername());
            List<ContractType> newContractList = new ArrayList<>();
            for (ContractType contractType : contractTypeList) {
                if (StringUtils.hasText(contractType.getRoles())) {
                    String[] roleStr = contractType.getRoles().split(",");
                    for (String role : roleStr) {
                        if (userRoles.contains(role)) {
                            newContractList.add(contractType);
                            break;
                        }
                    }
                } else {
                    newContractList.add(contractType);
                }
            }
            contractTypeList = newContractList;
        }
        return Result.OK(TreeBuilder.build(contractTypeList, "0"));
    }

    /**
     * 分页列表查询
     *
     * @param contractType
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同类型-分页列表查询")
    @ApiOperation(value = "合同类型-分页列表查询", notes = "合同类型-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<?> queryPageList(ContractType contractType,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<ContractType> queryWrapper = QueryGenerator.initQueryWrapper(contractType, req.getParameterMap());
            queryWrapper.orderByAsc("sort");
            List<ContractType> list = contractTypeService.queryTreeListNoPage(queryWrapper);
            IPage<ContractType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            String parentId = contractType.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            contractType.setPid(null);
            QueryWrapper<ContractType> queryWrapper = QueryGenerator.initQueryWrapper(contractType, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId).orderByAsc("sort");
            Page<ContractType> page = new Page<ContractType>(pageNo, pageSize);
            IPage<ContractType> pageList = contractTypeService.page(page, queryWrapper);
            return Result.OK(pageList);

        }
    }

    /**
     * 获取子数据
     *
     * @param contractType
     * @param req
     * @return
     */
    @AutoLog(value = "合同类型-获取子数据")
    @ApiOperation(value = "合同类型-获取子数据", notes = "合同类型-获取子数据")
    @GetMapping(value = "/childList")
    public Result<?> queryPageList(ContractType contractType, HttpServletRequest req) {
        QueryWrapper<ContractType> queryWrapper = QueryGenerator.initQueryWrapper(contractType, req.getParameterMap());
        queryWrapper.orderByAsc("sort");
        List<ContractType> list = contractTypeService.list(queryWrapper);
        IPage<ContractType> pageList = new Page<>(1, 10, list.size());
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
    @AutoLog(value = "合同类型-批量获取子数据")
    @ApiOperation(value = "合同类型-批量获取子数据", notes = "合同类型-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<ContractType> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<ContractType> list = contractTypeService.list(queryWrapper);
            IPage<ContractType> pageList = new Page<>(1, 10, list.size());
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
     * @param contractType
     * @return
     */
    @AutoLog(value = "合同类型-添加")
    @ApiOperation(value = "合同类型-添加", notes = "合同类型-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractType contractType) {
        contractTypeService.addContractType(contractType);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractType
     * @return
     */
    @AutoLog(value = "合同类型-编辑")
    @ApiOperation(value = "合同类型-编辑", notes = "合同类型-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractType contractType) {
        contractTypeService.updateContractType(contractType);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同类型-通过id删除")
    @ApiOperation(value = "合同类型-通过id删除", notes = "合同类型-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractTypeService.deleteContractType(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同类型-批量删除")
    @ApiOperation(value = "合同类型-批量删除", notes = "合同类型-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractTypeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同类型-通过id查询")
    @ApiOperation(value = "合同类型-通过id查询", notes = "合同类型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractType contractType = contractTypeService.getById(id);
        if (contractType == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contractType);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractType
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractType contractType) {
        return super.exportXls(request, contractType, ContractType.class, "合同类型");
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
        return super.importExcel(request, response, ContractType.class);
    }

}
