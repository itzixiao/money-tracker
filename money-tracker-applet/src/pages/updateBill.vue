<script setup lang='ts'>
import { ref, onMounted, defineOptions, getCurrentInstance } from "vue";
import type { Flow } from "@/utils/flow";
import { updateAliFile, updateWXFile } from "@/api/add";
import update from "@/utils/update.js";
import useUserStore from "@/store/modules/user";
import { getUserList } from "@/api/add";
import { getToken } from "@/utils/auth";
import config from "@/config";
const baseUrl = config.baseUrl;
const { proxy } = getCurrentInstance();
defineOptions({
  name: "UpdateBill",
});
const userStore = useUserStore();
const flows = ref<Flow[]>([]);
const billType = ref("");
const excelTableHead = ref();
const excelTableBody = ref();
const bookList = ref();
const bookId = ref();
const userList = ref();
const userId = ref();
const headerList: any = ref([]);
const tableData: any = ref();
const wxTablerHead = ref();

// 公共的配置部分
const createUploadOptions = (platform: any, fileType: any) => {
  return {
    url: `${baseUrl}/book/money/import/${platform}/pay/${fileType}`,
    name: "file",
    header: {
      Authorization: `Bearer ${getToken()}`,
    },
    formData: {},
  };
};

// WeChat 上传 CSV
const wxPptions = ref(createUploadOptions("wechat", "csv"));

// Alipay 上传 CSV
const zfbPptions = ref(createUploadOptions("ali", "csv"));

// WeChat 上传 ZIP
const wxZip = ref(createUploadOptions("wechat", "zip"));

// Alipay 上传 ZIP
const zfbZip = ref(createUploadOptions("ali", "zip"));

const wxlsjUpload: any = ref("");
const zfblsjUpload: any = ref("");
const wxUpload: any = ref("");
const zfUpload: any = ref("");
const addPwd = ref(false);
const zipPwd = ref("");
onMounted(() => {
  getBookId();
});

function setFomrData() {
  let data = {
    bookId: bookId.value,
    userId: userId.value,
  };
  wxUpload.value.setData("formData", data);
  zfUpload.value.setData("formData", data);
}

const getBookId = async () => {
  await userStore.getBookForm();
  bookList.value = uni.getStorageSync("bookList");
  bookId.value = uni.getStorageSync("bookId");
  setFomrData();
  getUserData();
};

const getUserData = () => {
  getUserList({ bookId: bookId.value }).then((res) => {
    if (res.code == 200 && res.rows.length > 0) {
      userList.value = res.rows.map((item: any) => {
        item.text = item.nickName;
        item.value = item.userId;
        return item;
      });
      userId.value = res.rows[0].userId;
      setFomrData();
    } else {
      userList.value = [];
    }
  });
};

const chooseType = (val: any) => {
  billType.value = val;
};

const handleDel = (index: number) => {
  tableData.value.splice(index, 1);
  flows.value.splice(index, 1);
};

const handleConfirm = () => {
  if (bookId.value == undefined || userId.value == undefined) {
    uni.showToast({ title: "请选择账本或成员！", icon: "error" });
    return;
  } else if (billType.value == "alipay") {
    uni.showLoading();
    updateAliFile({ bookId: bookId.value, userId: userId.value }, flows.value)
      .then((res) => {
        if (res.code == 200 && res.data) {
          uni.hideLoading();
          uni.showToast({
            title: "账单导入成功！",
            icon: "success",
          });
          handleCancel();
        }
      })
      .catch(() => {
        uni.hideLoading();
        uni.showToast({
          title: "账单导入失败！",
          icon: "error",
        });
      });
  } else {
    uni.showLoading();
    updateWXFile({ bookId: bookId.value, userId: userId.value }, flows.value)
      .then((res) => {
        if (res.code == 200 && res.data) {
          uni.hideLoading();
          uni.showToast({
            title: "账单导入成功！",
            icon: "success",
          });
          handleCancel();
        }
      })
      .catch(() => {
        uni.hideLoading();
        uni.showToast({
          title: "账单导入失败！",
          icon: "error",
        });
      });
  }
};

const handleCancel = () => {
  uni.switchTab({ url: "/pages/index" });
};

const removeFile = () => {
  flows.value = [];
  excelTableHead.value ? (excelTableHead.value.innerHTML = "") : "";
  excelTableBody.value ? (excelTableBody.value.innerHTML = "") : "";
  return true;
};

const bookChange = (e) => {
  userStore.$patch({ bookId: e });
  uni.setStorageSync("bookId", e);
  bookId.value = e;
  setFomrData();
  getUserData();
  removeFile();
};

const userChange = (e) => {
  userId.value = e;
  setFomrData();
  removeFile();
  headerList.value = [];
  tableData.value = [];
};

function onuploadEnd(item: any) {
  flows.value = [];
  tableData.value = [];
  wxTablerHead.value = [];
  if (item.type == "success" && item.responseText) {
    tableData.value = JSON.parse(item.responseText).data;
    if (tableData.value && tableData.value.length >= 0) {
      tableData.value.map((item: any) => {
        flows.value.push({
          day: item.tradeTime,
          description: item.tradeOpponent + "-" + item.paymentMethod,
          flowType: item.incomeExpense,
          money: item.amount,
          name: item.productDescription.replace(/\\"/g, ""),
          payType:
            billType.value == "wxpay"
              ? "微信"
              : billType.value == "alipay"
              ? "支付宝"
              : billType.value == "aliZip"
              ? "支付宝"
              : "微信",
          type: item.tradeCategory,
        });
        item.productDescription = item.productDescription.replace(/\\"/g, "");
        item.remark = item.remark.replace(/\\"/g, "");
        item.amount = `￥${item.amount.toFixed(2)}`;
        item.tradeTime = item.tradeTime;
        addPwd.value = false;
      });
    }
  }
}

function onChange(e: any, text: string) {
  billType.value = text;
  if (billType.value == "aliZip" || billType.value == "wxZip") {
    zipPwd.value = "";
    addPwd.value = true;
  }
}

const openImage = (val: any) => {
  let url = `guide/${val}`;
  uni.previewImage({
    indicator: "none",
    loop: false,
    urls: [proxy.previewPictures(url)],
  });
};

const changePwd = () => {
  if (zipPwd.value == "") {
    uni.showToast({
      title: "密码不能为空！",
      icon: "error",
    });
  } else {
    let zipData = {
      bookId: bookId.value,
      userId: userId.value,
      password: zipPwd.value,
    };
    zfblsjUpload.value.setData("formData", zipData);
    wxlsjUpload.value.setData("formData", zipData);
    if (billType.value == "aliZip") {
      zfblsjUpload.value.upload();
    } else if (billType.value == "wxZip") {
      wxlsjUpload.value.upload();
    }
  }
};
</script>
<template>
  <view class="upload-bill">
    <view class="">
      <view class="select-part">
        <span>当前账本：</span>
        <uni-data-select
          v-model="bookId"
          :localdata="bookList"
          :clear="false"
          @change="bookChange"
        >
        </uni-data-select>
      </view>
      <view class="select-part">
        <span>当前成员：</span>
        <uni-data-select
          v-model="userId"
          :localdata="userList"
          :clear="false"
          @change="userChange"
        >
        </uni-data-select>
      </view>
    </view>

    <view class="select-type">
      <view>
        <lsj-upload
          ref="wxUpload"
          childId="uploadId"
          :count="99"
          :option="zfbPptions"
          :toBase="false"
          :size="4000"
          :formats="''"
          @uploadEnd="onuploadEnd"
          @changeUp="onChange($event, 'alipay')"
          @click="chooseType('alipay')"
        >
          <up-button
            type="primary"
            size="small"
            text="支付宝账单"
            @click="chooseType('alipay')"
          >
          </up-button>
        </lsj-upload>
      </view>

      <view>
        <lsj-upload
          ref="zfUpload"
          childId="uploadId"
          :count="99"
          :option="wxPptions"
          :toBase="false"
          :size="4000"
          :formats="''"
          @uploadEnd="onuploadEnd"
          @changeUp="onChange($event, 'wxpay')"
          @click="chooseType('wxpay')"
        >
          <view>
            <up-button
              type="primary"
              size="small"
              text="微信账单"
              style="background-color: #04c15f; border: none"
              @click="chooseType('wxpay')"
            ></up-button>
          </view>
        </lsj-upload>
      </view>
    </view>
    <view class="select-type">
      <view>
        <lsj-upload
          ref="zfblsjUpload"
          childId="uploadId"
          :count="99"
          :option="zfbZip"
          :toBase="false"
          :size="4000"
          :formats="'zip'"
          :instantly="false"
          @uploadEnd="onuploadEnd"
          @changeUp="onChange($event, 'aliZip')"
          @click="chooseType('aliZip')"
        >
          <view>
            <up-button
              type="primary"
              size="small"
              text="支付宝压缩包"
              @click="chooseType('aliZip')"
            ></up-button>
          </view>
        </lsj-upload>
      </view>
      <view>
        <lsj-upload
          ref="wxlsjUpload"
          childId="uploadId"
          :count="99"
          :option="wxZip"
          :toBase="false"
          :size="4000"
          :formats="'zip'"
          :instantly="false"
          @uploadEnd="onuploadEnd"
          @changeUp="onChange($event, 'wxZip')"
          @click="chooseType('wxZip')"
        >
          <view>
            <up-button
              type="primary"
              size="small"
              text="微信压缩包"
              style="background-color: #04c15f; border: none"
              @click="chooseType('wxZip')"
            ></up-button>
          </view>
        </lsj-upload>
      </view>
    </view>
    <view class="tip">
      <span>1、目前支持'支付宝'/'微信'导出的带密码压缩包和解压后的CSV账单文件导入;</span><br />
      <span>2、选择账本和成员后可上传文件;</span><br />
      <span>3、选择文件后可以预览文件数据;</span><br />
      <span>4、选择文件后请确认导入按钮开始上传</span><br />
      <view
        >5、<span
          style="border-bottom: 1px solid blue; color: blue"
          @click="openImage('alizip.png')"
          >微信导出账单教程.png</span
        ></view
      >
      <view
        >6、<span
          style="border-bottom: 1px solid blue; color: blue"
          @click="openImage('downloadali.png')"
          >解压微信账单教程.png</span
        ></view
      >
      <view
        >7、<span
          style="border-bottom: 1px solid blue; color: blue"
          @click="openImage('downloadwx.png')"
          >支付宝导出账单教程.png</span
        >
      </view>
      <view
        >8、<span
          style="border-bottom: 1px solid blue; color: blue"
          @click="openImage('wxzip.png')"
          >解压支付宝账单教程.png</span
        >
      </view>
    </view>

    <view class="excel-table" v-if="tableData && tableData.length">
      <view class="day-list" v-for="(item, index) in tableData" :key="index">
        <view class="th"
          ><span>金额：</span><span>{{ item.amount }}</span></view
        >
        <view class="th"
          ><span>收/支：</span><span>{{ item.incomeExpense }}</span></view
        >
        <view class="th"
          ><span>交易单号：</span><span>{{ item.orderNo }}</span></view
        >
        <view class="th"
          ><span>支付方式：</span><span>{{ item.paymentMethod }}</span></view
        >
        <view class="th"
          ><span>商品：</span><span>{{ item.productDescription }}</span></view
        >
        <view class="th"
          ><span>备注：</span><span>{{ item.remark }}</span></view
        >
        <view class="th"
          ><span>交易类型：</span><span>{{ item.tradeCategory }}</span></view
        >
        <view class="th"
          ><span>交易对方：</span><span>{{ item.tradeOpponent }}</span></view
        >
        <view class="th"
          ><span>当前状态：</span><span>{{ item.tradeStatus }}</span></view
        >
        <view class="th"
          ><span>交易时间：</span><span>{{ item.tradeTime }}</span></view
        >
        <view>
          <up-button
            text="删除"
            type="primary"
            @click="handleDel(index)"
            style="height: 60rpx"
          ></up-button>
          <!-- <up-button text="编辑" @click="handleCancel"></up-button> -->
        </view>
      </view>
    </view>

    <view class="btn">
      <up-button
        text="确定导入"
        type="primary"
        @click="handleConfirm"
      ></up-button>
      <up-button text="取消" @click="handleCancel"></up-button>
    </view>
    <up-popup
      :show="addPwd"
      :round="5"
      mode="center"
      class="add-pwd"
      closeOnClickOverlay
      @close="addPwd = false"
    >
      <view class="password">
        <span>压缩包密码：</span>
        <up-input
          placeholder="请输入内容"
          border="surround"
          v-model="zipPwd"
        ></up-input>
      </view>
      <up-button text="确定" type="primary" @click="changePwd"></up-button>
    </up-popup>
  </view>
</template>

<style scoped lang='scss'>
:deep(.uni-select__input-text) {
  color: #333 !important;
}

.upload-bill {
  height: 100vh;
  box-sizing: border-box;
  padding: 40rpx;
  background: #fff;

  .select-part {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
  }

  .upload {
    padding: 20rpx 0;
  }

  .title {
    color: #111;
    font-size: 36rpx;
    margin-bottom: 20rpx;
  }

  .select-type {
    display: flex;
    justify-content: space-around;

    .u-button--small {
      width: 160rpx;
    }
  }

  .excel-table {
    width: 87vw;
    max-height: 500rpx;
    overflow: auto;
    border-collapse: collapse;
    border: 2rpx solid #e5e5e5;
    // border-right: none;

    .day-list {
      padding: 1vh;
      border-bottom: 1px solid #e5e5e5;
    }
  }
  :deep(.excel-td) {
    max-width: 12rem;
    padding: 0.5rem;
    border-collapse: collapse;
    border-bottom: 1px solid;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }

  .tip {
    color: rgb(235, 82, 82);
    margin-bottom: 1vh;
  }

  .pay-table {
    height: 25vh;

    .uni-group {
      display: flex;
    }
  }

  .btn {
    width: 100vw;
    height: 6vh;
    position: fixed;
    bottom: 30rpx;
    left: 0;
    display: flex;
    justify-content: space-around;
    margin-top: 2vh;

    .u-button {
      width: 20vw;
    }
  }
}

.tables {
  width: 100%;
  -webkit-overflow-scrolling: touch;
  display: table;
  border-collapse: collapse;
  overflow-y: auto; /* 允许纵向滚动 */
  max-height: 400rpx; /* 设置一个最大高度以便滚动 */

  .th {
    text-align: center;
    padding: 15rpx 0;
    display: table-cell;
    border-right: 2rpx solid #e5e5e5;
    min-width: 240rpx;
    background: #f5f7ff;
    font-size: 25rpx;
    position: sticky;
    top: 0;
    z-index: 1; /* 确保表头在滚动内容上方 */
  }

  .td {
    text-align: center;
    background: #ffffff;
    padding: 20rpx 5rpx;
    display: table-cell;
    border-top: 2rpx solid #e5e5e5;
    border-right: 2rpx solid #e5e5e5;
    font-size: 20rpx;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }

  .tr {
    display: table-row;
  }
}
#btn {
  padding: 20rpx 35rpx;
  display: flex;
  justify-content: space-between;
  .u-button {
    width: 100rpx !important;
  }
}
:deep(.add-pwd) {
  .u-popup__content {
    width: 80vw;
    padding: 2vh 5vw;

    .password {
      display: flex;
      align-items: center;
      margin-bottom: 30rpx;
    }
  }
}
</style>
