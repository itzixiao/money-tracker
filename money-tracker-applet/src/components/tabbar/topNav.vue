<script setup lang='ts'>
import {
  ref,
  onMounted,
  defineOptions,
  defineProps,
  defineEmits,
  watch,
} from "vue";
import useUserStore from "@/store/modules/user";
import { onLoad } from "@dcloudio/uni-app";

defineOptions({
  name: "TopNav",
});

const props = defineProps({
  bookList: {
    type: Array,
  },
  bookId: {
    type: Number,
  },
  showCalendar: {
    type: Boolean,
    required: false
  },
});

const userStore = useUserStore();
const bookIds = ref();
const emits = defineEmits<{
  bookChange: [value: any];
}>();

const bookChange = (e: any) => {
  uni.setStorageSync('bookId', e)
  emits("bookChange", e);
  userStore.$patch({ bookId: e });
};

onLoad(() => {
  bookIds.value = props.bookId
})

const toCalendar = () => {
  uni.navigateTo({url: '/pages/calendar'})
}

watch(
  () => props.bookId,
  (val) => {
    if (val !== null) {
      bookIds.value = val
    }
  }
);
</script>
<template>
  <view class="top-nav">
    <view>当前账本：</view>
    <uni-data-select
      style="width: calc(100% - 160rpx)"
      v-model="bookIds"
      :localdata="props.bookList"
      :clear="false"
      :titleColor="'red'"
      @change="bookChange"
    >
    </uni-data-select>
    <up-icon name="calendar" color="#fff" size="28" v-if="props.showCalendar" @click="toCalendar"></up-icon>
  </view>
</template>

<style scoped lang='scss'>
.top-nav {
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 8vw;
  box-sizing: border-box;
  >view:first-child{
    font-size: 28rpx;
    color: #fff;
    width: 150rpx;
  }
  :deep(.uni-select) {
    border: 1px solid #fff;
    .uni-select__input-text,
    .uni-icons {
      color: #fff !important;
    }

    .uni-scroll-view {
      color: #111;
    }

    .uni-scroll-view {
      height: 20vh;
    }
  }
}
</style>