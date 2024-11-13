package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 成员记账返回
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMemberCompareResVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 成员记账返回
     */
    @ApiModelProperty("成员记账返回")
    private List<BookMemberCompareVO> compareVOS = new ArrayList<>();
    /**
     * 总收入
     */
    @ApiModelProperty("总收入")
    private Integer totalAllRevenue ;

    @ApiModelProperty("总支出")
    private Integer totalAllSpending;
}
