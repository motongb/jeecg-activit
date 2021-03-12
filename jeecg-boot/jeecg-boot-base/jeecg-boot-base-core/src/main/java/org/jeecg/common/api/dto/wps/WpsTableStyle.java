package org.jeecg.common.api.dto.wps;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author motb
 * @date 2021/3/10 17:24
 * @description //TODO WpsCellStyle
 **/
@Data
@AllArgsConstructor
public class WpsTableStyle {

    private boolean bold;

    private String fontFamily;

    private int fontSize = 14;

    private int height = -1;
}
