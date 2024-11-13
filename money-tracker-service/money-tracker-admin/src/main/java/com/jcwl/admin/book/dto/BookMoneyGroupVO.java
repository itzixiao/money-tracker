package com.jcwl.admin.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcwl.admin.book.domain.BookMoney;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 账本明细聚合
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoneyGroupVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记账时间
     */
    @ApiModelProperty("记账时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bookTime;

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
     * 账本明细列表
     */
    @ApiModelProperty("账本明细列表")
    private List<BookMoney> bookMoneyList;

}
