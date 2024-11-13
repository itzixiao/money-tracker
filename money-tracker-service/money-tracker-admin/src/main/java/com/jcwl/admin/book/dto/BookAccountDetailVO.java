package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账户资产明细
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookAccountDetailVO {

    @ApiModelProperty("账户名称")
    private String name;

    @ApiModelProperty("余额")
    private Long balance;

    @ApiModelProperty("账户图标路径")
    private String accountUrl;
}
