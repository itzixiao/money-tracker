package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 账本流水统计返回
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookLedgerFlowStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 结余
     */
    @ApiModelProperty("结余")
    private Long balance = 0L;

    /**
     * 总收入
     */
    @ApiModelProperty("总收入")
    private Long totalRevenue = 0L;
    /**
     * 总支出
     */
    @ApiModelProperty("总支出")
    private Long totalSpending= 0L;
    /**
     * 记账笔数
     */
    @ApiModelProperty("记账笔数")
    private Integer pen = 0;

}
