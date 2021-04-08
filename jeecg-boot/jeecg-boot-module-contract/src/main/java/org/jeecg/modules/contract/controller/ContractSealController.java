package org.jeecg.modules.contract.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.contract.entity.ContractSeal;
import org.jeecg.modules.contract.service.IContractSealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 合同印章
 * @Author: jeecg-boot
 * @Date: 2021-03-31
 * @Version: V1.0
 */
@Api(tags = "合同印章")
@RestController
@RequestMapping("/contract/contractSeal")
@Slf4j
public class ContractSealController extends JeecgController<ContractSeal, IContractSealService> {
    @Autowired
    private IContractSealService contractSealService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param contractSeal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同印章-分页列表查询")
    @ApiOperation(value = "合同印章-分页列表查询", notes = "合同印章-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContractSeal contractSeal,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ContractSeal> queryWrapper = QueryGenerator.initQueryWrapper(contractSeal, req.getParameterMap());
        Page<ContractSeal> page = new Page<>(pageNo, pageSize);
        IPage<ContractSeal> pageList = contractSealService.page(page, queryWrapper);
        List<ContractSeal> contractSeals = pageList.getRecords();
        contractSeals.forEach(item -> {
            if (StringUtils.hasText(item.getUsers())) {
                String userNames = sysBaseAPI.queryUserByNames(item.getUsers().split(","))
                        .stream()
                        .map(LoginUser::getRealname)
                        .collect(Collectors.joining(","));
                item.setUserNames(userNames);
            }
        });
        pageList.setRecords(contractSeals);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractSeal
     * @return
     */
    @AutoLog(value = "合同印章-添加")
    @ApiOperation(value = "合同印章-添加", notes = "合同印章-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractSeal contractSeal) {
        contractSeal.setSealCode(String.valueOf(IdWorker.getId()));
        contractSealService.save(contractSeal);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractSeal
     * @return
     */
    @AutoLog(value = "合同印章-编辑")
    @ApiOperation(value = "合同印章-编辑", notes = "合同印章-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractSeal contractSeal) {
        contractSealService.updateById(contractSeal);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同印章-通过id删除")
    @ApiOperation(value = "合同印章-通过id删除", notes = "合同印章-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractSealService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同印章-批量删除")
    @ApiOperation(value = "合同印章-批量删除", notes = "合同印章-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractSealService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同印章-通过id查询")
    @ApiOperation(value = "合同印章-通过id查询", notes = "合同印章-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractSeal contractSeal = contractSealService.getById(id);
        if (contractSeal == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contractSeal);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractSeal
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractSeal contractSeal) {
        return super.exportXls(request, contractSeal, ContractSeal.class, "合同印章");
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
        return super.importExcel(request, response, ContractSeal.class);
    }

}
