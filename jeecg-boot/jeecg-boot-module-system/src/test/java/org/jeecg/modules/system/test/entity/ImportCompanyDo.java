package org.jeecg.modules.system.test.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Objects;

/**
 * @author motb
 * @date 2021/4/9 16:13
 * @description //TODO importCompanyDo
 **/
@Data
public class ImportCompanyDo {

    @Excel(name = "编号")
    private String id;

    @Excel(name = "分组")
    private String group;

    @Excel(name = "搜索词 1")
    private String context;

    @Excel(name = "属性")
    private String attr;

    @Excel(name = "法人")
    private String leader;

    @Excel(name = "名称1")
    private String name1;

    @Excel(name = "黑名单")
    private String black;

    @Excel(name = "业务角色")
    private String role;

    @Excel(name = "税号")
    private String creditCode;

    @Override
    public int hashCode() {
        if (id == null) {
            return 1;
        }
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportCompanyDo companyDo = (ImportCompanyDo) o;
        return Objects.equals(id, companyDo.id);
    }
}
