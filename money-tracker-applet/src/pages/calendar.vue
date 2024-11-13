<script setup lang='ts'>
import { onMounted, ref } from "vue";
import { getCalendarList } from "@/api/home";
import { onShow } from "@dcloudio/uni-app";
import TopNav from '@/components/tabbar/topNav.vue'
import { fenToYuan, yuanToFen } from "@/utils/conversion";

defineOptions({
  name: "Calendar",
});

const selected = ref([]);
const bookId = ref();
const bookList: any = ref([]);

onShow(() => {
  bookList.value = uni.getStorageSync("bookList");
  bookId.value = uni.getStorageSync("bookId");
  getList()
});

const getList = () => {
  getCalendarList({bookId: bookId.value}).then((res) => {
    if(res.code == 200) {
      selected.value = res.data.map(item => {
        item.date = item.bookTime
        item.info = item.expenditure == 0 ? 0 : fenToYuan(item.expenditure)
        item.infoColor = '#4a7f4a'
        item.topInfo = item.income == 0 ? 0 : '+' + fenToYuan(item.income)
        item.topInfoColor = '#f15252'
        return item
      })
    }
  });
};

const calendarChange = (e) => {
  uni.setStorageSync('calendar', e)
  uni.switchTab({
    url: '/pages/index',
  })
};
const bookChange = (e) => {
  uni.setStorageSync('calendar', e)
}
</script>
<template>
  <view>
    <view class="top-part">
      <TopNav :bookList="bookList" :bookId="bookId" :showCalendar="true"  @bookChange="bookChange" />
    </view>
    <wu-calendar
      :insert="true"
      :selected="selected"
      :color="'#e79d0f'"
      slideSwitchMode="vertical"
      @change="calendarChange"
    ></wu-calendar>
  </view>
</template>

<style scoped lang='scss'>
.calendar {
  :deep(.u-popup__content__close--top-right) {
    display: none;
  }
  :deep(uni-scroll-view) {
    height: 80vh !important;
  }
}

.top-part {
  background: #e79d0f;
  padding: 1vh 0;
}
</style>