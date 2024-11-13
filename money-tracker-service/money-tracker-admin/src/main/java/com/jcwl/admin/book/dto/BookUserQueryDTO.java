package com.jcwl.admin.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcwl.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 账本用户对象 book_user
 *
 * @author jcwl
 * @date 2024-10-15
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookUserQueryDTO extends PageDomain {
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
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    private String avatar;

    /**
     * 是否默认账本(0-否 1是）
     */
    @ApiModelProperty("是否默认账本(0-否 1是）")
    private Long defaultBook;

    /**
     * 0-无 1-同意 2-拒绝
     */
    @ApiModelProperty("0-无 1-同意 2-拒绝")
    private String auditStatus;

    /**
     * 0-存在 1-禁用 2-删除
     */
    @ApiModelProperty("0-存在 1-禁用 2-删除")
    private String status;

    /**
     * 是否管理员权限（0-成员 1-管理员 2-创建人）
     */
    @ApiModelProperty("是否管理员权限（0-成员 1-管理员 2-创建人）")
    private Integer auth;

    /**
     * 预留字段1
     */
    @ApiModelProperty("预留字段1")
    private String attr1;

    /**
     * 预留字段2
     */
    @ApiModelProperty("预留字段2")
    private String attr2;

    /**
     * 预留字段3
     */
    @ApiModelProperty("预留字段3")
    private Integer attr3;

    /**
     * 预留字段4
     */
    @ApiModelProperty("预留字段4")
    private Integer attr4;

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

    /**
     * 用户唯一标识
     */
    @JsonIgnore
    @ApiModelProperty("用户唯一标识")
    private Long orderByUserId;
}
