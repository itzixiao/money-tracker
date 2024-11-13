<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'person-filled' }" title="昵称"
        :rightText="user?.nickName" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'phone-filled' }" title="手机号码"
        :rightText="user?.phonenumber" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'email-filled' }" title="邮箱" :rightText="user?.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'auth-filled' }" title="岗位" :rightText="postGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'staff-filled' }" title="角色" :rightText="roleGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'calendar-filled' }" title="创建日期"
        :rightText="user?.createTime" />
    </uni-list>

    <u-button @click="register()">绑定微信</u-button>
    <u-button @click="changeInfo()">修改信息</u-button>
  </view>
</template>

<script setup>
import { getUserProfile } from "@/api/system/user"
import { ref } from "vue";
import modal from "@/plugins/modal"
import { onShow } from "@dcloudio/uni-app";

const user = ref({})
const roleGroup = ref("")
const postGroup = ref("")

onShow(() => {
  getUser()
})
function getUser() {
  getUserProfile().then(response => {
    user.value = response.data
    roleGroup.value = response.roleGroup
    postGroup.value = response.postGroup
  })
}

import { wxRegister, wechatLogin } from "@/api/oauth"
import { getWxCode } from "@/utils/geek"
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
import { setToken } from '@/utils/auth';

// modal.loading('绑定微信中...')
// getWxCode().then(res=>{
//   wxRegister('miniapp',res).then(res=>{
//     modal.closeLoading()
//   })
// })

function register() {
  modal.loading('绑定微信中...')
  uni.login({
    provider: 'weixin',
    success: (res) => {
      if (res.code) {
        wechatLogin({
          "jsCode": res.code,
          "phone": ""
        }).then(res => {
          if (res.data.token != null) {
            setToken(res.data.token);
            modal.msgSuccess('绑定成功')
            modal.closeLoading();
            uni.getUserInfo({
              provider: 'weixin',
              success: (userInfoRes) => {
                const userInfo = userInfoRes.userInfo; // 获取用户信息
                userStore.setAvatar(userInfo.avatarUrl);  // 设置头像
                userStore.setName(userInfo.nickName);      // 设置名称
                uni.setStorageSync('name', userInfo.nickName)
                uni.setStorageSync('avatar', userInfo.avatarUrl)
                uni.switchTab({
                  url: '/pages/mine/mine'
                });
                uni.$emit('refresh');
              },
              fail: (err) => {
                console.error('获取用户信息失败:', err);
              }
            });
          }

        });
      } else {
        console.error('登录失败！' + res.errMsg);
      }
    },
    fail: (err) => {
      console.error('uni.login 调用失败', err);
    }
  });
}

function changeInfo() {
  uni.navigateTo({url: '/pages/mine/info/edit'})
}

</script>

<style lang="scss">
page {
  background-color: #ffffff;
}
</style>
