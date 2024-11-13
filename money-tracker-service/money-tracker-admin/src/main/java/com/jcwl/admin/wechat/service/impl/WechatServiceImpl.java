package com.jcwl.admin.wechat.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.jcwl.admin.system.domain.SysUserOpen;
import com.jcwl.admin.system.mapper.SysUserOpenMapper;
import com.jcwl.admin.wechat.dto.UserLoginVO;
import com.jcwl.admin.wechat.dto.WechatMobileDTO;
import com.jcwl.admin.wechat.dto.WechatOpenIdDTO;
import com.jcwl.admin.wechat.service.WechatService;
import com.jcwl.common.core.domain.entity.SysUser;
import com.jcwl.common.core.domain.model.LoginUser;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.common.utils.DateUtils;
import com.jcwl.common.utils.SecurityUtils;
import com.jcwl.framework.web.service.SysPermissionService;
import com.jcwl.framework.web.service.TokenService;
import com.jcwl.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private SysUserOpenMapper userOpenMapper;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取微信小程序登录openId
     *
     * @param wechatOpenIdDTO
     */
    @Override
    public String getOpenId(WechatOpenIdDTO wechatOpenIdDTO) {
        WxMaUserService userService = wxMaService.getUserService();
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = userService.getSessionInfo(wechatOpenIdDTO.getJsCode());
        } catch (WxErrorException e) {
            BizPreconditions.thrException("获取微信用户信息失败");
        }
        return sessionInfo.getOpenid();
    }

    /**
     * 微信小程序登录
     *
     * @param wechatOpenIdDTO
     * @return
     */
    @Override
    public UserLoginVO wechatLogin(WechatOpenIdDTO wechatOpenIdDTO) {
        String openId = getOpenId(wechatOpenIdDTO);
        if (wechatOpenIdDTO.getBindAccount()) {
            return bindUser(openId, wechatOpenIdDTO);
        }
        return userLogin(openId);
    }

    private UserLoginVO bindUser(String openId, WechatOpenIdDTO wechatOpenIdDTO) {
        UserLoginVO userLoginVO = new UserLoginVO();
        SysUser sysUser = userService.selectUserByUserName(wechatOpenIdDTO.getUserName());

        SysUserOpen sysUserOpenByUserId = userOpenMapper.selectByUserId(sysUser.getUserId());
        BizPreconditions.ifNotNull(sysUserOpenByUserId, "[" + wechatOpenIdDTO.getUserName() + "]已经与其它账户绑定！");
        LoginUser loginUser = new LoginUser(sysUser.getUserId(), sysUser.getDeptId(), sysUser, permissionService.getMenuPermission(sysUser));
        String token = tokenService.createToken(loginUser);
        SysUserOpen sysUserOpen = userOpenMapper.selectByOpenId(openId);
        if (sysUserOpen == null) {
            sysUserOpen = new SysUserOpen();
            sysUserOpen.setUserId(sysUser.getUserId());
            sysUserOpen.setOpenId(openId);
            userOpenMapper.insert(sysUserOpen);
        }

        buildUserLoginVO(userLoginVO, sysUser, openId, token);
        return userLoginVO;
    }

    public UserLoginVO userLogin(String openId) {
        UserLoginVO userLoginVO = new UserLoginVO();
        SysUserOpen sysUserOpen = userOpenMapper.selectByOpenId(openId);
        Long userId;

        if (sysUserOpen == null) {
            userId = saveUser(openId);
        } else {
            userId = sysUserOpen.getUserId();
        }

        SysUser sysUser = userService.selectUserById(userId);
        LoginUser loginUser = new LoginUser(sysUser.getUserId(), sysUser.getDeptId(), sysUser, permissionService.getMenuPermission(sysUser));
        String token = tokenService.createToken(loginUser);

        buildUserLoginVO(userLoginVO, sysUser, openId, token);
        return userLoginVO;
    }

    public Long saveUser(String openId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(openId);
        sysUser.setNickName("微信用户");
        sysUser.setDeptId(100L);
        sysUser.setPassword(SecurityUtils.encryptPassword(sysUser.getUserName()));
        sysUser.setCreateTime(DateUtils.getNowDate());
        sysUser.setPostIds(new Long[]{2L});
        sysUser.setRoleIds(new Long[]{2L});
        userService.insertUser(sysUser);

        SysUserOpen sysUserOpen = new SysUserOpen();
        sysUserOpen.setUserId(sysUser.getUserId());
        sysUserOpen.setOpenId(openId);
        userOpenMapper.insert(sysUserOpen);

        return sysUser.getUserId();
    }

    private void buildUserLoginVO(UserLoginVO userLoginVO, SysUser sysUser, String openId, String token) {
        userLoginVO.setId(sysUser.getUserId());
        userLoginVO.setOpenid(openId);
        userLoginVO.setToken(token);
        userLoginVO.setGender(Integer.valueOf(sysUser.getSex()));
        userLoginVO.setNickName(sysUser.getNickName());
        userLoginVO.setAvatarUrl(sysUser.getAvatar());
        userLoginVO.setTel(sysUser.getPhonenumber());
    }

    /**
     * 获取微信小程序手机号
     *
     * @param wechatMobileDTO
     * @return
     */
    @Override
    public String getMobile(WechatMobileDTO wechatMobileDTO) {
        WxMaUserService userService = wxMaService.getUserService();
        WxMaPhoneNumberInfo phoneNoInfo = null;
        try {
            phoneNoInfo = userService.getPhoneNoInfo(wechatMobileDTO.getCode());
        } catch (WxErrorException e) {
            BizPreconditions.thrException("获取手机号信息失败");
        }
        return phoneNoInfo.getPhoneNumber();
    }

}
