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
public class BookMoneyReportTrendVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收入
     */
    @ApiModelProperty("收入")
    private List<Long> revenue = new ArrayList<>();

    /**
     * 支出
     */
    @ApiModelProperty("支出")
    private List<Long> expenditures = new ArrayList<>();

    @ApiModelProperty("日期x轴")
    private List<String> x = new ArrayList<>();
}
