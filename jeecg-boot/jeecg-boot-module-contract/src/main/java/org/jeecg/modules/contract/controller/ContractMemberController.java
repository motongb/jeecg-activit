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
import org.jeecg.modules.contract.entity.ContractCovertMember;
import org.jeecg.modules.contract.service.IContractMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 合同签订对象
 * @Author: jeecg-boot
 * @Date: 2021-02-07
 * @Version: V1.0
 */
@Api(tags = "合同签订对象")
@RestController
@RequestMapping("/contract/contractMember")
@Slf4j
public class ContractMemberController extends JeecgController<ContractCovertMember, IContractMemberService> {
    @Autowired
    private IContractMemberService contractMemberService;

    /**
     * 分页列表查询
     *
     * @param contractMember
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "合同签订对象-分页列表查询")
    @ApiOperation(value = "合同签订对象-分页列表查询", notes = "合同签订对象-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContractCovertMember contractMember,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ContractCovertMember> queryWrapper = QueryGenerator.initQueryWrapper(contractMember, req.getParameterMap());
        Page<ContractCovertMember> page = new Page<ContractCovertMember>(pageNo, pageSize);
        IPage<ContractCovertMember> pageList = contractMemberService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractMember
     * @return
     */
    @AutoLog(value = "合同签订对象-添加")
    @ApiOperation(value = "合同签订对象-添加", notes = "合同签订对象-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContractCovertMember contractMember) {
        contractMemberService.save(contractMember);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractMember
     * @return
     */
    @AutoLog(value = "合同签订对象-编辑")
    @ApiOperation(value = "合同签订对象-编辑", notes = "合同签订对象-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ContractCovertMember contractMember) {
        contractMemberService.updateById(contractMember);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同签订对象-通过id删除")
    @ApiOperation(value = "合同签订对象-通过id删除", notes = "合同签订对象-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        contractMemberService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同签订对象-批量删除")
    @ApiOperation(value = "合同签订对象-批量删除", notes = "合同签订对象-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractMemberService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同签订对象-通过id查询")
    @ApiOperation(value = "合同签订对象-通过id查询", notes = "合同签订对象-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractCovertMember contractMember = contractMemberService.getById(id);
        if (contractMember == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contractMember);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractMember
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractCovertMember contractMember) {
        return super.exportXls(request, contractMember, ContractCovertMember.class, "合同签订对象");
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
        return super.importExcel(request, response, ContractCovertMember.class);
    }

}
