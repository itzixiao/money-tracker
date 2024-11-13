<script setup lang='ts'>
import { onLoad } from "@dcloudio/uni-app";
import { ref, onMounted, defineOptions } from "vue";
import { getType, getBookList, getBillDetail } from "@/api/add";
import { fenToYuan } from "@/utils/conversion";
import { bookDetailList, deleteBookDetail } from "@/api/home";
import { previewPictures } from "@/utils/ruoyi";

defineOptions({
  name: "ViewDetail",
});

const id = ref();
const recordForm = ref({
  categoryName: "",
  categoryUrl: "",
  type: 0,
  bookTime: "",
  nickName: "",
  money: "",
  remark: "",
  detailImg: "",
  accountName: '',
});
const uToastRef: any = ref(null);

onLoad((option: any) => {
  id.value = option.id;
  getDetail(option.id);
});

const getDetail = (id: any) => {
  getBillDetail(id).then((res: any) => {
    if (res.code == 200) {
      res.data.money = fenToYuan(res.data.money);
      recordForm.value = res.data;
    }
  });
};

const handleEdit = () => {
  uni.navigateTo({
    url: `/pages/add/index?id=${id.value}`,
  });
};

const handleDelete = () => {
  deleteBookDetail(id.value).then((res) => {
    if (res.code == 200 && res.code) {
      uToastRef.value.show({
        type: "success",
        title: "",
        message: "删除成功",
        iconUrl: "https://cdn.uviewui.com/uview/demo/toast/success.png",
      });
      uni.switchTab({ url: "/pages/index" });
    }
  });
};

const showDetailImg = (val) => {
  uni.previewImage({
    indicator: "none",
    loop: false,
    urls: [previewPictures(val)],
  });
};

</script>
<template>
  <view class="view-detail">
    <view class="detail">
      <view class="detail-item">
        <view class="detail-classify">
          <image
            :src="previewPictures(recordForm.categoryUrl)"
            class="classify-img"
          />
          {{ recordForm.categoryName }}
        </view>
        <view :style="{ color: recordForm.type == 0 ? '#4a7f4a' : recordForm.type == 1 ? '#f15252' : '#8a8a8a' }"
          >{{ recordForm.type == 0 ? "+" : '' }}{{ recordForm.money }}元</view
        >
      </view>
      <view class="detail-item">
        <view class="text">时间</view>
        <view class="value">{{ recordForm.bookTime }}</view>
      </view>
      <view class="detail-item">
        <view class="text">账户</view>
        <view class="value">{{ recordForm.accountName }}</view>
      </view>
      <view class="detail-item">
        <view class="text">成员</view>
        <view class="value">{{ recordForm.nickName }}</view>
      </view>
      <view class="detail-item">
        <view class="text">备注</view>
        <view class="value">{{ recordForm.remark }}</view>
      </view>
      <view class="detail-item">
        <view class="text">附件</view>
        <image
          :src="previewPictures(recordForm.detailImg)"
          mode="aspectFill"
          class="annex-img"
          v-if="recordForm.detailImg !== ''"
          @click="showDetailImg(recordForm.detailImg)"
        />
      </view>
    </view>
    <view class="btn-bottom">
      <view class="btn" @click="handleEdit">编辑</view>
      <view class="btn" @click="handleDelete">删除</view>
    </view>
    <up-toast ref="uToastRef"></up-toast>
  </view>
</template>

<style scoped lang='scss'>
.view-detail {
  width: 100vw;
  height: 100vh;
  background: #fff;

  .detail {
    padding: 5vw;
    .detail-item {
      width: 100%;
      padding: 1vh 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #dedede;

      .text {
        width: 20%;
      }

      .value {
        width: 70%;
        text-align: right;
        word-wrap: break-word; /* 允许在单词边界上折行 */
        overflow-wrap: break-word; /* 对于兼容性，通常与word-wrap使用 */
        white-space: normal;
      }

      .detail-classify {
        display: flex;
        align-items: center;

        .classify-img {
          width: 10vw;
          height: 10vw;
          margin-right: 20rpx;
        }
      }
      .annex-img {
        width: 20vw;
        height: 20vw;
      }
    }
  }

  .btn-bottom {
    width: 100vw;
    height: 5vh;
    position: fixed;
    bottom: 0;
    left: 0;
    display: flex;
    align-items: center;
    border-top: 1px solid #dedede;
    .btn {
      width: 50%;
      height: 100%;
      text-align: center;
      line-height: 5vh;
      border-right: 1px solid #dedede;
    }
  }
}
</style>