<template>
  <div id="login_warp">
    <div id="top">
      <image src='@/static/images/banner/banner.png' id="imgs"></image>
      <div id="top_ionf">
        <image src="@/static/images/accountBook/accountbook.png"></image>
        <div>记账小帮手</div>
      </div>
    </div>
    <view class="normal-login-container">
      <view class="login-form-content">
        <view class="input-item flex align-center">
          <view class="iconfont icon-user icon"></view>
          <input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
        </view>
        <view class="input-item flex align-center">
          <view class="iconfont icon-password icon"></view>
          <input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码" maxlength="20" />
        </view>
        <view class="input-item flex align-center" style="width: 60%;margin: 0px;" v-if="captchaEnabled">
          <view class="iconfont icon-code icon"></view>
          <input v-model="loginForm.code" type="number" class="input" placeholder="请输入验证码" maxlength="4" />
          <view class="login-code">
            <image :src="codeUrl" @click="getCode" class="login-code-img"></image>
          </view>
        </view>
        <view class="action-btn">
          <button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">登录</button>
        </view>
      </view>
      <div id="register">
        <div @click="toRegister">注册新账户</div>
        <div>忘记登陆密码</div>
      </div>
      <!-- <view class="xieyi text-center">
        <text class="text-grey1">登录即代表同意</text>
        <text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
        <text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
      </view> -->
    </view>
  </div>
</template>

<script setup>
import modal from '@/plugins/modal'
import { getCodeImg } from '@/api/login'
import { ref } from "vue";
import config from '@/config.js'
import useUserStore from '@/store/modules/user'
import { getWxCode } from '@/utils/geek';
import { wxLogin } from '@/api/oauth';
import { setToken } from '@/utils/auth';
const userStore = useUserStore()
const codeUrl = ref("");
const captchaEnabled = ref(true); // 是否开启验证码
const useWxLogin = ref(false); // 是否使用微信登录
const globalConfig = ref(config);
const loginForm = ref({
  username: "admin",
  password: "admin123",
  code: "",
  uuid: ''
});

if (useWxLogin.value) {
  getWxCode().then(res => {
    wxLogin('miniapp', res).then(res => {
      if (res.token != null) {
        setToken(res.token);
        loginSuccess()
      }
    });
  })
}


// 获取图形验证码
function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img
      loginForm.value.uuid = res.uuid
    }
  })
};

async function handleLogin() {
  if (loginForm.value.username === "") {
    modal.msgError("请输入您的账号")
  } else if (loginForm.value.password === "") {
    modal.msgError("请输入您的密码")
  } else if (loginForm.value.code === "" && captchaEnabled.value) {
    modal.msgError("请输入验证码")
  } else {
    modal.loading("登录中，请耐心等待...")
    pwdLogin()
  }
};
// 密码登录
async function pwdLogin() {
  userStore.login(loginForm.value).then(() => {
    modal.closeLoading()
    loginSuccess()
  }).catch(() => {
    if (captchaEnabled.value) {
      modal.closeLoading()
      getCode()
    }
  })
};

function loginSuccess(result) {
  // 设置用户信息
  userStore.getInfo().then(res => {
    uni.switchTab({
      url: '/pages/index'
    });
  })
}

// 隐私协议
function handlePrivacy() {
  let site = globalConfig.value.appInfo.agreements[0];
  uni.navigateTo({
    url: `/pages/common/webview/index?title=${site.title}&url=${site.url}`
  });
};
// 用户协议
function handleUserAgrement() {
  let site = globalConfig.value.appInfo.agreements[1]
  uni.navigateTo({
    url: `/pages/common/webview/index?title=${site.title}&url=${site.url}`
  });
};

getCode();

const toRegister = () => {
  uni.navigateTo({
    url: '/pages/register'
  });
}
</script>

<style scoped lang='scss'>
#login_warp {
  width: 100%;
  height: 100vh;
  background: #fff;
  overflow: hidden;

  #top {
    height: 40vh;
    color: #fff;
    font-size: 36rpx;
    font-weight: 700;
    position: relative;

    #imgs,
    #top_ionf {
      width: 100%;
      height: 100%;
      position: absolute;
      left: 0;
      top: 0;
    }

    #top_ionf {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      image {
        width: 150rpx;
        height: 150rpx;
        margin-bottom: 30rpx;
      }
    }
  }
}

.normal-login-container {
  width: 100%;
  height: 60vh;
  display: flex;
  flex-direction: column;

  #register {
    padding: 0 150rpx;
    box-sizing: border-box;
    display: flex;
    color: #FD8702;
    justify-content: space-between;
  }

  .login-form-content {
    text-align: center;
    margin: 40rpx auto;
    width: 80%;

    .input-item {
      margin: 40rpx auto;

      height: 90rpx;
      border: 1rpx solid #ccc;
      border-radius: 10rpx;

      .icon {
        font-size: 38rpx;
        margin-left: 10px;
        color: #999;
      }

      .input {
        width: 100%;
        font-size: 14px;
        line-height: 20px;
        text-align: left;
        padding-left: 15px;
      }

    }

    .login-btn {
      height: 90rpx;
      background: #FD8702;
    }

    .xieyi {
      color: #333;
    }

    .login-code {
      height: 76rpx;
      float: right;

      .login-code-img {
        height: 76rpx;
        position: absolute;
        margin-left: 20rpx;
        width: 200rpx;
      }
    }
  }
}
</style>
