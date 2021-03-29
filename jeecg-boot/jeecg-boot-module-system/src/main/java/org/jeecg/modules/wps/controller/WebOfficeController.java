package org.jeecg.modules.wps.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.OaWpsModel;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.wps.service.WebOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author motb
 * @date 2021/2/25 9:04
 * @description //TODO WebOfficeController
 **/
@Api(tags = "在线office文件管理")
@RestController
@RequestMapping("/base/webOffice")
@Slf4j
public class WebOfficeController {

    @Autowired
    private WebOfficeService webOfficeService;

    @ApiOperation("获取预览地址")
    @GetMapping("/getViewUrl")
    public Result<Object> getViewUrl(@RequestParam @ApiParam("文档id") String fileId,
                                     @RequestParam @ApiParam("文档类型：word,excel,ppt,pdf") String fileType,
                                     @RequestParam @ApiParam("操作权限：write-写,read-读") String permission) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.OK(webOfficeService.getViewUrl(fileId, fileType, sysUser.getUsername(), true, permission));
    }

    @ApiOperation("创建新文档")
    @GetMapping("/getNewFileUrl")
    public Result<Object> getNewFileUrl(@RequestParam @ApiParam("文档类型：word,excel,ppt") String fileType) {
        return Result.OK(webOfficeService.getNewFileUrl(fileType));
    }

    @ApiOperation("保存文档")
    @PostMapping("/saveWpsModel")
    public Result<Object> saveWpsModel(@RequestBody OaWpsModel oaWpsModel) {
        return Result.OK(webOfficeService.saveWpsModel(oaWpsModel));
    }

    @ApiOperation("从模板文件复制")
    @GetMapping("/copyByModelFile")
    public Result<Object> copyByModelFile(@RequestParam @ApiParam("模板文件id") String fileId) {
        return Result.OK(webOfficeService.copyByModelFile(fileId));
    }
}
