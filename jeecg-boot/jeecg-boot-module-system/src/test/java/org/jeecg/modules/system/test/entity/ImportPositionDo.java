package org.jeecg.modules.system.test.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author motb
 * @date 2021/4/9 9:16
 * @description //TODO ImportPositionDo
 **/
@Data
public class ImportPositionDo {

    @Excel(name = "岗位编码")
    private String id;

    @Excel(name = "岗位名称")
    private String name;

    @Excel(name = "组织编码")
    private String companyId;

    @Excel(name = "岗位级别")
    private String level;

    @Excel(name = "是否主管岗位")
    private String isHeader;

    @Excel(name = "岗位状态（编码）")
    private String status;
}
