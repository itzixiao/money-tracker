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
public class WechatMobileDTO implements Serializable {

    @NotBlank(message = "手机号获取凭证不能为空")
    @ApiModelProperty(value = "手机号获取凭证", required = true)
    private String code;

}
