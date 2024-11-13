<template>
  <view class="setting-container" :style="{ height: `${windowHeight}px` }">
    <view class="menu-list">
      <view class="list-cell list-cell-arrow" @click="handleToPwd">
        <view class="menu-item-box">
          <view class="iconfont icon-password menu-icon"></view>
          <view>修改密码</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleToUpgrade">
        <view class="menu-item-box">
          <view class="iconfont icon-refresh menu-icon"></view>
          <view>检查更新</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleCleanTmp">
        <view class="menu-item-box">
          <view class="iconfont icon-clean menu-icon"></view>
          <view>清理缓存</view>
        </view>
      </view>
      <!-- #ifdef APP-PLUS || H5 -->
      <view class="list-cell" @click="uploadApk" v-if="env == 'development'">
        <view class="menu-item-box">
          <view class="iconfont icon-github menu-icon"></view>
          <view>上传新包(开发)----{{ env }}</view>
        </view>
      </view>
      <!-- #endif -->
    </view>
    <view class="cu-list menu">
      <view class="cu-item item-box">
        <view class="content text-center" @click="handleLogout">
          <text class="text-black">退出登录</text>
        </view>
      </view>
    </view>
  </view>
  <view>
    <uni-popup ref="popup" type="dialog">
      <uni-popup-dialog type="info" cancelText="关闭" confirmText="退出" title="通知" content="确定注销并退出系统吗"
        @confirm="dialogConfirm" @close="dialogClose">
      </uni-popup-dialog>
    </uni-popup>
  </view>
</template>
<script>

</script>
<script setup>
import { ref, getCurrentInstance, onMounted, onUnmounted } from "vue";
import app_upgrade from '@/uni_modules/app-upgrade/js_sdk/index.js'
import { searchVersion } from '@/api/system/upload.js'
import config from '@/config'
import update from '@/utils/update.js';
import useUserStore from '@/store/modules/user'
import { getToken } from '@/utils/auth'
const userStore = useUserStore()

const windowHeight = ref(uni.getSystemInfoSync().windowHeight);
const popup = ref(null);

const instance = getCurrentInstance();
const version = instance.appContext.config.globalProperties.$getVersion(); // 访问全局属性
const baseUrl = config.baseUrl;
const env = process.env.NODE_ENV;


onMounted(() => {
  console.log('组件已挂载');
  console.log(version, '1123')
});

onUnmounted(() => {
  console.log('组件已卸载');
});

function handleToPwd() {
  uni.navigateTo({
    url: '/pages/mine/pwd/index'
  });
};

function handleToUpgrade() {
  searchVersion({
    fileName: config.appInfo.name,
    // version: version
  }).then(res => {
    if (res.data.version && res.data.version != null && update.compareVersions(res.data.version, version) > 0) {
      try {
        uni.setStorageSync('totalSize', res.data.size);
        let download = `${baseUrl}/minio/download/${res.data.minioFileName}`
        app_upgrade(async (versionCode) => {
          return {
            changelog: `更新内容：${res.data.fixContent ? res.data.fixContent : '我们对应用进行了改进，以提升整体性能和稳定性，为您提供更流畅的使用体验。'}`,
            status: 1, // 0 无新版本 | 1 有新版本
            path: download // 新apk地址
          }
        }, 1)
      } catch (err) {
        console.log(err)
      }
    } else {

      uni.showToast({
        title: '已经是最新版本',
        icon: 'none',
      });

    }
  })
}

// 上传新包
function uploadApk(event) {
  uni.chooseFile({
    count: 1,
    extension: ['apk'],
    success: (res) => {
      let url = `${baseUrl}/version/updates/upload/version`;
      const file = res.tempFiles[0];
      let versions = {
        version,
        fileName: config.appInfo.name,
        size: file.size,
        fixContent: '更新了检查更新功能。'
      };
      let data = {
        versionUpdates: JSON.stringify(versions),
      };
      uni.showLoading({
        title: '上传中...',
      });
      update.$uploadForm(url, data, file.path, getToken()).then(res => { // 传递 file.path
        uni.showToast({ title: '上传成功', icon: 'success' });
      }).catch((err) => {
        uni.showToast({ title: '上传失败', icon: 'none' });
      });
    },
    fail: (err) => {
      console.log('选择文件失败', err);
    }
  });
};

function handleCleanTmp() {
  uni.showToast({
    title: '模块建设中~',
    mask: false,
    icon: "none",
    duration: 1000
  });
};
function handleLogout() {
  uni.showModal({
    title: '通知',
    content: `确定注销并退出系统吗？`,
    success: (res) => {
      console.log(res)
      if (res.confirm) {
        userStore.logOut().then(() => {
          let url = ''
          /*#ifdef APP-PLUS*/
          url = '/pages/login'
          /*#endif*/
          /*#ifdef H5*/
          url = '/pages/login'
          /*#endif*/
          /*#ifdef MP-WEIXIN*/
          url = '/pages/mine'
          /*#endif*/

          uni.reLaunch({
            url: url
          });
        })
        uni.clearStorageSync();
      }
    }
  })
};
</script>

<style lang="scss" scoped>
.page {
  background-color: #f8f8f8;
}

.item-box {
  background-color: #FFFFFF;
  margin: 30rpx;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 10rpx;
  border-radius: 8rpx;
  color: #303133;
  font-size: 32rpx;
}
</style>
