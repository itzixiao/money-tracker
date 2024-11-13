package com.jcwl.admin.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Json对账单导入对象
 *
 * @author jcwl
 * @date 2024-10-14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImportJsonDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间")
    private Date day;

    /**
     * 收/支
     */
    @ApiModelProperty("收/支")
    private String flowType;

    /**
     * 交易分类
     */
    @ApiModelProperty("交易分类")
    private String type;

    /**
     * 支付分类
     */
    @ApiModelProperty("支付分类")
    private String payType;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Double money;

    /**
     * 交易对方
     */
    @ApiModelProperty("交易对方")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;
}
