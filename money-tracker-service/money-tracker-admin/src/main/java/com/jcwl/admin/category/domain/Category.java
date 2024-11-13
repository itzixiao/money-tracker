package com.jcwl.admin.category.domain;

import com.jcwl.common.annotation.Excel;
import com.jcwl.common.core.domain.TreeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 分类对象 category
 *
 * @author jcwl
 * @date 2024-10-23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category extends TreeEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 分类唯一标识
     */
    @ApiModelProperty("分类唯一标识")
    private Long id;

    /**
     * 基础分类唯一标识
     */
    @ApiModelProperty("基础分类唯一标识")
    @Excel(name = "基础分类唯一标识")
    private Long categoryBaseId;

    /**
     * 父级节点唯一标识
     */
    @ApiModelProperty("父级节点唯一标识")
    @Excel(name = "父级节点唯一标识")
    @NotNull(message = "父级节点唯一标识不能为空")
    private Long parentId;

    /**
     * 类型名称
     */
    @ApiModelProperty("类型名称")
    @Excel(name = "类型名称")
    private String name;

    /**
     * 类型编码
     */
    @ApiModelProperty("类型编码")
    @Excel(name = "类型编码")
    private String code;

    /**
     * 分类图标
     */
    @ApiModelProperty("分类图标")
    @Excel(name = "分类图标")
    private String categoryUrl;

    /**
     * 是否有子节点
     */
    @ApiModelProperty("是否有子节点")
    @Excel(name = "是否有子节点")
    private String hasChild;

    /**
     * 0-收入 1-支出
     */
    @ApiModelProperty("0-收入 1-支出")
    @Excel(name = "0-收入 1-支出")
    @NotNull(message = "收支类型不能为空")
    private Integer type;

    /**
     * 账本唯一标识
     */
    @ApiModelProperty("账本唯一标识")
    @Excel(name = "账本唯一标识")
    @NotNull(message = "账本唯一标识不能为空")
    private Long bookId;

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
