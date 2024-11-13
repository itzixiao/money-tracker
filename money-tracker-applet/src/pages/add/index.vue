<script setup lang='ts'>
import config from "@/config";
import { ref, onMounted, defineOptions } from "vue";
import {
  getType,
  getUserList,
  getBillDetail,
  getAccountList,
  addClassifyType,
  editClassifyType,
  deleteClassifyType,
  getIconList,
} from "@/api/add";
import dayjs from "dayjs";
import KeyBoard from "@/components/keyboard/index.vue";
import { onLoad } from "@dcloudio/uni-app";
import { fenToYuan } from "@/utils/conversion";
import { previewPictures } from "@/utils/ruoyi";
import update from "@/utils/update.js";
const baseUrl = config.baseUrl;
import { getToken } from "@/utils/auth";
import useUserStore from "@/store/modules/user";

defineOptions({
  name: "",
});

const userStore = useUserStore();
const bookList: any = ref([]);
const showDatePicker = ref(false);
const recordForm: any = ref({
  money: "0.00",
  bookTime: dayjs(new Date()).format("YYYY-MM-DD HH:mm:ss"),
  userName: "请选择",
  userId: null,
  nickName: "",
  remark: "",
  type: 1,
  bookId: null,
  categoryId: "",
  categoryName: "请选择",
  categoryUrl: "",
  detailImg: "",
  accountId: "",
  accountName: "请选择",
  accountUrl: "",
  accountBaseId: "",
});
const classifyPop: any = ref(null);
const classifyList = ref();
const accountPop = ref();
const fileList = ref<any>([]);
const detailId = ref();
const showAdd = ref(false);
const rules = ref({
  "addForm.classifyName": [
    {
      type: "string",
      required: true,
      message: "请填写分类名称",
      trigger: ["blur"],
    },
  ],
  "addForm.fileList": [
    {
      type: "Array",
      required: true,
      message: "请上传分类图标",
      trigger: ["change"],
    },
  ],
});
const addForm = ref({
  classifyName: "",
  fileList: <any>[],
  isChoose: false,
  categoryUrl: "",
});
const memberPop = ref();
const memberList = ref();
const isBill = ref(true);
const accountList = ref();
const addFormref = ref();
const classifyType = ref();
const iconList: any = ref([]);
const nextClassify = ref();
const firstClassify = ref();
const showDelete = ref(false);
const showFirstDelete = ref(false);
const pageNum = ref(1);
const pageSize = ref(16);
const showMore = ref(true);
const isAdd = ref(true);

onLoad(async (option: any) => {
  userStore.getBookForm();
  if (option.id) {
    detailId.value = option.id * 1;
    getDetail(option.id);
  } else {
    getBook();
  }
});

const getBook = async () => {
  bookList.value = uni.getStorageSync("bookList");
  recordForm.value.bookId = uni.getStorageSync("bookId");
  getClassify();
  getUserData();
  getAccountData();
};

const bookChange = (e) => {
  uni.setStorageSync("bookId", e);
  userStore.$patch({ bookId: e });
  detailId.value = null;
  recordForm.value = {
    money: "0.00",
    bookTime: dayjs(new Date()).format("YYYY-MM-DD HH:mm:ss"),
    userName: "请选择",
    userId: null,
    nickName: "",
    remark: "",
    type: 1,
    bookId: e,
    categoryId: "",
    categoryBaseId: "",
    categoryName: "请选择",
    categoryUrl: "",
    detailImg: "",
    accountId: "",
    accountName: "请选择",
    accountUrl: "",
    accountBaseId: "",
  };
  fileList.value = [];
  getClassify();
  getUserData();
  getAccountData();
};

const changeType = (val: any) => {
  recordForm.value.type = val;
  getClassify();
};

const getClassify = () => {
  getType({
    bookId: recordForm.value.bookId,
    type: recordForm.value.type == 2 ? "" : recordForm.value.type,
  }).then((res: any) => {
    if (res.code == 200 && res.data.length > 0) {
      classifyList.value = res.data.map((item: any) => {
        item.isChoose = false;
        item.children.map((inner: any) => {
          inner.isChoose = false;
        });
        return item;
      });
      if (detailId.value == null) {
        classifyList.value[0].isChoose = true;
        classifyList.value[0].children[0].isChoose = true;
        recordForm.value.categoryName = res?.data[0]?.children[0]?.name;
        recordForm.value.categoryId = res.data[0]?.children[0]?.id;
        recordForm.value.categoryBaseId =
          res.data[0]?.children[0]?.categoryBaseId;
        recordForm.value.categoryUrl = res.data[0]?.children[0]?.categoryUrl;
        nextClassify.value = res.data[0].children;
        firstClassify.value = res.data[0];
      } else {
        firstClassify.value = res.data[0];
        classifyList.value[0].isChoose = true;
        classifyList.value.map((item) => {
          item.children.map((inner) => {
            if (inner.categoryUrl == recordForm.value.categoryUrl) {
              nextClassify.value = item.children;
              inner.isChoose = true;
            } else {
              inner.isChoose = false;
            }
          });
        });
      }
    } else {
      classifyList.value = [];
      recordForm.value.categoryName = "请选择";
      recordForm.value.categoryId = "";
      recordForm.value.categoryBaseId = "";
      recordForm.value.categoryUrl = "";
    }
  });
};

const chooseFirstClassify = (data: any) => {
  firstClassify.value = data;
  showDelete.value = false;
  classifyList.value.map((item) => {
    item.isChoose = false;
  });
  data.isChoose = true;
  nextClassify.value = data.children;
};

const choseClassify = (val: any) => {
  recordForm.value.categoryName = val?.name;
  recordForm.value.categoryUrl = val?.categoryUrl;
  recordForm.value.categoryId = val?.id;
  recordForm.value.categoryBaseId = val?.categoryBaseId;
  classifyList.value.map((item: any) => {
    item?.children.map((inner: any) => {
      inner.isChoose = inner?.categoryBaseId === val?.categoryBaseId;
    });
  });
  classifyPop.value.close();
};

const addClassify = (val: any) => {
  classifyType.value = val;
  getIconData();
  showAdd.value = true;
  isAdd.value = true;
  classifyPop.value.close();
};

const changeClassify = (val: any) => {
  addForm.value = val;
  addForm.value.classifyName = val.name;
  addForm.value.categoryUrl = previewPictures(val.categoryUrl);
  addForm.value.fileList = [
    { name: val.categoryUrl, url: previewPictures(val.categoryUrl) },
  ];
  getIconData();
  showAdd.value = true;
  isAdd.value = false;
  classifyPop.value.close();
};

const handleDeleteClassify = (val: any) => {
  deleteClassifyType(val.id).then((res) => {
    if (res.code == 200 && res.data) {
      uni.showToast({ title: "删除成功！", icon: "success", duration: 1000 });
      getClassify();
      showDelete.value = false;
      classifyPop.value.open();
    } else {
      uni.showToast({ title: "删除失败！", icon: "success", duration: 1000 });
    }
  });
};

const getIconData = () => {
  getIconList({
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  }).then((res) => {
    res.rows.map((item) => {
      item.isChoose = false;
      iconList.value.push(item);
    });
    showMore.value = iconList.value.length !== res.total;
  });
};

const getMore = () => {
  pageNum.value += 1;
  getIconData();
};

const chooseIcon = (val: any) => {
  iconList.value.map((item) => {
    item.isChoose = false;
  });
  val.isChoose = true;
  addForm.value.isChoose = true;
  addForm.value.categoryUrl = val.categoryUrl;
  addForm.value.fileList = [];
};

const getAccountData = () => {
  getAccountList({ bookId: recordForm.value.bookId }).then((res) => {
    if (res.code == 200 && res.data.length > 0) {
      accountList.value = res.data.map((item: any) => {
        item.children.map((inner: any) => {
          inner.isChoose = false;
        });
        return item;
      });
      if (detailId.value == null) {
        accountList.value[0].children[0].isChoose = true;
        recordForm.value.accountName = res?.data[0]?.children[0]?.name;
        recordForm.value.accountId = res.data[0]?.children[0]?.id;
        recordForm.value.accountUrl = res.data[0]?.children[0]?.accountUrl;
        recordForm.value.accountBaseId =
          res.data[0]?.children[0]?.accountBaseId;
      } else {
        accountList.value.map((item) => {
          item.children.map((inner) => {
            if (inner.id == recordForm.value.accountId) {
              inner.isChoose = true;
            } else {
              inner.isChoose = false;
            }
          });
        });
      }
    } else {
      accountList.value = [];
      recordForm.value.accountName = "请选择";
      recordForm.value.accountId = "";
      recordForm.value.accountUrl = "";
      recordForm.value.accountBaseId = "";
    }
  });
};

const choseAccount = (val: any) => {
  recordForm.value.accountId = val?.id;
  recordForm.value.accountName = val?.name;
  recordForm.value.accountUrl = val?.accountUrl;
  recordForm.value.accountBaseId = val?.accountBaseId;
  accountList.value.map((item: any) => {
    item?.children.map((inner: any) => {
      inner.isChoose = inner?.id == val?.id;
    });
  });
  accountPop.value.close();
};

const getUserData = () => {
  getUserList({ bookId: recordForm.value.bookId }).then((res) => {
    if (res.code == 200) {
      memberList.value = res.rows.map((item) => {
        item.isChoose = false;
        return item;
      });
      if (detailId.value == null) {
        recordForm.value.userName = res.rows[0]?.userName;
        recordForm.value.userId = res.rows[0]?.userId;
        recordForm.value.nickName = res.rows[0]?.nickName;
        res.rows[0].isChoose = true;
      } else {
        memberList.value.map((item) => {
          if (item.userId == recordForm.value.userId) {
            item.isChoose = true;
          } else {
            item.isChoose = false;
          }
        });
      }
    }
  });
};

const chooseUser = (data: any) => {
  memberList.value.map((item: any) => {
    if (item?.id == data?.id) {
      item.isChoose = true;
      recordForm.value.userName = item?.userName;
      recordForm.value.userId = item?.userId;
      recordForm.value.nickName = item?.nickName;
      memberPop.value.close();
    } else {
      item.isChoose = false;
    }
  });
};

const getDetail = (id: any) => {
  getBillDetail(id).then(async (res: any) => {
    if (res.code == 200) {
      res.data.money = String(fenToYuan(res.data.money));
      // res.data.bookTime = dayjs(new Date()).format("YYYY-MM-DD HH:mm:ss");
      if (res.data.money.indexOf("-") !== -1) {
        res.data.money = res.data.money.split("-")[1];
      }
      recordForm.type = res.data.type;
      recordForm.value = res.data;
      fileList.value =
        recordForm.value.detailImg == ""
          ? []
          : [{ url: previewPictures(recordForm.value.detailImg) }];
      await getBook();
    }
  });
};

const goback = () => {
  uni.switchTab({ url: "/pages/index" });
};

const openPop = (type: any) => {
  if (type === "classify") {
    classifyPop.value.open();
  } else if (type === "account") {
    accountPop.value.open();
  } else if (type === "member") {
    memberPop.value.open();
  }
};

const numChange = (val: any) => {
  console.log(val);
};

const setData = (val: any) => {
  recordForm.value.money = val.money;
};

const handleCancel = () => {
  showAdd.value = false;
  iconList.value = [];
  pageNum.value = 1;
  addForm.value = {
    classifyName: "",
    fileList: [],
    isChoose: false,
    categoryUrl: "",
  };
  getClassify();
  classifyPop.value.open();
};

const afterReads = (event: any) => {
  uni.showLoading({
    title: "上传中...",
  });
  let url = `${baseUrl}/minio/upload/file`;
  let file: any = null;
  file = isBill.value ? event.file.url : event.file[0].url;
  if (!file) {
    uni.showToast({ title: "文件路径无效", icon: "none", duration: 1000 });
    return;
  }
  update
    .$uploadForm(url, {}, file, getToken())
    .then((res) => {
      uni.showToast({ title: "上传成功", icon: "success", duration: 1000 });
      let row = JSON.parse(res.data);
      if (isBill.value) {
        recordForm.value.detailImg = row.data.name;
        fileList.value = [{ url: previewPictures(res.data.name) }];
      } else {
        addForm.value.fileList.push({
          name: row.data.name,
          url: previewPictures(row.data.name),
        });
      }
    })
    .catch((err) => {
      uni.showToast({ title: "上传失败", icon: "none", duration: 1000 });
    });
};

const handleConfirm = () => {
  addFormref.value.validate().then((valid) => {
    if (valid) {
      if (isAdd.value) {
        let data = {
          bookId: recordForm.value.bookId,
          categoryUrl: addForm.value.isChoose
            ? addForm.value.categoryUrl
            : addForm.value.fileList[0].name,
          name: addForm.value.classifyName,
          parentId:
            classifyType.value == "parent"
              ? ""
              : firstClassify.value.categoryBaseId,
          type: recordForm.value.type,
          hasChild: classifyType.value == "parent" ? "Y" : "N",
        };
        addClassifyType(data).then((res) => {
          if (res.code == 200 && res.data) {
            uni.showToast({
              title: "新增成功",
              icon: "success",
              duration: 1000,
            });
            handleCancel();
            // getClassify();
            // classifyPop.value.open();
          }
        });
      } else {
        let data = {
          bookId: recordForm.value.bookId,
          id: addForm.value.id,
          categoryUrl: addForm.value.isChoose
            ? addForm.value.categoryUrl
            : addForm.value.fileList[0].name,
          name: addForm.value.classifyName,
          parentId:
            classifyType.value == "parent"
              ? ""
              : firstClassify.value.categoryBaseId,
          type: addForm.value.type,
          hasChild: addForm.value.hasChild,
        };
        editClassifyType(data).then((res) => {
          if (res.code == 200 && res.data) {
            uni.showToast({
              title: "修改成功！",
              type: "success",
              icon: "success",
              duration: 1000,
            });
            handleCancel();
            // getClassify();
            // classifyPop.value.open();
          }
        });
      }
    }
  });
};

const deletePic = (event: any) => {
  fileList.value.splice(event.index, 1);
};

const deletePic2 = (event: any) => {
  addForm.value.fileList.splice(event.index, 1);
};
</script>
<template>
  <view class="add-accounts">
    <up-navbar :safeAreaInsetTop="false">
      <template #left>
        <view class="u-nav-slot">
          <up-icon name="arrow-left" size="19" @click="goback"></up-icon>
        </view>
      </template>
      <template #center>
        <view class="nav-select">
          <uni-data-select
            v-model="recordForm.bookId"
            :localdata="bookList"
            :clear="false"
            @change="bookChange"
          >
          </uni-data-select>
        </view>
      </template>
    </up-navbar>
    <view class="tab">
      <up-button
        type="primary"
        text="支出"
        :class="recordForm.type == 1 ? 'isSelect' : ''"
        @click="changeType(1)"
      ></up-button>
      <up-button
        type="primary"
        text="收入"
        :class="recordForm.type == 0 ? 'isSelect' : ''"
        @click="changeType(0)"
      ></up-button>
      <up-button
        type="primary"
        text="不计收支"
        :class="recordForm.type == 2 ? 'isSelect' : ''"
        @click="changeType(2)"
      ></up-button>
    </view>
    <view class="pay-detail">
      <view class="input-num">
        <up-text :text="recordForm.money" class="money-num"></up-text>
        <view class="upload-img">
          <up-upload
            :fileList="fileList"
            @afterRead="afterReads"
            @delete="deletePic"
            name="1"
            :maxCount="1"
            width="30"
            height="30"
            @click="isBill = true"
            :previewFullImage="true"
          ></up-upload>
          <span>拍照</span>
        </view>
      </view>
      <view class="account" @click="openPop('classify')">
        <up-icon name="calendar" size="35" color="#b0b0b0"></up-icon>
        <span class="text">分类</span>
        <span>{{ recordForm.categoryName }}</span>
      </view>
      <view class="account" @click="openPop('account')">
        <up-icon name="calendar" size="35" color="#b0b0b0"></up-icon>
        <span class="text">账户</span>
        <span>{{ recordForm.accountName }}</span>
      </view>
      <view class="account">
        <up-icon name="clock" size="35" color="#b0b0b0"></up-icon>
        <span class="text">时间</span>
        <up-datetime-picker
          hasInput
          :show="showDatePicker"
          v-model="recordForm.bookTime"
          mode="datetime"
          @confirm="showDatePicker = false"
        ></up-datetime-picker>
      </view>
      <view class="account" @click="openPop('member')">
        <up-icon name="man-add" size="35" color="#b0b0b0"></up-icon>
        <span class="text">成员</span>
        <span>{{ recordForm.nickName }}</span>
      </view>
      <view class="account">
        <up-icon name="order" size="35" color="#b0b0b0"></up-icon>
        <span class="text">备注</span>
        <up-textarea
          v-model="recordForm.remark"
          border="surround"
          placeholder="请输入内容"
        ></up-textarea>
      </view>
    </view>
    <KeyBoard
      @numChange="numChange"
      @setData="setData"
      :recordForm="recordForm"
      :detailId="detailId"
    />
    <uni-popup ref="classifyPop" type="bottom">
      <view class="classify-pop">
        <view class="pop-title">选择分类</view>
        <view class="bill-classify">
          <view class="left-classify">
            <view
              v-for="(item, index) in classifyList"
              :key="index"
              class="first-classify"
              :class="item.isChoose ? 'isCheck' : ''"
              @click="chooseFirstClassify(item)"
              @longpress="changeClassify(item)"
            >
              <image
                :src="previewPictures(item.categoryUrl)"
                class="classify-img"
              />
              {{ item.name }}
              <view
                class="delete"
                @click="handleDeleteClassify(item)"
                v-if="showFirstDelete"
                style="margin-left: 10rpx"
              >
                <up-icon name="close" size="16"></up-icon>
              </view>
            </view>
            <view class="addFirstClassify" @click="addClassify('parent')">
              <up-icon name="plus" size="26"></up-icon>
            </view>
            <view
              class="addFirstClassify"
              @click="showFirstDelete = !showFirstDelete"
            >
              <up-icon name="minus" size="26"></up-icon>
            </view>
          </view>
          <view class="right-classify">
            <view
              v-for="(inner, innerIndex) in nextClassify"
              :key="innerIndex"
              class="child-detail"
              @click="choseClassify(inner)"
              :style="{ background: inner.isChoose ? '#e3ad5b4a' : 'none' }"
              @longpress="changeClassify(inner)"
            >
              <view
                class="delete"
                @click="handleDeleteClassify(inner)"
                v-if="showDelete"
              >
                <up-icon name="close" size="16"></up-icon>
              </view>
              <image
                :src="previewPictures(inner.categoryUrl)"
                class="classify-img"
              />
              {{ inner.name }}
            </view>
            <view
              class="addClassify"
              @click="addClassify('child')"
              v-if="firstClassify.id !== null"
            >
              <up-icon name="plus" size="26"></up-icon>
            </view>
            <view
              class="addClassify"
              @click="showDelete = !showDelete"
              v-if="firstClassify.id !== null"
            >
              <up-icon name="minus" size="26"></up-icon>
            </view>
          </view>
        </view>
      </view>
    </uni-popup>

    <uni-popup ref="accountPop" type="bottom">
      <view class="account-pop">
        <view class="pop-title">选择账户</view>
        <view
          v-for="(item, index) in accountList"
          :key="index"
          style="margin-bottom: 30rpx"
        >
          <view class="account-first">
            <image
              :src="previewPictures(item.accountUrl)"
              class="account-img"
            />{{ item.name }}
          </view>
          <view class="account-children" v-if="item.children.length > 0">
            <view
              v-for="(inner, innerIndex) in item.children"
              :key="innerIndex"
              class="child-detail"
              @click="choseAccount(inner)"
            >
              <view class="name">
                <image
                  :src="previewPictures(inner.accountUrl)"
                  class="account-img"
                />
                {{ inner.name }}
              </view>
              <up-icon name="checkmark" color="red" v-if="inner.isChoose" />
            </view>
          </view>
        </view>
      </view>
    </uni-popup>

    <up-popup :show="showAdd" :round="5" mode="center">
      <view class="add-classify">
        <view class="title"> {{ isAdd ? "新增分类" : "修改分类" }} </view>
        <up-form
          labelPosition="left"
          :model="addForm"
          :rules="rules"
          ref="addFormref"
        >
          <up-form-item
            label="分类名称："
            prop="classifyName"
            borderBottom
            ref="item1"
            required
          >
            <up-input v-model="addForm.classifyName"></up-input>
          </up-form-item>
          <up-form-item
            label="分类图标："
            prop="classifyName"
            borderBottom
            ref="item1"
            required
          >
            <view>
              <up-upload
                :fileList="addForm.fileList"
                @afterRead="afterReads"
                @delete="deletePic2"
                name="1"
                multiple
                :maxCount="1"
                @click="isBill = false"
                :previewFullImage="true"
                width="40"
                height="40"
              ></up-upload>
              <view class="icon-list">
                <view
                  v-for="(item, index) in iconList"
                  :key="index"
                  class="account-img"
                  :class="item.isChoose ? 'choose-img' : ''"
                >
                  <image
                    :src="previewPictures(item.categoryUrl)"
                    @click="chooseIcon(item)"
                  />
                </view>
                <view class="more" @click="getMore" v-if="showMore"
                  >更多..</view
                >
              </view>
            </view>
          </up-form-item>
        </up-form>
        <view class="btn">
          <up-button
            text="确定"
            type="primary"
            size="small"
            @click="handleConfirm"
          ></up-button>
          <up-button text="取消" size="small" @click="handleCancel"></up-button>
        </view>
      </view>
    </up-popup>

    <uni-popup ref="memberPop" type="bottom">
      <view class="choose-member">
        <view class="pop-title">选择成员</view>
        <view class="member-list">
          <view
            v-for="(item, index) in memberList"
            :key="index"
            class="member-item"
            @click="chooseUser(item)"
          >
            <view class="nickname">
              <image :src="previewPictures(item.avatar)" class="member-img" />
              <span>{{ item.nickName }}</span>
            </view>
            <up-icon name="checkmark" color="red" v-if="item.isChoose" />
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<style scoped lang='scss'>
.add-accounts {
  width: 100vw;
  height: 100vh;
  background: #fff;
  color: #111;
  padding-top: 200rpx;

  :deep(.u-navbar--fixed) {
    top: 100rpx !important;
  }

  .nav-select {
    display: flex;

    :deep(.uni-select) {
      border: none;
      font-size: 35rpx;
    }

    :deep(.uni-select__selector) {
      width: 40vw;
      overflow: auto;
    }

    :deep(.uni-select__input-text) {
      color: #333 !important;
    }
  }
  .tab {
    display: flex;
    uni-button {
      width: 25vw;
      background: #ffd3a0;
      border: none;
    }

    .isSelect {
      background: rgb(253, 152, 2);
    }
  }
  .pay-detail {
    padding: 2vh 8vw;
    .input-num {
      border-bottom: 2rpx solid #333;
      display: flex;
      justify-content: space-between;
      margin-bottom: 1vh;

      .upload-img {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        :deep(.u-upload__button) {
          margin: 0;
          background-color: #fff;
        }
      }
      .money-num span {
        font-size: 30px;
      }
    }
    .account {
      display: flex;
      padding: 1vh 0;
      display: flex;
      align-items: center;
      border-bottom: 2rpx solid #dedede;

      .u-icon {
        margin-right: 20rpx;
      }

      .text {
        margin-right: 20rpx;
        color: #b0b0b0;
      }
    }
  }
  .bill-classify {
    display: flex;
    height: 50vh;
    background: #fff;
    // padding: 1vh 0 0;

    .left-classify {
      width: 30%;
      height: 100%;
      overflow: auto;
      border: 1px solid #e8e8e8;
      text-align: center;

      .first-classify {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 1vh 0;
      }

      .isCheck {
        background: rgb(241, 182, 93);
        color: #fff;
      }

      .addFirstClassify {
        height: 100rpx;
        display: flex;
        justify-content: center;
        align-items: center;

        .u-icon {
          padding: 1vw;
          border: 1px solid #646161;
          border-radius: 10rpx;
        }
      }
    }
    .right-classify {
      width: 70%;
      display: flex;
      flex-wrap: wrap;
      align-content: flex-start;
      padding: 1vh 3vw;
    }

    .child-detail {
      width: 15vw;
      // height: 15vw;
      padding: 10rpx 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      border-radius: 20rpx;
      position: relative;
      margin-right: 5vw;

      .delete {
        position: absolute;
        top: 0;
        right: -30rpx;
      }
    }

    .classify-img {
      width: 8vw;
      height: 8vw;
      border-radius: 50%;
      margin-bottom: 10rpx;
    }
    .addClassify {
      // width: 160rpx;
      // height: 160rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 20rpx;

      .u-icon {
        padding: 1vw;
        border: 1px solid #646161;
        border-radius: 50%;
      }
    }
  }
  .add-classify {
    width: 85vw;
    padding: 2vh 5vw;

    .title {
      font-size: 36rpx;
      text-align: center;
    }

    :deep(.u-form-item__body__left) {
      width: 22vw !important;
    }

    .icon-list {
      height: 20vh;
      overflow: auto;
      display: flex;
      flex-wrap: wrap;
      align-content: flex-start;
      .account-img {
        padding: 2vw;
        border-radius: 10rpx;
        uni-image {
          width: 60rpx;
          height: 60rpx;
        }
      }
      .choose-img {
        background: rgba(252, 190, 97, 0.521);
      }
      .more {
        color: #999;
        line-height: 80rpx;
        text-align: center;
      }
    }

    .btn {
      display: flex;
      justify-content: space-around;
      margin-top: 2vh;

      .u-button {
        width: 20vw;
      }
    }
  }
  .pop-title {
    text-align: center;
    height: 2vh;
    line-height: 2vh;
  }
  .choose-member {
    width: 100vw;
    height: 50vh;
    padding: 2vh 5vw;
    background: #fff;
    overflow: auto;

    .member-list {
      padding: 2vh 0;
      .member-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #dedede;
        padding: 1vh 0;
        .nickname {
          display: flex;
          align-items: center;
        }
      }
    }

    .member-img {
      width: 10vw;
      height: 10vw;
      margin-right: 20rpx;
    }
  }
  .classify-pop {
    width: 100vw;
    height: 50vh;
    padding: 2vh 0;
    background: #fff;
    overflow: auto;
  }
  .account-pop {
    width: 100vw;
    height: 50vh;
    padding: 2vh 5vw;
    background: #fff;
    overflow: auto;

    .account-first {
      display: flex;
      align-items: center;
      color: #999;
    }

    .account-children {
      .child-detail {
        margin: 10rpx 30rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid #dfdfdf;

        .name {
          display: flex;
          align-items: center;
        }
      }
    }
    .account-img {
      width: 8vw;
      height: 8vw;
      border-radius: 50%;
      margin: 0 10rpx 10rpx 0;
    }
  }
}
</style>