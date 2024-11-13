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
 * 微信对账单导入对象
 *
 * @author jcwl
 * @date 2024-10-14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatPayImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date tradeTime;

    /**
     * 交易类型
     */
    @ApiModelProperty("交易类型")
    @Excel(name = "交易类型")
    private String tradeCategory;

    /**
     * 交易对方
     */
    @ApiModelProperty("交易对方")
    @Excel(name = "交易对方")
    private String tradeOpponent;

    /**
     * 商品
     */
    @ApiModelProperty("商品")
    @Excel(name = "商品")
    private String productDescription;

    /**
     * 收/支
     */
    @ApiModelProperty("收/支")
    @Excel(name = "收/支")
    private String incomeExpense;

    /**
     * 金额(元)
     */
    @ApiModelProperty("金额(元)")
    @Excel(name = "金额(元)")
    private Double amount;

    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    @Excel(name = "支付方式")
    private String paymentMethod;

    /**
     * 当前状态
     */
    @ApiModelProperty("当前状态")
    @Excel(name = "当前状态")
    private String tradeStatus;

    /**
     * 交易单号
     */
    @ApiModelProperty("交易单号")
    @Excel(name = "交易单号")
    private String orderNo;

    /**
     * 备注
     */
    @ApiModelProperty("备注 ")
    @Excel(name = "备注")
    private String remark;

}
