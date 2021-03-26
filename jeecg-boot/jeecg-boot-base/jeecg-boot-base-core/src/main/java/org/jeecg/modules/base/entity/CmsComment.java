package org.jeecg.modules.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 评论表
 * @Author: jeecg-boot
 * @Date: 2021-03-18
 * @Version: V1.0
 */
@Data
@TableName("cms_comment")
@ApiModel(value = "cms_comment对象", description = "评论表")
public class CmsComment extends TreeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 内容
     */
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 业务id
     */
    @Excel(name = "业务id", width = 15)
    @ApiModelProperty(value = "业务id")
    private String bindId;

    @ApiModelProperty(value = "接收人")
    private String receive;

    @ApiModelProperty(value = "标题")
    private String title;

    @TableField(exist = false)
    private String post;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String author;
}
