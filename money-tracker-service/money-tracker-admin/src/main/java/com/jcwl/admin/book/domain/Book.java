package com.jcwl.admin.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcwl.common.annotation.Excel;
import com.jcwl.common.core.domain.JcwlBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账本对象 book
 *
 * @author jcwl
 * @date 2024-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book extends JcwlBaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    private Long id;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    @Excel(name = "用户唯一标识")
    private Long userId;

    /**
     * 账本名称
     */
    @ApiModelProperty("账本名称")
    @Excel(name = "账本名称")
    private String bookName;

    /**
     * 账本图片
     */
    @ApiModelProperty("账本图片")
    @Excel(name = "账本图片")
    private String bookAvatar;

    /**
     * 是否默认账本(0-否 1是）
     */
    @ApiModelProperty("是否默认账本(0-否 1是）")
    private Long defaultBook;

    /**
     * 成员加入账本是否需要审核（0-不需要 1-需要管理员审核）
     */
    @ApiModelProperty("成员加入账本是否需要审核（0-不需要 1-需要管理员审核）")
    @Excel(name = "成员加入账本是否需要审核", readConverterExp = "0=-不需要,1=-需要管理员审核")
    private Long userAudit;

    /**
     * 是否允许对外搜索私密账本（0-私密不允许 1-公开允许）
     */
    @ApiModelProperty("是否允许对外搜索私密账本（0-私密不允许 1-公开允许）")
    @Excel(name = "是否允许对外搜索私密账本", readConverterExp = "0=-私密不允许,1=-公开允许")
    private Long showSearch;

    /**
     * 预留字段1
     */
    @ApiModelProperty("预留字段1")
    @Excel(name = "预留字段1")
    @JsonIgnore
    private String attr1;

    /**
     * 预留字段2
     */
    @ApiModelProperty("预留字段2")
    @Excel(name = "预留字段2")
    @JsonIgnore
    private String attr2;

    /**
     * 预留字段3
     */
    @ApiModelProperty("预留字段3")
    @Excel(name = "预留字段3")
    @JsonIgnore
    private Integer attr3;

    /**
     * 预留字段4
     */
    @ApiModelProperty("预留字段4")
    @Excel(name = "预留字段4")
    @JsonIgnore
    private Integer attr4;

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
