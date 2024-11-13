<script setup lang='ts'>
import { reactive, ref, onMounted } from 'vue';
import foundation from './components/reportForm/foundation/foundation.vue';
import classification from './components/reportForm/classification/classification.vue';
import account from './components/reportForm/account/account.vue';
import member from './components/reportForm/member/member.vue';
import { parseTime } from '@/utils/ruoyi'
import useUserStore from '@/store/modules/user'
import TopNav from "@/components/tabbar/topNav.vue";
import { onLoad, onShow } from '@dcloudio/uni-app';

const userStore = useUserStore()
const lineBg = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAOCAYAAABdC15GAAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFxSURBVHgBzZNRTsJAEIb/WTW+lpiY+FZPIDew3ABP4GJ8hxsI9zBpOYHeQDwBPQI+mRiRvpLojtPdYhCorQqF/6GdbGd2vvwzBXZcNAt4oj1ANeUoAT5iqkUjbEFLHNmhD1YPEvpZ3ghkGlVDCkc94/BmHMq998I5ONiY1ZBfpKAyuOtgAc5yOEDmYEWNh32BHF91sGHZHmwW4azciN9aQwnz3SJEgOmte+R2tdLprTYoa50mvuomlLpD4Y3oQZnov6D2RzCqI93bWOHaEmAGqQUyRBlZR1WfarcD/EJ2z8DtzDGvsMCwpm8XOCfDUsVOCYhiqRxI/CTQo4UOvjzO7Pow18vfywneuUHHUUxLn55lLw5JFpZ8bEUcY8oXdOLWiHLTxvoGpLqoUmy6dBT15o/ox3znpoycAmxUsiJTbs1cmxeVKp+0zmFIS7bGWiVghC7Vwse8jFKAX9eljh4ggKLLv7uaQvG9/F59Oo2SouxPu7OTCxN/s8wAAAAASUVORK5CYII=";
const tabList = reactive([
  { name: '基础统计' },
  { name: '分类' },
  { name: '账户' },
  { name: '成员' },
]);
const userId = uni.getStorageSync('userId');
const showMonth = ref<boolean>(false); // 控制弹窗显示的变量
const showYear = ref<boolean>(false); // 控制弹窗显示的变量
const logoType = ref<any>(2); // 默认选中月份
const bookTime = ref<any>('2024'); // 选中
const selectedMonth = ref<any>((Date.now())); // 选中的月份
const selectedYear = ref<string>(''); // 选中的年份
const currentYear = new Date().getFullYear();
const yearRange = [Array.from({ length: 25 }, (_, i) => currentYear - i).map(year => year.toString())];
const tabIndex = ref(0)
const bookList: any = ref([]) // 账本列表
const bookId = ref() // 账本id
const timer = ref(0)
const handleDate = () => {
  if (logoType.value == 2) {
    showYear.value = true
  } else {
    showMonth.value = true
  }
}
const cancel = () => {
  if (logoType.value == 2) {
    showYear.value = false
  } else {
    showMonth.value = false
  }
}
const confirm = (e: any) => {
  if (logoType.value == 2) {
    bookTime.value = e.value[0]
    showYear.value = false
  } else {
    bookTime.value = parseTime(e.value, '{y}-{m}');
    showMonth.value = false
  }
  timer.value++
}

function tabClick(item: any) {
  tabIndex.value = item.index
}
const changeActived = (id: any) => {
  if(logoType.value == id) return
  const currentMonth = new Date().getMonth() + 1;
  if (id == 2) {
    bookTime.value = parseTime(bookTime.value, '{y}');
  } else {
    bookTime.value = `${bookTime.value}-${String(currentMonth).padStart(2, '0')}`;
  }
  logoType.value = id;
  timer.value++;
}


const bookChange = (e) => {
  bookId.value = e
  timer.value++
}

onShow(async() => {
  userStore.getBookForm()
  bookList.value = uni.getStorageSync('bookList')
  bookId.value = uni.getStorageSync('bookId')
  getBook()
});

const getBook = async () => {
  if (bookList.value.length > 0) {
    timer.value++
  } else {

  }
};

</script>
<template>
  <view style="background:#FD8702;padding: 15rpx 0;">
    <TopNav :bookList="bookList" :bookId="bookId" @bookChange="bookChange" />
  </view>
  <view id="chart_warp">
    <!-- tab -->
    <view id="tabs">
      <up-tabs lineWidth="20" lineHeight="7" :lineColor="`url(${lineBg}) 100% 100%`"
        :activeStyle="{
          color: '#303133',
          fontWeight: 'bold',
          transform: 'scale(1.05)'
        }" :inactiveStyle="{
          color: '#606266',
          transform: 'scale(1)'
        }" :list="tabList" @click="tabClick">
      </up-tabs>
    </view>
    <view id="main">
      <!-- 日期 -->
      <view id="dates">
        <view @click="handleDate">
          <up-icon name="calendar" style="margin-right: 10rpx;"></up-icon>
          <text>{{ bookTime }}</text>
        </view>
        <up-picker v-model="selectedYear" :show="showYear" @cancel="cancel" :columns="yearRange"
          @confirm="confirm"></up-picker>
        <up-datetime-picker :show="showMonth" v-model="selectedMonth" @confirm="confirm" @cancel="cancel"
          mode="year-month" format="YYYY-MM">
        </up-datetime-picker>
        <view id="date_tab">
          <view :class="{ actived: logoType === 2 }" @click="changeActived(2)">年</view>
          <view :class="{ actived: logoType === 1 }" @click="changeActived(1)">月</view>
        </view>
      </view>
      <foundation v-if="tabIndex == 0" :bookId="bookId" :logoType="logoType" :userId="userId" :key="timer"
        :bookTime="bookTime" />
      <classification v-if="tabIndex == 1" :bookId="bookId" :logoType="logoType" :userId="userId" :key="timer"
        :bookTime="bookTime" />
      <account v-if="tabIndex == 2" :bookId="bookId" :logoType="logoType" :userId="userId" :key="timer"
        :bookTime="bookTime" />
      <member v-if="tabIndex == 3" :bookId="bookId" :logoType="logoType" :userId="userId" :key="timer"
        :bookTime="bookTime"/>
    </view>
    <TabBar :currPage="2" />
  </view>
</template>

<style scoped lang='scss'>
#chart_warp {
  padding-bottom: 120rpx;
  box-sizing: border-box;
  background: #F1F1F1;
  .shadow_,
  #con {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }

  #main {
    padding: 0 20rpx;
    box-sizing: border-box;
  }

  #tabs{
    background: #fff;
  }

  #dates {
    padding: 10rpx;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    background: #fff;
    margin: 20rpx 0;

    >view:first-child {
      display: flex;
      align-items: center;
      width: 300rpx;
      justify-content: center
    }

    // text-align: center;
    // line-height: 100rpx;
  }



  #date_tab {
    display: flex;
    justify-content: center;

    >view {
      width: 180rpx;
      height: 50rpx;
      text-align: center;
      line-height: 50rpx;
      background: #F1F1F1;
      color: #333;
      margin: 0 10rpx;
    }

    .actived {
      background: #2979ff;
      color: #fff;
    }
  }
}
</style>