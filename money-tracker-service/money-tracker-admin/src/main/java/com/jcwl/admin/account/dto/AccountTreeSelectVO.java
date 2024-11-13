package com.jcwl.admin.account.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 账户对象树节点 category
 *
 * @author jcwl
 * @date 2024-10-23
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountTreeSelectVO implements Serializable {
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
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<AccountTreeSelectVO> children;

}
