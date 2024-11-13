package com.jcwl.admin.book.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcwl.common.core.page.PageDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 账本明细对象 book_money
 *
 * @author jcwl
 * @date 2024-10-18
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoneyQueryDTO extends PageDomain{
    private static final long serialVersionUID = 1L;

    /**
     * 导出文件名
     */
    @ApiModelProperty("导出文件名")
    private String exportFileName;


    /**
     * 账本唯一标识
     */
    @NotNull(message = "账本唯一标识不能为空")
    @ApiModelProperty("账本唯一标识")
    private Long bookId;

    /**
     * 基础分类唯一标识
     */
    @ApiModelProperty("基础分类唯一标识")
    private Long categoryBaseId;

    /**
     * 分类唯一标识
     */
    @ApiModelProperty("分类唯一标识")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 分类图标
     */
    @ApiModelProperty("分类图标")
    private String categoryUrl;

    /**
     * 基础账户唯一标识
     */
    @ApiModelProperty("基础账户唯一标识")
    private Long accountBaseId;

    /**
     * 账户唯一标识
     */
    @ApiModelProperty("账户唯一标识")
    private Long accountId;

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String accountName;

    /**
     * 账户图标
     */
    @ApiModelProperty("账户图标")
    private String accountUrl;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    private Long userId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Long money;

    /**
     * 记账时间
     */
    @ApiModelProperty("记账时间")
    private Date bookTime;

    /**
     * 记账时间
     */
    @ApiModelProperty("记账时间开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginBookTime;

    /**
     * 记账时间
     */
    @ApiModelProperty("记账时间结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endBookTime;

    /**
     * 0-收入 1-支出 2-不计收支
     */
    @ApiModelProperty("0-收入 1-支出 2-不计收支")
    private Integer type;

    /**
     * 富文本详情
     */
    @ApiModelProperty("富文本详情")
    private String detailDesc;

    /**
     * 是否显示详情 0-否 1-是
     */
    @ApiModelProperty("是否显示详情 0-否 1-是")
    private String detailDescFlag;

    /**
     * 附件地址
     */
    @ApiModelProperty("附件地址")
    private String detailImg;

    /**
     * 创建者ID
     */
    @ApiModelProperty("创建者ID")
    private Long createId;

    /**
     * 更新者ID
     */
    @ApiModelProperty("更新者ID")
    private Long updateId;
}
