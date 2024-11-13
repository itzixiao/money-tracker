package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class BookMemberResVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private List<String> x;
    /**
     * 记账笔数
     */
    @ApiModelProperty("记账笔数")
    private List<Integer> y;

    @ApiModelProperty("总笔数")
    private Integer total = 0;
}
