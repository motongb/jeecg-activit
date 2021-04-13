package org.jeecg.modules.system.test.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author motb
 * @date 2021/4/9 16:52
 * @description //TODO ImportBankDo
 **/
@Data
public class ImportBankDo {

    @Excel(name = "企业编号")
    private String companyId;

    @Excel(name = "银行国家")
    private String country;

    @Excel(name = "银行编号")
    private String bankCode;

    @Excel(name = "帐户名称")
    private String bank;

    @Excel(name = "银行帐户")
    private String bankNo;

    @Excel(name = "帐户持有人")
    private String bankName;
}
