package org.jeecg.common.api.dto.wps;

import lombok.Data;

/**
 * @author motb
 * @date 2021/3/2 9:54
 * @description //TODO WpsFileHisDTO
 **/
@Data
public class WpsFileHisDTO {

    private String id;

    private String name;

    private int version;

    private long size;

    private long create_time;

    private long modify_time;

    private String download_url;

    private WpsUserDTO creator;

    private WpsUserDTO modifier;
}
