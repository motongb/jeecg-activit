package org.jeecg.modules.wps.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.wps.service.WebOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author motb
 * @date 2021/2/25 11:38
 * @description //TODO WpsCallbackController
 **/
@Slf4j
@RestController
@RequestMapping("/base/wps")
public class WpsCallbackController {

    @Autowired
    private WebOfficeService webOfficeService;

    @PostMapping("/v1/3rd/file/new")
    public Map<String, String> newFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam("name") String filename,
                                       @RequestParam String _w_userid,
                                       @RequestParam String _w_filetype) {
        log.info("新建文档：{}", filename);
        return webOfficeService.createNewFile(file, filename, _w_userid, _w_filetype);
    }

    @PostMapping("/v1/3rd/file/save")
    public Map<String, Object> saveFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam String _w_fileid,
                                        @RequestParam String _w_userid) {
        log.info("保存文档：{}", file.getOriginalFilename());
        return webOfficeService.saveFile(file, _w_fileid, _w_userid);
    }

    @GetMapping("/v1/3rd/file/info")
    public Map<String, Object> fileInfo(@RequestParam String _w_fileid,
                                        @RequestParam String _w_userid) {
        log.info("获取文件信息：_w_fileid={},_w_userid={}", _w_fileid, _w_userid);
        return webOfficeService.fileInfo(_w_fileid, _w_userid);
    }

    @PostMapping("/v1/3rd/user/info")
    public Map<String, Object> userInfo(@RequestBody Map<String, String[]> infoData) {
        log.info("获取用户信息：user_ids={}", infoData.get("ids"));
        return webOfficeService.userInfo(infoData.get("ids"));
    }

    @PostMapping("/v1/3rd/onnotify")
    public Result<Object> onNotify(@RequestBody Map<String, Object> infoData) {
        log.info("打开文档通知：{}", infoData);
        return Result.OK();
    }

    @PostMapping("/v1/3rd/file/online")
    public Result<Object> onlineUser(@RequestBody Map<String, String[]> infoData) {
        log.info("当前在线编辑用户：{}", infoData.get("ids"));
        return Result.OK();
    }

    @GetMapping("/v1/3rd/file/version/{version}")
    public Map<String, Object> getByVersion(@PathVariable Integer version,
                                            @RequestParam String _w_fileid) {
        log.info("获取版本{}的文档", version);
        return webOfficeService.getByVersion(version, _w_fileid);
    }

    @PutMapping("/v1/3rd/file/rename")
    public Result<Object> rename(@RequestBody Map<String, String> infoData,
                                 @RequestParam String _w_fileid) {
        log.info("重命名文档_w_fileid={},新文档名：{}", _w_fileid, infoData.get("name"));
        webOfficeService.rename(infoData.get("name"), _w_fileid);
        return Result.OK();
    }

    @PostMapping("/v1/3rd/file/history")
    public Map<String, Object> history(@RequestBody Map<String, Object> infoData,
                                       @RequestParam String _w_fileid) {
        int offset = (int) infoData.get("offset");
        int count = (int) infoData.get("count");
        log.info("获取文件_w_fileid={}，历史数据", _w_fileid);
        return webOfficeService.history(offset + 1, count, _w_fileid);
    }
}
