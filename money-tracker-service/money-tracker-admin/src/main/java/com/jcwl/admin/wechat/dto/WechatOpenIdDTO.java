package com.jcwl.admin.wechat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatOpenIdDTO implements Serializable {

    @NotBlank(message = "jsCode不能为空")
    @ApiModelProperty(value = "登录时获取的 code，可通过wx.login获取")
    private String jsCode;

    @ApiModelProperty(value = "微信登录时手机号")
    private String phone;

    @ApiModelProperty("是否绑定用户")
    private Boolean bindAccount = Boolean.FALSE;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("用户密码")
    private String password;
}
