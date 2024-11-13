package com.jcwl.admin.book.dto;

import com.jcwl.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 账本对象 book
 *
 * @author jcwl
 * @date 2024-10-18
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookQueryDTO extends PageDomain{
    private static final long serialVersionUID = 1L;

    /**
     * 导出文件名
     */
    @ApiModelProperty("导出文件名")
    private String exportFileName;


    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    private Long userId;

    /**
     * 账本名称
     */
    @ApiModelProperty("账本名称")
    private String bookName;

    /**
     * 账本图片
     */
    @ApiModelProperty("账本图片")
    private String bookAvatar;

    /**
     * 成员加入账本是否需要审核（0-不需要 1-需要管理员审核）
     */
    @ApiModelProperty("成员加入账本是否需要审核（0-不需要 1-需要管理员审核）")
    private Long userAudit;

    /**
     * 是否允许对外搜索私密账本（0-私密不允许 1-公开允许）
     */
    @ApiModelProperty("是否允许对外搜索私密账本（0-私密不允许 1-公开允许）")
    private Long showSearch;

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
     * 账本唯一标识数组
     */
    @ApiModelProperty("账本唯一标识数组")
    private List<Long> bookIds;
}
