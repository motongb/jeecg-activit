package org.jeecg.modules.system.test.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author motb
 * @date 2021/4/9 16:50
 * @description //TODO ImportAddressDo
 **/
@Data
public class ImportAddressDo {

    @Excel(name = "企业编号")
    private String companyId;

    @Excel(name = "地址号码")
    private String code;

    @Excel(name = "街道")
    private String address;

    @Excel(name = "邮政编码")
    private String postCode;

    @Excel(name = "城市")
    private String city;

    @Excel(name = "国家/地区")
    private String country;

    @Excel(name = "电话")
    private String phone;

    @Excel(name = "移动电话")
    private String mobile;

    @Excel(name = "传真")
    private String fax;

    @Excel(name = "电子邮件地址")
    private String email;
}
