package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 账户趋势图
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookAccountTrendVO {
    @ApiModelProperty("记账年月")
    private List<String> x;

    @ApiModelProperty("记账金额")
    private List<Long> y;
}
