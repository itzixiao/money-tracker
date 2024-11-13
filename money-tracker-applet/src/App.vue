<template>
  <div>

  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { getToken } from '@/utils/auth';

// 初始化应用
const initApp = () => {
  //#ifdef H5
  checkLogin();
  //#endif
};

// 检查登录状态
const checkLogin = () => {
  if (!getToken()) {
    // 退出登录并跳转到登录页面
    uni.reLaunch({
      url: '/pages/login',
    });
  }
};

// 组件挂载时调用初始化方法
onMounted(() => {
  /*#ifdef APP-PLUS*/
  initApp(); 
  /*#endif*/
  
  /*#ifdef H5*/
  initApp(); 
  /*#endif*/
});
</script>

<style lang="scss">
@import "uview-plus/index.scss";
@import '@/static/scss/index.scss';
.uni-tabbar{
  display: none !important; // 页面加载时隐藏默认TabBar
}
</style>