package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 成员开支收入返回
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookExpenditureIncomeResVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private List<BookExpenditureIncomeVO> vos;
    /**
     * 记账笔数
     */
    @ApiModelProperty("总支出/总收入")
    private Integer money;


}
