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
public class BookAccountVO {
    @ApiModelProperty("记账年月")
    private String data;

    @ApiModelProperty("记账金额")
    private Long money;
}
