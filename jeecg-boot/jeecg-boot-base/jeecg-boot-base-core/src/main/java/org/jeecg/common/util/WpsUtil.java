package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.config.WpsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

/**
 * @author motb
 * @date 2021/2/25 10:05
 * @description //TODO WpsUtil
 **/
@Slf4j
@Component
public class WpsUtil {

    private WpsProperties wpsProperties;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @Value(value = "${jeecg.uploadType}")
    private String uploadType;

    @Autowired
    public WpsUtil(WpsProperties wpsProperties) {
        this.wpsProperties = wpsProperties;
    }

    public String getWpsUrl(Map<String, String> values, String fileType, String fileId) {
        String keyValueStr = getUrlParam(values);
        String signature = getSignature(values, wpsProperties.getAppsecret());
        String fileTypeCode = getTypeCode(fileType);
        return wpsProperties.getDomain() + fileTypeCode + "/" + fileId + "?" + keyValueStr + "&_w_signature=" + signature;
    }

    public String getNewFileUrl(Map<String, String> values, String fileType) {
        String keyValueStr = getUrlParam(values);
        String signature = getSignature(values, wpsProperties.getAppsecret());
        String fileTypeCode = getTypeCode(fileType);
        return wpsProperties.getDomain() + fileTypeCode + "/new/0?" + keyValueStr + "&_w_signature=" + signature;
    }

    private String getUrlParam(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        params.put("_w_appid", wpsProperties.getAppid());
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (builder.length() > 0) {
                    builder.append('&');
                }
                builder.append(URLEncoder.encode(entry.getKey(), "utf-8")).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String getSignature(Map<String, String> params, String appSecret) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keys.add(entry.getKey());
        }

        // 将所有参数按key的升序排序
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // 构造签名的源字符串
        StringBuilder contents = new StringBuilder("");
        for (String key : keys) {
            if (key == "_w_signature") {
                continue;
            }
            contents.append(key + "=").append(params.get(key));
            System.out.println("key:" + key + ",value:" + params.get(key));
        }
        contents.append("_w_secretkey=").append(appSecret);

        // 进行hmac sha1 签名
        byte[] bytes = hmacSha1(appSecret.getBytes(), contents.toString().getBytes());
        //字符串经过Base64编码
        String sign = encodeBase64String(bytes);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(sign);
        return sign;
    }

    private byte[] hmacSha1(byte[] key, byte[] data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance(signingKey.getAlgorithm());
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTypeCode(String fileType) {
        if ("word".equalsIgnoreCase(fileType)) {
            return "w";
        }
        if ("excel".equalsIgnoreCase(fileType)) {
            return "s";
        }
        if ("ppt".equalsIgnoreCase(fileType)) {
            return "p";
        }
        if ("pdf".equalsIgnoreCase(fileType)) {
            return "f";
        }
        return null;
    }

    public String upload(MultipartFile file, String bizPath) {
        if (StringUtils.isEmpty(bizPath)) {
            bizPath = "temp";
        }
        String savePath = "";
        if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
            savePath = this.uploadLocal(file, bizPath);
        } else {
            savePath = CommonUtils.upload(file, bizPath, uploadType);
        }
        return savePath;
    }

    /**
     * 本地上传
     *
     * @param mf
     * @param bizPath
     * @return
     */
    private String uploadLocal(MultipartFile mf, String bizPath) {
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            File file = new File(ctxPath + File.separator + bizPath + File.separator);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String orgName = mf.getOriginalFilename();// 获取文件名
            orgName = CommonUtils.getFileName(orgName);
            if (orgName.indexOf(".") != -1) {
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            } else {
                fileName = orgName + "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if (oConvertUtils.isNotEmpty(bizPath)) {
                dbpath = bizPath + File.separator + fileName;
            } else {
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return dbpath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}