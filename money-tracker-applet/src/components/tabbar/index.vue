<script setup lang='ts'>
import { ref, defineOptions, onMounted, defineProps } from "vue";

defineOptions({
  name: "TabBar",
});

const props = defineProps({
  currPage: {
    type: Number,
  },
});

const curItem: any = ref(1);
const tabbarList = ref([
  {
    id: 1,
    pagePath: "/pages/index",
    iconPath: "/static/images/tabbar/home.png",
    selectedIconPath: "/static/images/tabbar/home_.png",
    text: "首页",
    centerItem: false,
  },
  {
    id: 2,
    pagePath: "/pages/reportForm",
    iconPath: "/static/images/tabbar/reportForm.png",
    selectedIconPath: "/static/images/tabbar/reportForm_.png",
    text: "报表",
    centerItem: false,
  },
  {
    id: 3,
    pagePath: "/pages/add/index",
    iconPath: "/static/images/tabbar/add.png",
    selectedIconPath: "/static/images/tabbar/add.png",
    text: "",
    centerItem: true,
  },
  {
    id: 4,
    pagePath: "/pages/accountBook",
    iconPath: "/static/images/tabbar/accountBook.png",
    selectedIconPath: "/static/images/tabbar/accountBook_.png",
    text: "账本",
    centerItem: false,
  },
  {
    id: 5,
    pagePath: "/pages/mine",
    iconPath: "/static/images/tabbar/mine.png",
    selectedIconPath: "/static/images/tabbar/mine_.png",
    text: "我的",
    centerItem: false,
  },
]);

onMounted(() => {
  curItem.value = props.currPage;
  uni.hideTabBar();
});

const changeItem = (item: any) => {
  if (item.id == 3) {
    uni.navigateTo({
      url: "/pages/add/index",
    });
  }
  uni.switchTab({ url: item.pagePath });
};

const updateFile = (val: any) => {
  if (val.id == 3) {
    uni.navigateTo({url:'/pages/updateBill'})
  }
};

</script>
<template>
  <view class="container">
    <view
      class="tabbar-item"
      :class="[item.centerItem ? ' center-item' : '']"
      :style="'width: calc(100% /' + tabbarList.length + ')'"
      @click="changeItem(item)"
      v-for="(item, i) in tabbarList"
      :key="i"
      @longpress="updateFile(item)"
    >
      <view class="item-top">
        <image
          :src="curItem === item.id ? item.selectedIconPath : item.iconPath"
        />
      </view>
      <view
        class="item-bottom"
        :class="[curItem === item.id ? 'item-active' : '']"
        >{{ item.text }}</view
      >
    </view>
  </view>
</template>


<style lang="scss" scoped>
$textDefaultColor: #9398a6; // 文字默认颜色
$bottomBg: #ffffff; // 底部背景
$textSelectedColor: #fd8702; // 文字选中颜色
$centerItemBg: #fff; // 中间凸起按钮背景

.container {
  position: fixed;
  bottom: 0;
  left: 0;
  display: flex;
  align-items: center;
  width: 100%;
  height: 110rpx;
  color: $textDefaultColor;
  padding: 5rpx 0;
  background-color: $bottomBg;
  box-shadow: 0 0 10rpx #999;
}

.tabbar-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  height: 100rpx;

  .item-top {
    flex-shrink: 0;
    width: 65rpx;
    height: 65rpx;
    padding: 4rpx;

    image {
      width: 100%;
      height: 100%;
    }
  }

  .item-bottom {
    width: 100%;
    font-size: 28rpx;
  }

  .item-active {
    color: $textSelectedColor;
  }
}

.center-item {
  position: relative;

  .item-top {
    position: absolute;
    top: -55rpx;
    left: 50%;
    transform: translateX(-50%);
    width: 110rpx;
    height: 110rpx;
    background-color: $centerItemBg;
    border-radius: 10rpx;
  }

  .item-bottom {
    position: absolute;
    bottom: 5rpx;
  }
}
</style>