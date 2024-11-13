package com.jcwl.admin.book.dto;

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
public class BookAccountLineVO implements Serializable {
    private static final long serialVersionUID = 1L;




    @ApiModelProperty("日期x轴")
    private List<String> x = new ArrayList<>();

    /**
     * 余额
     */
    @ApiModelProperty("余额")
    private List<Double> y = new ArrayList<>();
}
