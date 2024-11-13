package com.jcwl.admin.wechat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录，响应实体
 *
 * @author jcwl
 * @date 2024-10-15
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = -3345054919691215954L;

    /**
     * 用户登录凭证
     */
    private String code;

    /**
     * 不包括敏感信息的原始数据字符串，用于计算签名
     */
    private String rawData;

    /**
     * 使用 sha1(rawData + sessionkey) 得到字符串，用于校验用户信息
     */
    private String signature;

    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;

    /**
     * 加密算法的初始向量
     */
    private String iv;

    /**
     * openid
     */
    @JsonIgnore
    private String openid;

}
