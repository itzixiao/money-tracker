package com.jcwl.admin.book.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcwl.common.annotation.Excel;
import com.jcwl.common.core.domain.JcwlBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 账本明细对象 book_money
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMoney extends JcwlBaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    private Long id;

    /**
     * 账本唯一标识
     */
    @ApiModelProperty("账本唯一标识")
    @Excel(name = "账本唯一标识")
    private Long bookId;

    /**
     * 基础分类唯一标识
     */
    @ApiModelProperty("基础分类唯一标识")
    @Excel(name = "基础分类唯一标识")
    private Long categoryBaseId;

    /**
     * 分类唯一标识
     */
    @ApiModelProperty("分类唯一标识")
    @Excel(name = "分类唯一标识")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    @Excel(name = "分类名称")
    private String categoryName;

    /**
     * 分类图标
     */
    @ApiModelProperty("分类图标")
    @Excel(name = "分类图标")
    private String categoryUrl;

    /**
     * 基础账户唯一标识
     */
    @ApiModelProperty("基础账户唯一标识")
    @Excel(name = "基础账户唯一标识")
    private Long accountBaseId;

    /**
     * 账户唯一标识
     */
    @ApiModelProperty("账户唯一标识")
    @Excel(name = "账户唯一标识")
    private Long accountId;

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    @Excel(name = "账户名称")
    private String accountName;

    /**
     * 账户图标
     */
    @ApiModelProperty("账户图标")
    @Excel(name = "账户图标")
    private String accountUrl;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    @Excel(name = "用户唯一标识")
    private Long userId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    @Excel(name = "用户账号")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    @Excel(name = "用户昵称")
    private String nickName;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    @Excel(name = "金额")
    private Long money;

    /**
     * 记账时间
     */
    @ApiModelProperty("记账时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "记账时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bookTime;

    /**
     * 分组记账时间
     */
    @ApiModelProperty("分组记账时间")
    @JsonIgnore
    private Date groupBookTime;

    public Date getGroupBookTime() {
        if (bookTime == null) {
            return null;
        }
        LocalDate localDate = bookTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 分组记账时间
     */
    @ApiModelProperty("0-收入 1-支出 2-不计收支")
    @Excel(name = "0-收入 1-支出 2-不计收支")
    private Integer type;

    /**
     * 富文本详情
     */
    @ApiModelProperty("富文本详情")
    @Excel(name = "富文本详情")
    private String detailDesc;

    /**
     * 是否显示详情 0-否 1-是
     */
    @ApiModelProperty("是否显示详情 0-否 1-是")
    @Excel(name = "是否显示详情 0-否 1-是")
    private String detailDescFlag;

    /**
     * 附件地址
     */
    @ApiModelProperty("附件地址")
    @Excel(name = "附件地址")
    private String detailImg;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 创建者ID
     */
    @ApiModelProperty("创建者ID")
    @Excel(name = "创建者ID")
    private Long createId;

    /**
     * 更新者ID
     */
    @ApiModelProperty("更新者ID")
    @Excel(name = "更新者ID")
    private Long updateId;

}
