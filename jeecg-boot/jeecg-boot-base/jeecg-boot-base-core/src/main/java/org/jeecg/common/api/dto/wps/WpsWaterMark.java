package org.jeecg.common.api.dto.wps;

import lombok.Data;

@Data
public class WpsWaterMark {
    private int type = 0;              //水印类型， 0为无水印； 1为文字水印
    private String value = "禁止传阅";                  //文字水印的文字，当type为1时此字段必选
    private String fillstyle = "rgba( 192, 192, 192, 0.6 )";    //水印的透明度，非必选，有默认值
    private String font = "bold 20px Serif";       //水印的字体，非必选，有默认值
    private Double rotate = -0.7853982;           //水印的旋转度，非必选，有默认值
    private int horizontal = 50;             //水印水平间距，非必选，有默认值
    private int vertical = 100;             //水印垂直间距，非必选，有默认值
}