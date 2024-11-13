package com.jcwl.admin.book.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 账户资产明细
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookAccountDetailRspVO {

    @ApiModelProperty("资产明细")
    private List<BookAccountDetailVO> assetDetails;

    @ApiModelProperty("负债明细")
    private List<BookAccountDetailVO> liabilityDetails;
}
