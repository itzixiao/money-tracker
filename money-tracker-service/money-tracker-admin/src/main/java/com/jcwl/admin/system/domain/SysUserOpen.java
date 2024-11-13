package com.jcwl.admin.system.domain;

import com.jcwl.common.core.domain.JcwlBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和第三方登录关联对象 sys_user_open
 *
 * @author jcwl
 * @date 2024-10-15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserOpen extends JcwlBaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 第三方openId
     */
    @ApiModelProperty("第三方openId")
    private String  openId;

}
