<template>
  <view class="mine-container" :style="{ height: `${windowHeight}px` }">
    <!--顶部个人信息栏-->
    <view class="header-section" :key="timer">
      <view class="flex padding justify-between">
        <view class="flex align-center">
          <view v-if="!userStore.avatar"  @click="handleToAvatar" class="cu-avatar xl round bg-white">
            <view class="iconfont icon-people text-gray icon"></view>
          </view>
          <image v-if="userStore.avatar" @click="handleToAvatar" :src="previewPictures(userStore.avatar)" class="cu-avatar xl round"
            mode="widthFix">
          </image>
          <view v-if="!userStore.name" @click="handleToLogin" class="login-tip">
            点击登录
          </view>
          <view v-if="userStore.name" @click="handleToInfo" class="user-info">
            <view class="u_title">
              用户名：{{ userStore.name }}
            </view>
          </view>
        </view>
        <view @click="handleToInfo" class="flex align-center">
          <text>个人信息</text>
          <view class="iconfont icon-right"></view>
        </view>
      </view>
    </view>
    <view class="content-section">
      <view class="mine-actions grid col-4 text-center">
        <view class="action-item" @click="handleJiaoLiuQun">
          <view class="iconfont icon-friendfill text-pink icon"></view>
          <text class="text">交流群</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-service text-blue icon"></view>
          <text class="text">在线客服</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-community text-mauve icon"></view>
          <text class="text">反馈社区</text>
        </view>
        <view class="action-item" @click="handleBuilding">
          <view class="iconfont icon-dianzan text-green icon"></view>
          <text class="text">点赞我们</text>
        </view>
      </view>

      <view class="menu-list">
        <view class="list-cell list-cell-arrow" @click="handleHelp">
          <view class="menu-item-box">
            <view class="iconfont icon-help menu-icon"></view>
            <view>常见问题</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleAbout">
          <view class="menu-item-box">
            <view class="iconfont icon-aixin menu-icon"></view>
            <view>关于我们</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow" @click="handleToSetting">
          <view class="menu-item-box">
            <view class="iconfont icon-setting menu-icon"></view>
            <view>应用设置</view>
          </view>
        </view>
      </view>

    </view>
    <!-- 登录 -->
    <up-popup :show="loginPopup" :round="15" mode="bottom" @close="handleCancel">
      <view id="login_popup">
        <view id="login_title">用户登录</view>
        <view id="login_main">
          <view>为了防止数据丢失，建议您登录</view>
          <view>
            <up-button icon="weixin-fill" shape="circle" color="#05c160" class="login-btn" type="primary" text="微信一键登录"
              @click="wxLogin()">
            </up-button>
          </view>
          <view @click="Login()">
            <up-button shape="circle" color="#FD8702" type="primary" :plain="true" text="账号登录"></up-button>
          </view>
        </view>
        <view class="login_agreement">我已阅度并同意</view>
      </view>
    </up-popup>
  </view>
  <TabBar :currPage="5" />
</template>
<script setup>
import { ref, onMounted } from "vue";
import { onShow } from "@dcloudio/uni-app";
import { getToken } from '@/utils/auth';
import { loginPopup, wxLogin, getAvatarUrl, Login, timer } from '@/utils/weChatQuickLogin.ts'
import { previewPictures } from '@/utils/ruoyi'
import useUserStore from '@/store/modules/user'

const userStore = useUserStore()
const windowHeight = ref(uni.getSystemInfoSync().windowHeight - 50);
const popup = ref(null);

getAvatarUrl()

onShow(async () => {
  /*#ifdef MP-WEIXIN*/
  if (!getToken()) {
    handleToLogin()
  }
  /*#endif*/
})

uni.$on('refresh', () => {
  getAvatarUrl()
})

function handleToInfo() {
  uni.navigateTo({
    url: '/pages/mine/info/index'
  });
};

function handleToSetting() {
  uni.navigateTo({
    url: '/pages/mine/setting/index'
  });
};
function handleToLogin() {
  /*#ifdef H5*/
  uni.reLaunch({
    url: '/pages/mine/login'
  });
  /*#endif*/
  /*#ifdef APP-PLUS*/
  uni.reLaunch({
    url: '/pages/mine/login'
  });
  /*#endif*/

  /*#ifdef MP-WEIXIN*/
  loginPopup.value = true
  /*#endif*/

};
function handleToAvatar() {
  uni.navigateTo({
    url: '/pages/mine/avatar/index'
  });
};
function handleLogout() {
  popup.value.open();
};
function dialogConfirm() {
  //console.log('----------------点击确认------------')

  userStore.logOut().then(() => {
    uni.reLaunch({
      url: '/pages/mine/login'
    });
  })
};
function dialogClose() {
  //console.log('点击关闭')
};
function handleHelp() {
  uni.navigateTo({
    url: '/pages/mine/help/index'
  });
};
function handleAbout() {
  uni.navigateTo({
    url: '/pages/mine/about/index'
  });
};
function handleJiaoLiuQun() {
  uni.showToast({
    title: 'QQ群：679240265',
    mask: false,
    icon: "none",
    duration: 1000
  });
};
function handleBuilding() {
  uni.showToast({
    title: '模块建设中~',
    mask: false,
    icon: "none",
    duration: 1000
  });
}

const handleCancel = () => {
  loginPopup.value = false;
};

onMounted(() => {

});

</script>

<style lang="scss" scoped>
page {
  background-color: #f5f6f7;
}

.mine-container {
  width: 100%;
  height: 100%;


  .header-section {
    padding: 15px 15px 45px 15px;
    background-color: #FD8702;
    color: white;

    .login-tip {
      font-size: 18px;
      margin-left: 10px;
    }

    .cu-avatar {
      border: 2px solid #eaeaea;

      .icon {
        font-size: 40px;
      }
    }

    .user-info {
      margin-left: 15px;

      .u_title {
        font-size: 18px;
        line-height: 30px;
      }
    }
  }

  .content-section {
    position: relative;
    top: -50px;

    .mine-actions {
      margin: 15px 15px;
      padding: 20px 0px;
      border-radius: 8px;
      background-color: white;

      .action-item {
        .icon {
          font-size: 28px;
        }

        .text {
          display: block;
          font-size: 13px;
          margin: 8px 0px;
        }
      }
    }
  }
}

// 小程序快捷登录
#login_popup {
  width: 100%;
  height: 40vh;
  padding: 40rpx 70rpx;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  #login_title {
    text-align: center;
    font-size: 30rpx;
    font-weight: 700;
  }

  .login-btn {
    color: white;
  }

  #login_main {
    height: 19vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    >view:first-child {
      color: #777;
      font-size: 24rpx;
    }

    >view {
      margin-bottom: 10rpx;
    }
  }

  .login_agreement {
    font-size: 22rpx !important;
    color: #888;
  }
}
</style>
