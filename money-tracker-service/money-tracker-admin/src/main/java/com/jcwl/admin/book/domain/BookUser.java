package com.jcwl.admin.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcwl.common.annotation.Excel;
import com.jcwl.common.core.domain.JcwlBaseEntity;
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
public class BookUser extends JcwlBaseEntity {
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
    @NotNull(message = "账本唯一标识不能为空")
    private Long bookId;

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
    @NotNull(message = "用户账号不能为空")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    @Excel(name = "用户昵称")
    private String nickName;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    @Excel(name = "头像地址")
    private String avatar;

    /**
     * 是否默认账本(0-否 1是）
     */
    @ApiModelProperty("是否默认账本(0-否 1是）")
    @Excel(name = "是否默认账本(0-否 1是）")
    private Long defaultBook;

    /**
     * 0-无 1-同意 2-拒绝
     */
    @ApiModelProperty("0-无 1-同意 2-拒绝")
    @Excel(name = "0-无 1-同意 2-拒绝")
    private String auditStatus;

    /**
     * 0-存在 1-禁用 2-删除
     */
    @ApiModelProperty("0-存在 1-禁用 2-删除")
    @Excel(name = "0-存在 1-禁用 2-删除")
    private String status;

    /**
     * 是否管理员权限（0-成员 1-管理员 2-创建人）
     */
    @ApiModelProperty("是否管理员权限（0-成员 1-管理员 2-创建人）")
    @Excel(name = "是否管理员权限", readConverterExp = "0=-成员,1=-管理员,2=-创建人")
    private Integer auth;

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
