package com.jcwl.admin.category.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分类对象树节点 category
 *
 * @author jcwl
 * @date 2024-10-23
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryTreeSelectVO implements Serializable {
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
    private Long categoryBaseId;

    /**
     * 父级节点唯一标识
     */
    @ApiModelProperty("父级节点唯一标识")
    private Long parentId;

    /**
     * 类型名称
     */
    @ApiModelProperty("类型名称")
    private String name;

    /**
     * 类型编码
     */
    @ApiModelProperty("类型编码")
    private String code;

    /**
     * 分类图标
     */
    @ApiModelProperty("分类图标")
    private String categoryUrl;

    /**
     * 是否有子节点
     */
    @ApiModelProperty("是否有子节点")
    private String hasChild;

    /**
     * 0-收入 1-支出
     */
    @ApiModelProperty("0-收入 1-支出")
    private Integer type;

    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<CategoryTreeSelectVO> children;

}
