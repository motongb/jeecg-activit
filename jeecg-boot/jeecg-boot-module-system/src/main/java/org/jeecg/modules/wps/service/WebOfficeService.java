package org.jeecg.modules.wps.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.wps.WpsFileDTO;
import org.jeecg.common.api.dto.wps.WpsFileHisDTO;
import org.jeecg.common.api.dto.wps.WpsUserDTO;
import org.jeecg.common.api.vo.OaWpsModel;
import org.jeecg.common.constant.WpsConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.api.WebOfficeAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.WpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author motb
 * @date 2021/2/25 9:09
 * @description //TODO WebOfficeService
 **/
@Slf4j
@Service
public class WebOfficeService implements WebOfficeAPI {

    @Autowired
    private WpsUtil wpsUtil;

    @Autowired
    private OaWpsModelService oaWpsModelService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Value("${jeecg.host}")
    private String domain;

    public String getViewUrl(String fileId, String fileType, String userId, boolean checkToken, String permission) {
        Map<String, String> params = new HashMap<>();
        params.put("_w_fileid", fileId);
        params.put("_w_userid", userId);
        params.put("_w_permission", permission);
        return wpsUtil.getWpsUrl(params, fileType, fileId, checkToken);
    }

    public String getNewFileUrl(String fileType) {
        Map<String, String> params = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        params.put("_w_userid", sysUser.getUsername());
        params.put("_w_filetype", fileType);
        return wpsUtil.getNewFileUrl(params, fileType);
    }


    public Map<String, Object> saveWpsModel(OaWpsModel oaWpsModel) {
        Map<String, Object> result = new HashMap<>();
        String fileId = IdUtil.simpleUUID();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        oaWpsModel.setFileId(fileId);
        oaWpsModel.setCanDelete("0");
        oaWpsModel.setDeleted("0");
        oaWpsModel.setUpdateBy(sysUser.getUsername());
        oaWpsModel.setUpdateTime(new Date());
        oaWpsModelService.save(oaWpsModel);
        result.put("model", oaWpsModel);
        result.put("preViewUrl", this.getViewUrl(fileId, oaWpsModel.getFileType(), oaWpsModel.getCreateBy(), true, "write"));
        return result;
    }

    @Override
    public OaWpsModel copyByModelFile(String fileId) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        OaWpsModel oaWpsModel = oaWpsModelService.getOne(new LambdaQueryWrapper<OaWpsModel>()
                .eq(OaWpsModel::getFileId, fileId).orderByDesc(OaWpsModel::getVersion).last("limit 1"));
        if (Objects.isNull(oaWpsModel)) {
            throw new RuntimeException("文件丢失");
        }
        String newFileId = IdUtil.simpleUUID();
        OaWpsModel newOaWpsModel = new OaWpsModel();
        BeanUtil.copyProperties(oaWpsModel, newOaWpsModel);
        newOaWpsModel.setFileId(newFileId);
        newOaWpsModel.setUpdateBy(sysUser.getUsername());
        newOaWpsModel.setUpdateTime(new Date());
        newOaWpsModel.setId(null);
        newOaWpsModel.setDownloadUrl(wpsUtil.copyFile(oaWpsModel.getDownloadUrl(), ""));
        oaWpsModelService.save(newOaWpsModel);
        return newOaWpsModel;
    }

    @Override
    public IService<OaWpsModel> getOaWpsModelService() {
        return this.oaWpsModelService;
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

    public Map<String, Object> createNewFile(MultipartFile file, String filename, String userId, String fileType) {
        String filePath = wpsUtil.upload(file, "");
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.hasText(filePath)) {
            OaWpsModel oaWpsModel = new OaWpsModel();
            String fileId = IdUtil.simpleUUID();
            LoginUser user = sysBaseAPI.getUserByName(userId);
            oaWpsModel.setFileType(fileType);
            oaWpsModel.setCanDelete("0");
            oaWpsModel.setDeleted("0");
            oaWpsModel.setDownloadUrl(filePath);
            oaWpsModel.setName(filename);
            oaWpsModel.setSize(file.getSize());
            oaWpsModel.setFileId(fileId);
            oaWpsModel.setCreateBy(userId);
            oaWpsModel.setSysOrgCode(user.getOrgCode());
            oaWpsModel.setUpdateBy(userId);
            oaWpsModel.setUpdateTime(new Date());
            oaWpsModelService.save(oaWpsModel);
            result.put("redirect_url", this.getViewUrl(fileId, fileType, userId, false, "write"));
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
            newOaWpsModel.setFileType(oaWpsModel.getFileType());
            newOaWpsModel.setCanDelete("0");
            newOaWpsModel.setDeleted("0");
            newOaWpsModel.setDownloadUrl(filePath);
            newOaWpsModel.setName(oaWpsModel.getName());
            newOaWpsModel.setSize(file.getSize());
            newOaWpsModel.setFileId(fileId);
            newOaWpsModel.setCreateBy(oaWpsModel.getCreateBy());
            newOaWpsModel.setCreateTime(oaWpsModel.getCreateTime());
            newOaWpsModel.setSysOrgCode(oaWpsModel.getSysOrgCode());
            newOaWpsModel.setUpdateBy(userId);
            newOaWpsModel.setUpdateTime(new Date());
            int version = oaWpsModel.getVersion();
            newOaWpsModel.setVersion(++version);
            oaWpsModelService.save(newOaWpsModel);
            result.put("file", setFileDTO(fileId, newOaWpsModel));
        }
        return result;
    }

    public Map<String, Object> fileInfo(String fileId, String userId, String permission) {
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
            if (StringUtils.hasText(permission)) {
                userDTO.setPermission(permission);
            }
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
            List<WpsFileHisDTO> fileHisDTOS = oaWpsModelList.stream().map(oaWpsModel -> {
                WpsFileHisDTO fileHisDTO = new WpsFileHisDTO();
                fileHisDTO.setId(fileId);
                fileHisDTO.setName(oaWpsModel.getName());
                fileHisDTO.setVersion(oaWpsModel.getVersion());
                fileHisDTO.setSize(oaWpsModel.getSize());
                fileHisDTO.setDownload_url(this.domain + WpsConstant.WPS_FILE_DOWN_PRE + oaWpsModel.getDownloadUrl());
                if (Objects.nonNull(oaWpsModel.getCreateTime())) {
                    fileHisDTO.setCreate_time(oaWpsModel.getCreateTime().getTime());
                }
                if (Objects.nonNull(oaWpsModel.getUpdateTime())) {
                    fileHisDTO.setModify_time(oaWpsModel.getUpdateTime().getTime());
                }
                List<LoginUser> creators = sysBaseAPI.queryUserByNames(new String[]{oaWpsModel.getCreateBy()});
                List<LoginUser> modifiers = sysBaseAPI.queryUserByNames(new String[]{oaWpsModel.getUpdateBy()});
                WpsUserDTO creator = new WpsUserDTO();
                WpsUserDTO modifier = new WpsUserDTO();
                if (!CollectionUtils.isEmpty(creators)) {
                    creator.setId(creators.get(0).getUsername());
                    creator.setName(creators.get(0).getRealname());
                    creator.setAvatar_url(creators.get(0).getAvatar());
                }
                if (!CollectionUtils.isEmpty(modifiers)) {
                    modifier.setId(modifiers.get(0).getUsername());
                    modifier.setName(modifiers.get(0).getRealname());
                    modifier.setAvatar_url(modifiers.get(0).getAvatar());
                }
                fileHisDTO.setCreator(creator);
                fileHisDTO.setModifier(modifier);
                return fileHisDTO;
            }).collect(Collectors.toList());
            result.put("histories", fileHisDTOS);
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
        if (Objects.nonNull(oaWpsModel.getCreateTime())) {
            fileDTO.setCreate_time(oaWpsModel.getCreateTime().getTime());
        }
        fileDTO.setModifier(oaWpsModel.getUpdateBy());
        if (Objects.nonNull(oaWpsModel.getUpdateTime())) {
            fileDTO.setModify_time(oaWpsModel.getUpdateTime().getTime());
        }
        return fileDTO;
    }
}
