package org.jeecg.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 物资表
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Data
@TableName("materiel")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "materiel对象", description = "物资表")
public class Materiel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String text;
    /**
     * 长描述
     */
    @Excel(name = "长描述", width = 15)
    @ApiModelProperty(value = "长描述")
    private String textLong;
    /**
     * 编号
     */
    @Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "编号")
    private String code;
    /**
     * 分类
     */
    @Excel(name = "物料类型", width = 15)
    @ApiModelProperty(value = "物料类型")
    private String category;
    /**
     * 物料组
     */
    @Excel(name = "物料组", width = 15)
    @ApiModelProperty(value = "物料组")
    private String groups;
    /**
     * 物料组描述
     */
    @Excel(name = "物料组描述", width = 15)
    @ApiModelProperty(value = "物料组描述")
    private String groupsName;
    /**
     * 基本单位
     */
    @Excel(name = "基本单位", width = 15)
    @ApiModelProperty(value = "基本单位")
    private String unit;
    /**
     * 大小/量纲
     */
    @Excel(name = "大小/量纲", width = 15)
    @ApiModelProperty(value = "大小/量纲")
    private String size;
    /**
     * 毛重
     */
    @Excel(name = "毛重", width = 15)
    @ApiModelProperty(value = "毛重")
    private String grossWeight;
    /**
     * 净重
     */
    @Excel(name = "净重", width = 15)
    @ApiModelProperty(value = "净重")
    private String netWeight;
    /**
     * 重量单位
     */
    @Excel(name = "重量单位", width = 15)
    @ApiModelProperty(value = "重量单位")
    private String unitWeight;
    /**
     * 体积
     */
    @Excel(name = "体积", width = 15)
    @ApiModelProperty(value = "体积")
    private String volume;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 行业标准描述
     */
    @Excel(name = "行业标准描述", width = 15)
    @ApiModelProperty(value = "行业标准描述")
    private String standard;
    /**
     * 删除标识
     */
    @Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private String delFlag;
}
