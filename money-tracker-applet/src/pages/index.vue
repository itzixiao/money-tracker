<script setup lang='ts'>
import dayjs from "dayjs";
import { ref } from "vue";
import { bookDetailList, deleteBookDetail, getCountInfo } from "@/api/home";
import { fenToYuan, yuanToFen } from "@/utils/conversion";
import { getFirstDayOfMonth, getLastDayOfMonth } from "@/utils/ruoyi";
import { previewPictures } from "@/utils/ruoyi";
import useUserStore from "@/store/modules/user";
import TopNav from "@/components/tabbar/topNav.vue";
import { onLoad, onShow } from "@dcloudio/uni-app";

const userStore = useUserStore();
const searchYear = ref();
const searchMonth = ref();
const consumeList: any = ref([]);
const selectDate = ref();
const billType = ref("");
const uToastRef: any = ref(null);
const pageNum = ref(1);
const expenditure: any = ref("0.00");
const income: any = ref("0.00");
const balance: any = ref("0.00");
const bookId = ref();
const bookList: any = ref([]);
const addAccountBook = ref(false);
const paging = ref();
const rightOptions = ref([
  {
    text: "编辑",
    style: {
      backgroundColor: "#007aff",
    },
  },
  {
    text: "删除",
    style: {
      backgroundColor: "#F56C6C",
    },
  },
]);
const chooseData = ref();
const listTotal = ref(0);
const showButton = ref(false);
const singleDate = ref(uni.getStorageSync('calendar').fulldate)

onShow(async () => {
  await userStore.getBookForm();
  let nowDate = dayjs().format("YYYY-MM-DD");
  searchYear.value = nowDate.split("-")[0];
  searchMonth.value = nowDate.split("-")[1];
  bookList.value = uni.getStorageSync("bookList");
  bookId.value = uni.getStorageSync("bookId");
  let calendarData = uni.getStorageSync('calendar')
  if(calendarData.fulldate !== undefined) {
    searchYear.value = calendarData.fulldate.split("-")[0];
    searchMonth.value = calendarData.fulldate.split("-")[1];
    singleDate.value = uni.getStorageSync('calendar').fulldate
  }
  getBook();
});

const getBook = async () => {
  if (bookList.value.length > 0) {
    getList(1, 10);
    getCount();
  } else {
    addAccountBook.value = true;
  }
};

const getCount = () => {
  let data1 = {
    bookId: bookId.value,
    beginBookTime:
      getFirstDayOfMonth(searchYear.value, searchMonth.value) + " 00:00:00",
    endBookTime:
      getLastDayOfMonth(searchYear.value, searchMonth.value) + " 23:59:59",
  };
  getCountInfo(data1).then((res: any) => {
    if (res.code == 200) {
      searchYear.value = res.data[0]?.bookMonth
        ? res.data[0].bookMonth.split("-")[0]
        : searchYear.value;
      searchMonth.value = res.data[0]?.bookMonth
        ? res.data[0].bookMonth.split("-")[1]
        : searchMonth.value;
      expenditure.value = res.data[0]?.expenditure
        ? fenToYuan(res.data[0].expenditure)
        : "0.00";
      income.value = res.data[0]?.income
        ? fenToYuan(res.data[0].income)
        : "0.00";
      balance.value = res.data[0]?.balance
        ? fenToYuan(res.data[0].balance)
        : "0.00";
    }
  });
};

const getList = (pageNo, pageSize) => {
  singleDate.value = uni.getStorageSync('calendar').fulldate
  if(bookList.value.length > 0) {
    let data2 = {
      type: billType.value,
      pageNum: pageNo,
      pageSize: pageSize,
      bookId: bookId.value,
      beginBookTime:
      singleDate.value == undefined ? getFirstDayOfMonth(searchYear.value, searchMonth.value) + " 00:00:00" : singleDate.value + " 00:00:00",
      endBookTime:
      singleDate.value == undefined ? getLastDayOfMonth(searchYear.value, searchMonth.value) + " 23:59:59" : singleDate.value + " 23:59:59",
    };
    bookDetailList(data2)
      .then((res) => {
        if (res.code == 200) {
          res.rows.map((item) => {
            item.bookMoneyList.map((inner) => {
              let time = inner.bookTime.split(" ")[1].split(":");
              inner.dateTime = time[0] + ":" + time[1];
            });
          });
          listTotal.value = res.total;

          // 始终调用 complete 方法，确保分页组件正确处理数据
          paging.value.complete(res.rows);

          if(res.rows.length == 0) {
            showButton.value = false
          }
        }
      })
      .catch(() => {});
  }
};

const getDate = (type: any) => {
  const date = new Date();
  let year = date.getFullYear();
  let month: any = date.getMonth() + 1;

  if (type === "start") {
    year = year - 60;
  } else if (type === "end") {
    year = year + 2;
  }
  month = month > 9 ? month : "0" + month;
  return `${year}-${month}`;
};

const dateChange = (e: any) => {
  searchYear.value = e.detail.value.split("-")[0];
  searchMonth.value = e.detail.value.split("-")[1];
  pageNum.value = 1;
  getCount();
  getList(1, 10);
};

const handleSelect = (e: any) => {
  billType.value = e;
  pageNum.value = 1;
  consumeList.value = [];
  getList(1, 10);
};

const handleOperate = (val: any) => {
  if (val.index == 0) {
    handleEdit(chooseData.value);
  } else {
    handleDelete(chooseData.value);
  }
};
const swipeChange = (data: any) => {
  chooseData.value = data;
};

const handleEdit = (data: any) => {
  uni.redirectTo({
    url: `/pages/add/index?id=${data.id}`,
  });
};
const handleDelete = (data: any) => {
  deleteBookDetail(data.id).then((res) => {
    if (res.code == 200 && res.data) {
      uToastRef.value.show({
        type: "success",
        title: "",
        message: "删除成功",
        iconUrl: "https://cdn.uviewui.com/uview/demo/toast/success.png",
      });
      pageNum.value = 1;
      getCount();
      getList(1, 10);
    }
  });
};

const viewDetail = (data: any) => {
  uni.navigateTo({
    url: `/pages/viewDetail?id=${data.id}`,
  });
};

const bookChange = (e) => {
  bookId.value = e;
  billType.value = "";
  pageNum.value = 1;
  getCount();
  getList(1, 10);
};

const toAccountBook = () => {
  uni.switchTab({ url: "/pages/accountBook" });
};

const showDetailImg = (val) => {
  uni.previewImage({
    indicator: "none",
    loop: false,
    urls: [previewPictures(val)],
  });
};

const refreshChange = () => {
  consumeList.value = []
  uni.setStorageSync('calendar', {})
  getList(1, 10);
}

const refreshEnd = (e) => {
  getCount();
  // if (e < 10) {
  //   if (searchMonth.value == 12) {
  //     searchYear.value = searchYear.value * 1 + 1;
  //     searchMonth.value = 1;
  //   } else {
  //     searchMonth.value = searchMonth.value * 1 + 1;
  //   }
  // } else {
  //   if (searchMonth.value == 1) {
  //     searchYear.value = searchYear.value * 1 - 1;
  //     searchMonth.value = 12;
  //   } else {
  //     searchMonth.value -= 1;
  //   }
  // }
  // selectDate.value =
  //   searchMonth.value < 10
  //     ? searchYear.value + "-" + "0" + searchMonth.value
  //     : searchYear.value + "-" + searchMonth.value;
  // setTimeout(() => {
  //   getList(1, 10);
  //   getCount();
  // }, 500);
};

const loadLastData = () => {
  uni.setStorageSync('calendar', {})
  showButton.value = false
  paging.value.complete([]);
  consumeList.value = [];
  if (searchMonth.value == 1) {
    searchYear.value = searchYear.value * 1 - 1;
    searchMonth.value = 12;
  } else {
    searchMonth.value -= 1;
  }
  selectDate.value =
    searchMonth.value < 10
      ? searchYear.value + "-" + "0" + searchMonth.value
      : searchYear.value + "-" + searchMonth.value;
  getList(1, 10);
  getCount();
};

const loadingEnd = (e: any) => {
  showButton.value = e == 2 && listTotal.value > 0;
};
</script>
<template>
  <view class="home-page">
    <view class="top-part">
      <TopNav :bookList="bookList" :bookId="bookId" :showCalendar="true" @bookChange="bookChange" />
      <view class="search-list">
        <view class="search-date">
          <view class="select-date">
            <span class="year">{{ searchYear }}年</span>
            <view class="month">
              {{ searchMonth }}<span>月</span>
              <picker
                mode="date"
                fields="month"
                :value="selectDate"
                :start="getDate('start')"
                :end="getDate('end')"
                @change="dateChange"
              >
                <up-icon name="arrow-down-fill" color="#fff"></up-icon>
              </picker>
            </view>
          </view>
          <view class="total-put">
            <span>总支出</span>
            <span class="total-num">{{ expenditure }}</span>
          </view>
        </view>
        <view class="put-push">
          <view class="income">
            <span class="title">收入</span>
            <span class="num">{{ income }}</span>
          </view>
          <view class="pay">
            <span class="title">结余</span>
            <span class="num">{{ balance }}</span>
          </view>
        </view>
      </view>
    </view>
    <view class="bottom-part">
      <view class="bill-type">
        <view
          class="button"
          :class="billType == '' ? 'selected' : ''"
          @click="handleSelect('')"
          >全部</view
        >
        <view
          class="button"
          :class="billType == '0' ? 'selected' : ''"
          @click="handleSelect('0')"
          >收入</view
        >
        <view
          class="button"
          :class="billType == '1' ? 'selected' : ''"
          @click="handleSelect('1')"
          >支出</view
        >
        <view
          class="button"
          :class="billType == '2' ? 'selected' : ''"
          @click="handleSelect('2')"
          >不计收支</view
        >
      </view>
      <z-paging
        ref="paging"
        v-model="consumeList"
        :refresher-enabled="true"
        :use-refresher-status-bar-placeholder="true"
        :fixed="false"
        :use-custom-refresher="true"
        height="59vh"
        :show-loading-more-no-more-view="false"
        @query="getList"
        @refresherTouchend="refreshEnd"
        @loadingStatusChange="loadingEnd"
        @refresherStatusChange="refreshChange"
      >
        <uni-swipe-action>
          <view
            v-for="(item, index) in consumeList"
            :key="index"
            class="day-detail"
          >
            <view class="pay-date">
              <view>{{ item.bookTime }}</view>
              <view>
                <span>支出: {{ fenToYuan(item.expenditure) }}</span>
                <span>收入: {{ fenToYuan(item.income) }}</span>
              </view>
            </view>
            <uni-swipe-action-item
              v-for="inner in item.bookMoneyList"
              :key="inner.id"
              :right-options="rightOptions"
              class="day-pay"
              autoClose
              @click="handleOperate"
              @change="swipeChange(inner)"
            >
              <view style="width: 100%">
                <view class="pay-content" @click="viewDetail(inner)">
                  <view class="type">
                    <image
                      :src="previewPictures(inner.categoryUrl)"
                      class="classify-img"
                    />
                    <view>{{ inner.categoryName }}</view>
                  </view>
                  <span
                    :style="{
                      color:
                        inner.type == 0
                          ? '#4a7f4a'
                          : inner.type == 1
                          ? '#f15252'
                          : '#8a8a8a',
                    }"
                    style="font-weight: 600; margin-right: 20rpx"
                    >{{ inner.type == 0 ? "+" : ""
                    }}{{ fenToYuan(inner.money) }}元</span
                  >
                </view>
                <view class="detail-content">
                  <view class="remark">{{ inner.remark }}</view>
                  <image
                    :src="previewPictures(inner.detailImg)"
                    mode="aspectFill"
                    class="detail-img"
                    v-if="inner.detailImg !== ''"
                    @click="showDetailImg(inner.detailImg)"
                  />
                  <view class="add-detail">
                    <span>{{ inner.nickName }}</span>
                    <span>{{ inner.accountName }}</span>
                    <span class="dot">·</span>
                    <span>{{ inner.dateTime }}</span>
                  </view>
                </view>
              </view>
              <!-- <view class="btn">
                <view
                  class="edit-btn"
                  v-if="inner.showEdit"
                  @click="handleEdit(inner)"
                  >编辑</view
                >
                <view
                  class="delete-btn"
                  v-if="inner.showDelete"
                  @click="handleDelete(inner)"
                  >删除</view
                >
              </view> -->
            </uni-swipe-action-item>
          </view>
          <up-button
            type="primary"
            text="加载上月账单明细"
            @click="loadLastData"
            v-if="showButton"
            class="lastBill"
            color="#fd8702"
          ></up-button>
        </uni-swipe-action>
      </z-paging>
    </view>
    <up-toast ref="uToastRef"></up-toast>
    <up-popup :show="addAccountBook" :round="5" mode="center" class="add-book">
      <view class="text">当前没有账本，去创建新账本！</view>
      <view class="btn">
        <up-button
          type="primary"
          size="small"
          text="确定"
          @click="toAccountBook"
        ></up-button>
        <up-button
          type="primary"
          size="small"
          text="取消"
          @click="addAccountBook = false"
        ></up-button>
      </view>
    </up-popup>
  </view>
  <TabBar :currPage="1" />
</template>

<style scoped lang='scss'>
.home-page {
  padding-bottom: 120rpx;
  box-sizing: border-box;
  height: 100vh;
  overflow: hidden;
  .top-part {
    width: 100%;
    background: #fd8702;
    padding: 100rpx 0;
    color: #fff;
    border-radius: 0 0 20rpx 20rpx;
    box-shadow: 0 0 10rpx #838383d2;

    .search-list {
      width: 100%;
      height: 50%;
      padding: 20rpx 40rpx;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .search-date {
        width: 100%;
        display: flex;
        justify-content: space-between;
        margin-bottom: 20rpx;

        .total-put {
          width: 50%;
          display: flex;
          flex-direction: column;
          justify-content: space-around;

          .total-num {
            font-size: 50rpx;
            font-weight: 600;
          }
        }

        .select-date {
          width: 50%;
          // height: 50%;
          .year {
            font-size: 30rpx;
          }
          .month {
            font-size: 50rpx;
            display: flex;
            align-items: center;
            span {
              font-size: 30rpx;
              margin-right: 10rpx;
            }
          }
        }
      }
      .put-push {
        width: 100%;
        display: flex;
        .income,
        .pay {
          width: 50%;
          height: 100%;

          .title {
            font-size: 30rpx;
            margin-right: 10rpx;
          }
          .num {
            font-size: 40rpx;
          }
        }
      }
    }
  }

  .bottom-part {
    position: relative;
    top: -80rpx;
    box-sizing: border-box;
    margin: 0 30rpx;
    padding: 30rpx;
    box-shadow: 0 0 10rpx #838383d2;
    background: #fff;
    border-radius: 20rpx;
    .bill-type {
      display: flex;
      padding: 10rpx 0;
      .button {
        padding: 10rpx 20rpx;
        text-align: center;
        background: #fff;
        margin-right: 25rpx;
        border-radius: 30rpx;
        border: 2rpx solid #ddd;
      }
      .selected {
        background: #ffcb8e;
        border: 2rpx solid #ffcb8e;
      }
    }
    .month-bill {
      height: 59vh;
      overflow: auto;
      box-sizing: border-box;
    }
    .day-detail {
      padding: 15rpx 0;
      .pay-date {
        display: flex;
        justify-content: space-between;
        align-items: center;
        span:first-child {
          margin-right: 20rpx;
        }
      }
      .day-pay {
        border-bottom: 1rpx solid #dedede;
        padding: 20rpx 0;
        position: relative;
        flex-direction: row;

        .pay-content {
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
          .type {
            display: flex;
            align-items: center;

            .classify-img {
              width: 70rpx;
              height: 70rpx;
              margin-right: 20rpx;
              border-radius: 50%;
            }

            .type-name {
              display: flex;
              flex-direction: column;

              .category-name {
                font-size: 35rpx;
              }
            }
          }
        }

        .detail-content {
          color: #8d8d8d;
          padding-left: 70rpx;
          display: flex;
          flex-direction: column;

          :deep(.uni-tooltip-popup) {
            left: 10% !important;
          }

          .remark {
            width: 90%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }

          .detail-img {
            width: 70rpx;
            height: 70rpx;
            margin-bottom: 20rpx;
          }

          .add-detail {
            display: flex;
            align-items: center;
            font-size: 24rpx;
            span {
              padding: 0 10rpx;

              &:first-child {
                border-right: 1px solid #a5a0a0;
              }
            }

            span:first-child {
              padding: 0 10rpx 0 0;
            }
          }
        }

        .btn {
          right: -200rpx;
          height: 100%;
          position: absolute;
          top: 0;
          display: flex;

          .edit-btn,
          .delete-btn {
            width: 100rpx;
            text-align: center;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
          }
          .edit-btn {
            background-color: rgb(218, 165, 50);
          }
          .delete-btn {
            background-color: rgb(221, 49, 49);
          }
        }
      }
    }
    .lastBill {
      height: 60rpx;
      background:#fd8702;
      border: none;
    }
  }
}
:deep(.add-book) {
  .u-popup__content {
    width: 70vw;
    padding: 2vh 5vw;

    .text {
      padding: 2vh 0;
      text-align: center;
    }

    .btn {
      width: 100%;
      display: flex;

      .u-button {
        width: 15vw;
      }
    }
  }
}
</style>
