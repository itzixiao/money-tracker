package com.jcwl.admin.account.dto;

import com.jcwl.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账户基础对象 account_base
 *
 * @author jcwl
 * @date 2024-10-24
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountBaseQueryDTO extends PageDomain {
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
