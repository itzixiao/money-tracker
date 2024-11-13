package com.jcwl.admin.wechat.controller;

import com.jcwl.admin.wechat.dto.UserLoginVO;
import com.jcwl.admin.wechat.dto.WechatMobileDTO;
import com.jcwl.admin.wechat.dto.WechatOpenIdDTO;
import com.jcwl.admin.wechat.service.WechatService;
import com.jcwl.common.annotation.Anonymous;
import com.jcwl.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "小程序管理")
@RestController
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @ApiOperation(value = "获取微信小程序登录openId")
    @PostMapping("/getOpenId")
    public R<String> getOpenId(@Validated @RequestBody WechatOpenIdDTO wechatOpenIdDTO) {
        String openId = wechatService.getOpenId(wechatOpenIdDTO);
        return R.ok(openId);
    }

    @ApiOperation(value = "获取微信小程序手机号")
    @PostMapping("/getMobile")
    public R<String> getMobile(@Validated @RequestBody WechatMobileDTO wechatMobileDTO) {
        String mobile = wechatService.getMobile(wechatMobileDTO);
        return R.ok(mobile);
    }

    /**
     * 微信用户登录
     *
     * @param wechatOpenIdDTO
     * @return
     */
    @Anonymous
    @ApiOperation(value = "微信用户登录")
    @PostMapping("/login")
    public R<UserLoginVO> login(@Validated @RequestBody WechatOpenIdDTO wechatOpenIdDTO) {
        return R.ok(wechatService.wechatLogin(wechatOpenIdDTO));
    }
}
