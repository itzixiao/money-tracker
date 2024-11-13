package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 账本报表请求
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoneyReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记账年月
     */
    @ApiModelProperty("记账年月")
    private String bookTime;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    private Long userId;

    /**
     * 账本id
     */
    @ApiModelProperty("账本id")
    private Long bookId;

    /**
     * 标识  0.今日  1.本月  2.本年
     */
    @ApiModelProperty("标识  0.今日  1.本月  2.本年")
    private Integer logoType;

    /**
     * 0-收入 1-支出
     */
    @ApiModelProperty("0-收入 1-支出")
    private Integer type;
}
