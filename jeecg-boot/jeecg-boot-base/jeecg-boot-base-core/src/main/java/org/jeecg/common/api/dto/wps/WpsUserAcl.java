package org.jeecg.common.api.dto.wps;

import lombok.Data;

@Data
public class WpsUserAcl {
    private int rename = 1;    //重命名权限，1为打开该权限，0为关闭该权限，默认为0
    private int history = 1;    //历史版本权限，1为打开该权限，0为关闭该权限,默认为1
    private int copy = 1;      //复制
    private int export = 1;    //导出PDF
    private int print = 1;     //打印
}