package com.jcwl.admin.account.domain;

import com.jcwl.common.annotation.Excel;
import com.jcwl.common.core.domain.JcwlBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账户对象 book_account
 *
 * @author jcwl
 * @date 2024-10-24
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account extends JcwlBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 账户唯一标识
     */
    @ApiModelProperty("账户唯一标识")
    private Long id;

    /**
     * 基础账户唯一标识
     */
    @ApiModelProperty("基础账户唯一标识")
    @Excel(name = "基础账户唯一标识")
    private Long accountBaseId;

    /**
     * 父级节点唯一标识
     */
    @ApiModelProperty("父级节点唯一标识")
    @Excel(name = "父级节点唯一标识")
    private Long parentId;

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    @Excel(name = "账户名称")
    private String name;

    /**
     * 账户编码
     */
    @ApiModelProperty("账户编码")
    @Excel(name = "账户编码")
    private String code;

    /**
     * 账户图标
     */
    @ApiModelProperty("账户图标")
    @Excel(name = "账户图标")
    private String accountUrl;

    /**
     * 是否有子节点
     */
    @ApiModelProperty("是否有子节点")
    @Excel(name = "是否有子节点")
    private String hasChild;

    /**
     * 账本唯一标识
     */
    @ApiModelProperty("账本唯一标识")
    @Excel(name = "账本唯一标识")
    private Long bookId;

    /**
     * 余额
     */
    @ApiModelProperty("余额")
    @Excel(name = "余额")
    private Long balance = 0L;

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
