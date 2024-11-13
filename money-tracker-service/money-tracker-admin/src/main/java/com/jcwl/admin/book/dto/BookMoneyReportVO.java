package com.jcwl.admin.book.dto;

import com.jcwl.admin.book.domain.BookMoney;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 账本报表请求
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoneyReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收入
     */
    @ApiModelProperty("收入")
    private List<BookReportVO> revenue = new ArrayList<>();

    /**
     * 支出
     */
    @ApiModelProperty("支出")
    private List<BookReportVO> expenditures = new ArrayList<>();

    /**
     * 总支出金额
     */
    @ApiModelProperty("总支出金额")
    private Long totalExpenditure;
    /**
     * 总支出笔数
     */
    @ApiModelProperty("总支出笔数")
    private Integer totalExpenditureCount;

    /**
     * 总收入金额
     */
    @ApiModelProperty("总收入金额")
    private Long totalRevenue;

    @ApiModelProperty("总收入笔数")
    private Integer totalRevenueCount;
}
