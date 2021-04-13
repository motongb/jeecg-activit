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
 * @Description: 柳钢采购合同表
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Data
@TableName("lg_contract_purchase")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "lg_contract_purchase对象", description = "柳钢采购合同表")
public class LgContractPurchase implements Serializable {
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
     * 合同名称
     */
    @Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String name;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String code;
    /**
     * 类型编码
     */
    @Excel(name = "类型编码", width = 15)
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    /**
     * 我方编码
     */
    @Excel(name = "我方编码", width = 15)
    @ApiModelProperty(value = "我方编码")
    private String firstMember;
    /**
     * 乙方编码
     */
    @Excel(name = "乙方编码", width = 15)
    @ApiModelProperty(value = "乙方编码")
    private String secondMember;
    /**
     * 丙方编码
     */
    @Excel(name = "丙方编码", width = 15)
    @ApiModelProperty(value = "丙方编码")
    private String thirdMember;
    /**
     * 经办人
     */
    @Excel(name = "经办人", width = 15)
    @ApiModelProperty(value = "经办人")
    private String userId;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 签署方数
     */
    @Excel(name = "签署方数", width = 15)
    @ApiModelProperty(value = "签署方数")
    private Integer memberUse;
    /**
     * 是否自动编号,1-是,0-否
     */
    @Excel(name = "是否自动编号,1-是,0-否", width = 15)
    @ApiModelProperty(value = "是否自动编号,1-是,0-否")
    private Integer isAutoCode;
    /**
     * 流程记录id
     */
    @Excel(name = "流程记录id", width = 15)
    @ApiModelProperty(value = "流程记录id")
    private String actBusiness;
    /**
     * 公司代码
     */
    @Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    /**
     * 采购方式
     */
    @Excel(name = "采购方式", width = 15)
    @ApiModelProperty(value = "采购方式")
    private String purchaseWay;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private String programNo;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String programName;
    /**
     * 招标编号
     */
    @Excel(name = "招标编号", width = 15)
    @ApiModelProperty(value = "招标编号")
    private String postCode;
    /**
     * 中标id
     */
    @Excel(name = "中标id", width = 15)
    @ApiModelProperty(value = "中标id")
    private String biddingId;
    /**
     * 合同性质
     */
    @Excel(name = "合同性质", width = 15)
    @ApiModelProperty(value = "合同性质")
    private String attr;
    /**
     * 年度
     */
    @Excel(name = "年度", width = 15)
    @ApiModelProperty(value = "年度")
    private String year;
    /**
     * 是否使用模板：1-是，0-否
     */
    @Excel(name = "是否使用模板：1-是，0-否", width = 15)
    @ApiModelProperty(value = "是否使用模板：1-是，0-否")
    private Integer useModel;
    /**
     * 原模版id
     */
    @Excel(name = "原模版id", width = 15)
    @ApiModelProperty(value = "原模版id")
    private String sourceModel;
    /**
     * 合同正文
     */
    @Excel(name = "合同正文", width = 15)
    @ApiModelProperty(value = "合同正文")
    private String fileContract;
    /**
     * 模板文件
     */
    @Excel(name = "模板文件", width = 15)
    @ApiModelProperty(value = "模板文件")
    private String fileModel;
    /**
     * 合同附件
     */
    @Excel(name = "合同附件", width = 15)
    @ApiModelProperty(value = "合同附件")
    private String fileAttach;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 合同价款
     */
    @Excel(name = "合同价款", width = 15)
    @ApiModelProperty(value = "合同价款")
    private String amount;
    /**
     * 让利
     */
    @Excel(name = "让利", width = 15)
    @ApiModelProperty(value = "让利")
    private String jangli;
    /**
     * 货币码
     */
    @Excel(name = "货币码", width = 15)
    @ApiModelProperty(value = "货币码")
    private String coin;
    /**
     * 付款方式
     */
    @Excel(name = "付款方式", width = 15)
    @ApiModelProperty(value = "付款方式")
    private String payWay;
    /**
     * 是否含税
     */
    @Excel(name = "是否含税", width = 15)
    @ApiModelProperty(value = "是否含税")
    private Integer isRate;
    /**
     * 税率
     */
    @Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private String rate;
    /**
     * 是否安装
     */
    @Excel(name = "是否安装", width = 15)
    @ApiModelProperty(value = "是否安装")
    private Integer isInstall;
    /**
     * 技术协议
     */
    @Excel(name = "技术协议", width = 15)
    @ApiModelProperty(value = "技术协议")
    private String protocol;
    /**
     * 签订地点
     */
    @Excel(name = "签订地点", width = 15)
    @ApiModelProperty(value = "签订地点")
    private String signPlace;
    /**
     * 签订时间
     */
    @Excel(name = "签订时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "签订时间")
    private Date signTime;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private String link;
    /**
     * 有无技术协议
     */
    @Excel(name = "有无技术协议", width = 15)
    @ApiModelProperty(value = "有无技术协议")
    private Integer hasProtocol;
}
