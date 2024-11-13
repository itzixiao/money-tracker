package com.jcwl.admin.account.dto;

import com.jcwl.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 账户对象 book_account
 *
 * @author jcwl
 * @date 2024-10-24
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountQueryDTO extends PageDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 导出文件名
     */
    @ApiModelProperty("导出文件名")
    private String exportFileName;


    /**
     * 基础账户唯一标识
     */
    @ApiModelProperty("基础账户唯一标识")
    private Long accountBaseId;

    /**
     * 父级节点唯一标识
     */
    @ApiModelProperty("父级节点唯一标识")
    private Long parentId;

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String name;

    /**
     * 账户编码
     */
    @ApiModelProperty("账户编码")
    private String code;

    /**
     * 账户图标
     */
    @ApiModelProperty("账户图标")
    private String accountUrl;

    /**
     * 是否有子节点
     */
    @ApiModelProperty("是否有子节点")
    private String hasChild;

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
