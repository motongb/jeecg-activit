package org.jeecg.modules.wps.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.wps.WpsFileDTO;
import org.jeecg.common.api.dto.wps.WpsUserDTO;
import org.jeecg.common.api.vo.OaWpsModel;
import org.jeecg.common.constant.WpsConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.WpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author motb
 * @date 2021/2/25 9:09
 * @description //TODO WebOfficeService
 **/
@Slf4j
@Service
public class WebOfficeService {

    @Autowired
    private WpsUtil wpsUtil;

    @Autowired
    private OaWpsModelService oaWpsModelService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Value("${jeecg.host}")
    private String domain;

    public String getViewUrl(String fileId, String fileType, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("_w_fileid", fileId);
        params.put("_w_userid", userId);
        return wpsUtil.getWpsUrl(params, fileType, fileId);
    }

    public String getNewFileUrl(String fileType) {
        Map<String, String> params = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        params.put("_w_userid", sysUser.getUsername());
        params.put("_w_filetype", fileType);
        return wpsUtil.getNewFileUrl(params, fileType);
    }

    /*********wps回调方法 start**********/
    public Map<String, Object> userInfo(String[] ids) {
        Map<String, Object> result = new HashMap<>();
        List<WpsUserDTO> userDTOS = sysBaseAPI.queryUserByNames(ids).stream().map(item -> {
            WpsUserDTO userDTO = new WpsUserDTO();
            userDTO.setId(item.getUsername());
            userDTO.setName(item.getRealname());
            userDTO.setAvatar_url(item.getAvatar());
            return userDTO;
        }).collect(Collectors.toList());
        result.put("users", userDTOS);
        return result;
    }

    public Map<String, String> createNewFile(MultipartFile file, String filename, String userId, String fileType) {
        String filePath = wpsUtil.upload(file, "");
        Map<String, String> result = new HashMap<>();
        if (StringUtils.hasText(filePath)) {
            OaWpsModel oaWpsModel = new OaWpsModel();
            String fileId = IdUtil.simpleUUID();
            LoginUser user = sysBaseAPI.getUserByName(userId);
            oaWpsModel.setCanDelete("0");
            oaWpsModel.setDeleted("0");
            oaWpsModel.setDownloadUrl(filePath);
            oaWpsModel.setName(filename);
            oaWpsModel.setSize(file.getSize());
            oaWpsModel.setFileId(fileId);
            oaWpsModel.setCreateBy(userId);
            oaWpsModel.setSysOrgCode(user.getOrgCode());
            oaWpsModelService.save(oaWpsModel);
            result.put("redirect_url", this.getViewUrl(fileId, fileType, userId));
            result.put("user_id", userId);
        }
        return result;
    }

    public Map<String, Object> saveFile(MultipartFile file, String fileId, String userId) {
        Map<String, Object> result = new HashMap<>();
        String filePath = wpsUtil.upload(file, "");
        if (StringUtils.hasText(filePath)) {
            OaWpsModel oaWpsModel = oaWpsModelService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                    .eq(OaWpsModel::getFileId, fileId).orderByDesc(OaWpsModel::getVersion).last("limit 1"));
            OaWpsModel newOaWpsModel = new OaWpsModel();
            newOaWpsModel.setCanDelete("0");
            newOaWpsModel.setDeleted("0");
            newOaWpsModel.setDownloadUrl(filePath);
            newOaWpsModel.setName(oaWpsModel.getName());
            newOaWpsModel.setSize(file.getSize());
            newOaWpsModel.setFileId(fileId);
            newOaWpsModel.setCreateBy(userId);
            newOaWpsModel.setSysOrgCode(oaWpsModel.getSysOrgCode());
            int version = oaWpsModel.getVersion();
            newOaWpsModel.setVersion(++version);
            oaWpsModelService.save(oaWpsModel);
            result.put("file", setFileDTO(fileId, newOaWpsModel));
        }
        return result;
    }

    public Map<String, Object> fileInfo(String fileId, String userId) {
        Map<String, Object> result = new HashMap<>();
        OaWpsModel oaWpsModel = oaWpsModelService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                .eq(OaWpsModel::getFileId, fileId).orderByDesc(OaWpsModel::getVersion).last("limit 1"));
        if (Objects.nonNull(oaWpsModel)) {
            result.put("file", setFileDTO(fileId, oaWpsModel));
            WpsUserDTO userDTO = new WpsUserDTO();
            LoginUser loginUser = sysBaseAPI.getUserByName(userId);
            userDTO.setId(userId);
            userDTO.setName(loginUser.getRealname());
            userDTO.setAvatar_url(loginUser.getAvatar());
            result.put("user", userDTO);
        }
        return result;
    }

    public Map<String, Object> getByVersion(Integer version, String fileId) {
        Map<String, Object> result = new HashMap<>();
        OaWpsModel oaWpsModel = oaWpsModelService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                .eq(OaWpsModel::getFileId, fileId).eq(OaWpsModel::getVersion, version));
        if (Objects.nonNull(oaWpsModel)) {
            result.put("file", setFileDTO(fileId, oaWpsModel));
        }
        return result;
    }

    public void rename(String name, String fileId) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        OaWpsModel oaWpsModel = oaWpsModelService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                .eq(OaWpsModel::getFileId, fileId).orderByDesc(OaWpsModel::getVersion).last("limit 1"));
        oaWpsModel.setName(name);
        oaWpsModelService.updateById(oaWpsModel);
    }

    public Map<String, Object> history(int pageNum, int pageSize, String fileId) {
        Map<String, Object> result = new HashMap<>();
        IPage<OaWpsModel> page = new Page<>(pageNum, pageSize);
        oaWpsModelService.page(page, new LambdaQueryWrapper<OaWpsModel>().eq(OaWpsModel::getFileId, fileId));
        List<OaWpsModel> oaWpsModelList = page.getRecords();
        if (!CollectionUtils.isEmpty(oaWpsModelList)) {
            List<Map<String, Object>> fileMaps = oaWpsModelList.stream().map(oaWpsModel -> {
                Map<String, Object> fileMap = new HashMap<>();
                fileMap.put("id", fileId);
                fileMap.put("name", oaWpsModel.getName());
                fileMap.put("version", oaWpsModel.getVersion());
                fileMap.put("size", oaWpsModel.getSize());
                fileMap.put("download_url", this.domain + WpsConstant.WPS_FILE_DOWN_PRE + oaWpsModel.getDownloadUrl());
                String createTime = String.valueOf(oaWpsModel.getCreateTime().getTime());
                String updateTime = String.valueOf(oaWpsModel.getUpdateTime().getTime());
                if (StringUtils.hasText(createTime)) {
                    fileMap.put("create_time", Long.valueOf(createTime.substring(0, createTime.length() - 3)));
                }
                if (StringUtils.hasText(updateTime)) {
                    fileMap.put("modify_time", Long.valueOf(updateTime.substring(0, updateTime.length() - 3)));
                }
                LoginUser creator = sysBaseAPI.getUserByName(oaWpsModel.getCreateBy());
                if (Objects.nonNull(creator)) {
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("id", creator.getUsername());
                    userMap.put("name", creator.getRealname());
                    userMap.put("avatar_url", creator.getAvatar());
                    fileMap.put("creator", userMap);
                }
                LoginUser modifier = sysBaseAPI.getUserByName(oaWpsModel.getUpdateBy());
                if (Objects.nonNull(modifier)) {
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("id", modifier.getUsername());
                    userMap.put("name", modifier.getRealname());
                    userMap.put("avatar_url", modifier.getAvatar());
                    fileMap.put("modifier", userMap);
                }
                return fileMap;
            }).collect(Collectors.toList());
            result.put("histories", fileMaps);
        }
        return result;
    }

    private WpsFileDTO setFileDTO(String fileId, OaWpsModel oaWpsModel) {
        WpsFileDTO fileDTO = new WpsFileDTO();
        fileDTO.setId(fileId);
        fileDTO.setName(oaWpsModel.getName());
        fileDTO.setSize(oaWpsModel.getSize());
        fileDTO.setVersion(oaWpsModel.getVersion());
        fileDTO.setDownload_url(this.domain + WpsConstant.WPS_FILE_DOWN_PRE + oaWpsModel.getDownloadUrl());
        fileDTO.setCreator(oaWpsModel.getCreateBy());
        String createTime = String.valueOf(oaWpsModel.getCreateTime().getTime());
        if (StringUtils.hasText(createTime)) {
            fileDTO.setCreate_time(Long.valueOf(createTime.substring(0, createTime.length() - 3)));
        }
        fileDTO.setModifier(oaWpsModel.getUpdateBy());
        String updateTime = String.valueOf(oaWpsModel.getUpdateTime().getTime());
        if (StringUtils.hasText(updateTime)) {
            fileDTO.setModify_time(Long.valueOf(updateTime.substring(0, updateTime.length() - 3)));
        }
        return fileDTO;
    }
}
