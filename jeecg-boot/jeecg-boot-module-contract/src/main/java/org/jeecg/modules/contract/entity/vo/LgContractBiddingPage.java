package org.jeecg.modules.contract.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.contract.entity.LgContractBiddingItem;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 柳钢中标信息
 * @Author: jeecg-boot
 * @Date: 2021-04-13
 * @Version: V1.0
 */
@Data
@ApiModel(value = "lg_contract_biddingPage对象", description = "柳钢中标信息")
public class LgContractBiddingPage {

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
     * 公司代码
     */
    @Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    /**
     * 公司名称
     */
    @Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    /**
     * 供应商代码
     */
    @Excel(name = "供应商代码", width = 15)
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;
    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 采购组织
     */
    @Excel(name = "采购组织", width = 15)
    @ApiModelProperty(value = "采购组织")
    private String applyDept;
    /**
     * 中标金额
     */
    @Excel(name = "中标金额", width = 15)
    @ApiModelProperty(value = "中标金额")
    private String amount;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private String programCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String programName;
    /**
     * 经办人
     */
    @Excel(name = "经办人", width = 15)
    @ApiModelProperty(value = "经办人")
    private String userId;
    /**
     * 是否需要技术协议
     */
    @Excel(name = "是否需要技术协议", width = 15)
    @ApiModelProperty(value = "是否需要技术协议")
    private String isProtocol;
    /**
     * 中标时间
     */
    @Excel(name = "中标时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "中标时间")
    private Date biddingTime;
    /**
     * 属性
     */
    @Excel(name = "属性", width = 15)
    @ApiModelProperty(value = "属性")
    private String attract;
    /**
     * 是否含税
     */
    @Excel(name = "是否含税", width = 15)
    @ApiModelProperty(value = "是否含税")
    private String isRate;
    /**
     * 税率
     */
    @Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private String rate;
    /**
     * 招标编号
     */
    @Excel(name = "招标编号", width = 15)
    @ApiModelProperty(value = "招标编号")
    private String postCode;

    @ExcelCollection(name = "柳钢合同中标项")
    @ApiModelProperty(value = "柳钢合同中标项")
    private List<LgContractBiddingItem> lgContractBiddingItemList;

}
