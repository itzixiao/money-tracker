package com.jcwl.admin.wechat.service;

import com.jcwl.admin.wechat.dto.UserLoginDTO;
import com.jcwl.admin.wechat.dto.UserLoginVO;
import com.jcwl.admin.wechat.dto.WechatMobileDTO;
import com.jcwl.admin.wechat.dto.WechatOpenIdDTO;

public interface WechatService {

    String getOpenId(WechatOpenIdDTO wechatOpenIdDTO);

    String getMobile(WechatMobileDTO wechatMobileDTO);

    UserLoginVO wechatLogin(WechatOpenIdDTO wechatOpenIdDTO);
}
