package com.jcwl.admin.category.dto;

import com.jcwl.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类基础对象 category_base
 *
 * @author jcwl
 * @date 2024-10-23
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryBaseQueryDTO extends PageDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 导出文件名
     */
    @ApiModelProperty("导出文件名")
    private String exportFileName;


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
