package org.jeecg.common.api.dto.wps;

import lombok.Data;

/**
 * @author motb
 * @date 2021/2/25 9:09
 * @description //TODO WpsDTO
 **/
@Data
public class WpsFileDTO {
    private String id;              //文件id,字符串长度小于32
    private String name;            //文件名(必须带文件后缀)
    private Integer version;                    //当前版本号，位数小于11
    private Long size;                        //文件大小，单位为B(文件真实大小，否则会出现异常)
    private String creator;                    //创建者id，字符串长度小于32
    private Long create_time;        //创建时间，时间戳，单位为秒
    private String modifier;                    //修改者id，字符串长度小于32
    private Long modify_time;        //修改时间，时间戳，单位为秒
    private String download_url;     //文档下载地址
    private Integer preview_pages = 0;    //限制预览页数
    private WpsUserAcl user_acl = new WpsUserAcl();
    private WpsWaterMark watermark = new WpsWaterMark();
}