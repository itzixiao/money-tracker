package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 月度余额表
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonthlyBalanceVO {
    /**
     * 账单月份
     */
    @ApiModelProperty("账单月份")
    private String bookTime;
    /**
     * 账本id
     */
    @ApiModelProperty("账本id")
    private Long bookId;
    /**
     * 余额
     */
    @ApiModelProperty("余额")
    private Long balance;
    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    private Long userId;

}
