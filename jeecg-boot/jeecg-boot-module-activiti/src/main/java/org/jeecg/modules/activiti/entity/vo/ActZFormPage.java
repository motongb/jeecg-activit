package org.jeecg.modules.activiti.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.activiti.entity.ActZFieldGroup;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 流程表单
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
@Data
@ApiModel(value = "act_z_formPage对象", description = "流程表单")
public class ActZFormPage {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 表单标题
     */
    @Excel(name = "表单标题", width = 15)
    @ApiModelProperty(value = "表单标题")
    private String name;
    /**
     * 表单编码
     */
    @Excel(name = "表单编码", width = 15)
    @ApiModelProperty(value = "表单编码")
    private String code;
    /**
     * 组件路径
     */
    @Excel(name = "组件路径", width = 15)
    @ApiModelProperty(value = "组件路径")
    private String path;
    /**
     * 表单样式
     */
    @Excel(name = "表单样式", width = 15, dicCode = "act_z_form_style")
    @Dict(dicCode = "act_z_form_style")
    @ApiModelProperty(value = "表单样式")
    private String style;
    /**
     * 数据库表类型
     */
    @Excel(name = "数据库表类型", width = 15, dicCode = "ol_form_biz_type")
    @Dict(dicCode = "ol_form_biz_type")
    @ApiModelProperty(value = "数据库表类型")
    private String tableType;
    /**
     * 数据库表名
     */
    @Excel(name = "数据库表名", width = 15, dictTable = "onl_cgform_head", dicText = "table_txt", dicCode = "table_name")
    @Dict(dictTable = "onl_cgform_head", dicText = "table_txt", dicCode = "table_name")
    @ApiModelProperty(value = "数据库表名")
    private String tableName;
    /**
     * 类型
     */
    @Excel(name = "类型", width = 15, dicCode = "act_z_form_type")
    @Dict(dicCode = "act_z_form_type")
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 启用
     */
    @Excel(name = "启用", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "启用")
    private Integer status;
    /**
     * 表元数据id
     */
    @Excel(name = "表元数据id", width = 15)
    @ApiModelProperty(value = "表元数据id")
    private String tableMetaId;

    @ExcelCollection(name = "流程表字段组")
    @ApiModelProperty(value = "流程表字段组")
    private List<ActZFieldGroup> actZFieldGroupList;

}
