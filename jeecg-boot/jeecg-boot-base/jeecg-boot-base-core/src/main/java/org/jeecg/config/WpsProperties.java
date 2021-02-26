package org.jeecg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author motb
 * @date 2021/2/25 10:54
 * @description //TODO WpsProperties
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jeecg.wps")
public class WpsProperties {

    private String domain;

    private String appid;

    private String appsecret;
}
