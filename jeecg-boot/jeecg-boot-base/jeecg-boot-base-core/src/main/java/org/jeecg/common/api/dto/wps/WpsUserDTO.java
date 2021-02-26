package org.jeecg.common.api.dto.wps;

import lombok.Data;

/**
 * @author motb
 * @date 2021/2/25 14:42
 * @description //TODO WpsUserDTO
 **/
@Data
public class WpsUserDTO {
    private String id;                //用户id，长度小于32
    private String name;            //用户名称
    private String permission = "write";    //用户操作权限，write：可编辑，read：预览
    private String avatar_url;    //用户头像地址
}
