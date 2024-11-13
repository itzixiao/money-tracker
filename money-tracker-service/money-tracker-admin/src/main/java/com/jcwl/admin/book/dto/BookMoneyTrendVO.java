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
 * 账本趋势图返回
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoneyTrendVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收入
     */
    @ApiModelProperty("日期")
    private String bookTime;

    /**
     * 支出
     */
    @ApiModelProperty("总金额")
    private Long money ;

}
