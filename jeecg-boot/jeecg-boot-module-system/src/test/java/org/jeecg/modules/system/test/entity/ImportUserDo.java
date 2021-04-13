package org.jeecg.modules.system.test.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author motb
 * @date 2021/4/9 10:00
 * @description //TODO ImportUserDo
 **/
@Data
public class ImportUserDo {

    @Excel(name = "员工编号")
    private String id;

    @Excel(name = "员工姓名")
    private String name;

    @Excel(name = "性别性别")
    private String sex;

    @Excel(name = "出生日期")
    private String birthday;

    @Excel(name = "入司时间")
    private String joinTime;

    @Excel(name = "组织编码")
    private String orgId;

    @Excel(name = "岗位编码")
    private String post;

    @Excel(name = "人员状态（编码）")
    private String status;
}
