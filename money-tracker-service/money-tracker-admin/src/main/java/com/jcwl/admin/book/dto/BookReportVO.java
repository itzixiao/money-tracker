package com.jcwl.admin.book.dto;

import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.common.annotation.Excel;
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
public class BookReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;
    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Long money;
    /**
     * 0-收入 1-支出
     */
    @ApiModelProperty("0-收入 1-支出")
    private Integer type;

    /**
     * 图标路径
     */
    @ApiModelProperty("图标路径")
    private String categoryUrl;
}
