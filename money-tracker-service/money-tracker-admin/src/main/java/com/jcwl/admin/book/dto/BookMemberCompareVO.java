package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 成员记账返回
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMemberCompareVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String name;
    /**
     * 用户头像地址
     */
    @ApiModelProperty("用户头像地址")
    private String avatar;
    /**
     * 总支出
     */
    @ApiModelProperty("总支出")
    private Integer totalSpending;
    /**
     * 总收入
     */
    @ApiModelProperty("总收入")
    private Integer totalRevenue;
}
