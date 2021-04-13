package org.jeecg.modules.system.test.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author motb
 * @date 2021/4/8 17:25
 * @description //TODO ImportDepart
 **/
@Data
public class ImportDepartDo {

    @Excel(name = "组织编码")
    private String id;

    @Excel(name = "组织名称")
    private String name;

    @Excel(name = "组织类型（编码）")
    private String category;

    @Excel(name = "组织状态（编码）")
    private String status;

    @Excel(name = "大类（编码）")
    private String type;

    @Excel(name = "上级编码")
    private String pid;

    @Excel(name = "备用1")
    private String otherCode;
}
