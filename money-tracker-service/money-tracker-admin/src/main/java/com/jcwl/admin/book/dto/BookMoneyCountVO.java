package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 账本明细聚合
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoneyCountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记账月份
     */
    @ApiModelProperty("记账月份")
    private String bookMonth;

    /**
     * 收入
     */
    @ApiModelProperty("收入")
    private Long income;

    /**
     * 支出
     */
    @ApiModelProperty("支出")
    private Long expenditure;

    /**
     * 结余
     */
    @ApiModelProperty("结余")
    private Long balance;

}
