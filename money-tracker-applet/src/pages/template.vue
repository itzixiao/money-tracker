<template>
    <view class="wrap">
    </view>
  </template>
  
  <script>
  import list from "./template.config.js";
  export default {
    data() {
      return {
        list: list,
        // desc: '收集众多的常用页面和布局，减少开发者的重复工作，让你专注逻辑，事半功倍'
      }
    },
    computed: {
      getIcon() {
        return path => {
          return '../static/uview/demo/' + path + '.png';
          return 'https://cdn.uviewui.com/uview/example/' + path + '.png';
        }
      },
    },
    methods: {
      openPage(path) {
        this.$u.route({
          url: path
        })
      },
      getGroupTitle(item) {
        return item.groupName
      },
      getFieldTitle(item) {
        return item.title
      }
    }
  }
  </script>
  
  <style>
  /* page {
          background-color: rgb(240, 242, 244);
      } */
  </style>
  
  <style lang="scss" scoped>
  .u-cell-icon {
    width: 36rpx;
    height: 36rpx;
    margin-right: 8rpx;
  }
  </style>
  