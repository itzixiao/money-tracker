package com.jcwl.admin.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcwl.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付宝对账单导入对象
 *
 * @author jcwl
 * @date 2024-10-14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AliPayImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date tradeTime;

    /**
     * 交易分类
     */
    @ApiModelProperty("交易分类")
    @Excel(name = "交易分类")
    private String tradeCategory;

    /**
     * 交易对方
     */
    @ApiModelProperty("交易对方")
    @Excel(name = "交易对方")
    private String tradeOpponent;

    /**
     * 商品说明
     */
    @ApiModelProperty("商品说明")
    @Excel(name = "商品说明")
    private String productDescription;

    /**
     * 收/支
     */
    @ApiModelProperty("收/支")
    @Excel(name = "收/支")
    private String incomeExpense;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    @Excel(name = "金额")
    private Double amount;

    /**
     * 收/付款方式
     */
    @ApiModelProperty("收/付款方式")
    @Excel(name = "收/付款方式")
    private String paymentMethod;

    /**
     * 交易状态
     */
    @ApiModelProperty("交易状态")
    @Excel(name = "交易状态")
    private String tradeStatus;

    /**
     * 交易订单号
     */
    @ApiModelProperty("交易订单号")
    @Excel(name = "交易订单号")
    private String orderNo;

    /**
     * 备注
     */
    @ApiModelProperty("备注 ")
    @Excel(name = "备注")
    private String remark;

}
