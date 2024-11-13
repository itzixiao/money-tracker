package com.jcwl.admin.category.dto;

import com.jcwl.common.core.page.PageDomain;
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
public class CategoryQueryDTO extends PageDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 导出文件名
     */
    @ApiModelProperty("导出文件名")
    private String exportFileName;


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
