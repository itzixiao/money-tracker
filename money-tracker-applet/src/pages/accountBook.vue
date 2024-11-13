<script setup lang='ts'>
import config from '@/config'
import { reactive, ref, onMounted, getCurrentInstance } from 'vue';
import { addBook, updateBook, bookALL, delBook } from '@/api/ccountBook/index'
import update from '@/utils/update.js';
import { getToken } from '@/utils/auth'
const { proxy } = getCurrentInstance();

const baseUrl = config.baseUrl

const debounce = proxy.debounce;
const bookName = ref<any>();
const handleShow = ref<boolean>(false); // 控制弹窗显示的变量
const tabHeader = reactive([
  { name: '全部', id: 0 },
  { name: '我创建', id: 2 },
  { name: '我参与', id: 1 },
]);
const activeTab = ref(0)
const ActibeIndex = ref()
const ActibeRow = ref()
const pageNum = ref(1);
const pageSize = ref(10);
const formData = ref<any>({
  bookName: '',
  userAudit: '',  // （0-不需要 1-需要管理员审核）
  defaultBook: '' // 是否默认账本(0-否 1是）
})
const tabList = ref<any>([]);
const handelList = ref([
  {
    name: '账本成员',
  },
  {
    name: '账本设置',
    color: '#55AAFF',
  },
  {
    name: '删除账本',
    color: 'red',
  },
])
const titles = ref<string>('')
const accountBookModel = ref<boolean>(false)
const fileList = ref<any>([])
// 操作
const handles = (row: any, id: any) => {
  handleShow.value = true
  ActibeIndex.value = id
  ActibeRow.value = row
}
const selectClick = async (row: { name: string }) => {
  clear();
  if (row.name === '删除账本') {
    const res = await uni.showModal({
      title: '确认删除',
      content: `您确定要删除${ActibeRow.value.bookName}账本吗？`,
    });
    if (res.confirm) {
      try {
        await delBook(ActibeRow.value.id);
        uni.showToast({
          title: '删除成功',
          duration: 1000,
        });
        await resetOperation();
        handleShow.value = false;
      } catch (error) {
        uni.showToast({
          title: '删除失败，请重试',
          icon: 'none',
        });
      }
    }
  } else if (row.name === '账本设置') {
    titles.value = '账本设置账本';
    accountBookModel.value = true;
    formData.value = { ...ActibeRow.value };
    if (formData.value.bookAvatar) {
      const parsedAvatar = formData.value.bookAvatar;
      fileList.value.push({
        name: parsedAvatar,
        url: proxy.previewPictures(parsedAvatar)
      });
    }
  } else if (row.name === '账本成员') {
    uni.navigateTo({
      url: `/pages/components/accountBook/AccountBookMembers?id=${ActibeRow.value.id}` // 传递参数
    });
  }
};

// 创建新账本
const handelAdd = () => {
  clear()
  titles.value = '创建新账本'
  accountBookModel.value = true
}


const afterReads = (event: any) => {
  let url = `${baseUrl}/minio/upload/file`;
  let file: any = null;

  /*#ifdef H5*/
  file = event.file.file.path;
  /*#endif*/
  /*#ifdef APP-PLUS*/
  file = event.file.url;
  /*#endif*/
  /*#ifdef MP-WEIXIN*/
  file = event.file.url;
  /*#endif*/

  if (!file) {
    uni.showToast({ title: '文件路径无效', icon: 'none' });
    return;
  }

  let data = {};
  uni.showLoading({
    title: '上传中...',
  });

  update.$uploadForm(url, data, file, getToken()).then(res => {
    uni.showToast({ title: '上传成功', icon: 'success' });
    let row = JSON.parse(res.data);
    fileList.value.push({
      name: row.data.fileName,
      url: file
    });

  }).catch((err) => {
    uni.showToast({ title: '上传失败', icon: 'none' });
  });
};


// 删除图片
const deletePic = (event: any) => {
  fileList.value.splice(event.index, 1)
};

// 确定
const handleConfirm = () => {
  let file: string = ''
  if (fileList.value && fileList.value.length > 0) {
    file = fileList.value[0].name;
  }
  if (titles.value == '创建新账本') {
    addBook({
      ...formData.value,
      bookAvatar: file
    }).then(res => {
      accountBookModel.value = false;
      uni.showToast({ title: '新增成功', icon: 'success' });
      resetOperation()
    })
  } else {
    updateBook({
      ...formData.value,
      bookAvatar: file
    }).then(res => {
      accountBookModel.value = false;
      uni.showToast({ title: '账本设置成功', icon: 'success' });
      resetOperation()
    })
  }

};
// 清空表单
const clear = () => {
  formData.value = {
    bookName: '',
    fileList: [],
    defaultBook: '',
    userAudit: ''
  }
  fileList.value = []
}
// 取消
const handleCancel = () => {
  accountBookModel.value = false;
  clear()
};
const tabClick = (e: any) => {
  activeTab.value = e.id
  resetOperation()
}

const resetOperation = () => {
  pageNum.value = 1; // 重置页码
  tabList.value = []; // 清空当前列表
  hasLoadedData = false; // 重置加载状态
  getList(); // 重新加载数据
}

// 账本列表
let totalItems = 1; // 总条数
let isLoading = false; // 加载状态
let hasLoadedData = false; // 新增标志
const getList = () => {
  if (isLoading || (hasLoadedData && tabList.value.length >= totalItems)) return;

  isLoading = true;

  bookALL({
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    auth: activeTab.value,
    bookName: bookName.value
  }).then((res: any) => {
    isLoading = false;
    if (res.code == 200) {
      totalItems = res.total;
      tabList.value = tabList.value.concat(res.rows);
      hasLoadedData = true;
    }
  }).catch(() => {
    isLoading = false;
  });
}

const clearList = () => {
  bookName.value = ''
  resetOperation()
}

const handleInput = debounce((event: any) => {
  pageNum.value = 1;
  resetOperation()
}, 300);

const bookAvatarName = (avatar: any) => {
  if (avatar && typeof avatar === 'string' && avatar.trim() !== '') {
    try {
      if (avatar) {
        return avatar;
      }
    } catch (error) {
      return null;
    }
  }
  return null;
};

const previewImage = (imageUrl: any) => {
  uni.previewImage({
    urls: [imageUrl],
    current: imageUrl
  });
}

const scrolltolower = (e: any) => {
  pageNum.value += 1;
  getList();
};
const scrolltoupper = (e: any) => {
};

onMounted(() => {
  getList()
});
</script>
<template>
  <div id="warp">
    <view id="query">
      <up-input placeholder="搜索我的账本" size="small" shape="circle" prefixIcon="search" border="surround"
        v-model="bookName" id="ipt" @clear="clearList" @change="handleInput" clearable></up-input>
    </view>
    <!-- tab -->

    <up-tabs :list="tabHeader" lineWidth="30" lineColor="#FD8702" :activeStyle="{
      color: '#FD8702',
      fontWeight: 'bold',
      transform: 'scale(1.05)'
    }" :inactiveStyle="{
      color: '#606266',
      transform: 'scale(1)'
    }" itemStyle="padding-left: 15px; padding-right: 15px; height: 34px;" @click="tabClick">
    </up-tabs>
    <scroll-view class="book_list" :scroll-y="true" :scroll-with-animation="true" @scrolltolower="scrolltolower"
      @scrolltoupper="scrolltoupper">
      <view id="talbe_con">
        <view v-if="tabList && tabList.length > 0" v-for="(item, index) in tabList" :key="index" id="talbe_con_lis">
          <image v-if="bookAvatarName(item.bookAvatar || '')"
            :src="proxy.previewPictures(bookAvatarName(item.bookAvatar))" alt="Book Avatar"
            @click="previewImage(proxy.previewPictures(bookAvatarName(item.bookAvatar)))" />
          <image v-else src="@/static/images/accountBook/accountbook.png" alt="Default Avatar" />
          <view id="cont">
            <view>
              <view>
                <text>{{ item.bookName }}</text>
                <text style="color: #FD8702;" v-if="item.defaultBook == 1">（默认账本）</text>
              </view>
              <view>我创建 | 1人 </view>
            </view>
            <view @click="handles(item, index)"><up-icon name="more-dot-fill"></up-icon></view>
          </view>
        </view>
        <up-empty mode="list" v-else>
        </up-empty>
      </view>
    </scroll-view>

    <view id="addAb">
      <up-button text="创建新账本" icon="plus-circle" type="warning" style="background: #FD8702;" shape="circle"
        @click="handelAdd"></up-button>
    </view>
  </div>
  <!-- 操作框 -->
  <up-action-sheet :actions="handelList" :show="handleShow" cancelText="取消" @select="selectClick"
    @close="handleShow = false"></up-action-sheet>

  <!-- 创建新账本弹框 -->
  <up-popup :show="accountBookModel" :round="5" mode="center" @close="handleCancel">
    <view id="model_">
      <view id="model_title">{{ titles }}</view>
      <up-row customStyle="margin-bottom: 20rpx">
        <up-col span="3">
          <view id="label_bookName">账本名称：</view>
        </up-col>
        <up-col span="9">
          <up-input placeholder="请输入账本名称" border="surround" v-model="formData.bookName"></up-input>
        </up-col>
      </up-row>
      <up-row>
        <up-col span="3">
          <view id="label_bookName">账本图片：</view>
        </up-col>
        <up-col span="9">
          <up-upload :fileList="fileList" @afterRead="afterReads" @delete="deletePic" name="1" :maxCount="1"
            :previewFullImage="false"></up-upload>
        </up-col>
      </up-row>
      <up-row customStyle="margin-bottom: 20rpx">
        <up-col span="3">
          <view id="label_bookName">默认账本：</view>
        </up-col>
        <up-col span="9">
          <up-radio-group v-model="formData.defaultBook">
            <up-radio label="是" :name="1" style="margin-right: 20rpx;">是</up-radio>
            <up-radio label="否" :name="0">否</up-radio>
          </up-radio-group>
        </up-col>
      </up-row>
      <!-- <up-row>
        <up-col span="12">
          <view id="label_bookName">成员加入账本是否需要审核：</view>
        </up-col>
      </up-row> -->
      <!-- <up-radio-group v-model="formData.userAudit">
        <up-radio label="需要管理员审核" :name="1" style="margin-right: 20rpx;"></up-radio>
        <up-radio label="不需要" :name="0"></up-radio>
      </up-radio-group> -->
      <view id="btn">
        <up-button text="确定" type="primary" @click="handleConfirm" color="#FD8702"></up-button>
        <up-button text="取消" @click="handleCancel"></up-button>
      </view>
    </view>
  </up-popup>

  <TabBar :currPage="4" />
</template>

<style scoped lang='scss'>
page {
  background: #fff;
}

#warp {
  /*#ifdef APP-PLUS*/
  width: 100%;
  height: 100vh;
  /*#endif*/
  /*#ifdef MP-WEIXIN*/
  width: 100%;
  height: 100vh;
  /*#endif*/
  background: #fff;
  box-sizing: border-box;
}

.book_list {
  height: calc(100vh - 420rpx);
  /*#ifdef H5*/
  height: calc(100vh - 470rpx);
  /*#endif*/
  overflow: auto;
}

#query {
  padding: 0 30rpx 30rpx 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;

  #ipt {
    height: 60rpx;
  }
}

#talbe_con {
  box-sizing: border-box;
  padding: 18px;

  #talbe_con_lis {
    align-items: center;
    display: flex;
    margin-bottom: 30rpx;
  }

  image {
    width: 60rpx;
    height: 65rpx;
    border-radius: 5rpx;
    margin-right: 20rpx;
  }

  #cont {
    flex: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;

    >view:first-child {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      font-size: 28rpx;
      font-weight: 559;

      >view:last-child {
        margin-top: 10rpx;
        color: #ccc;
        font-size: 20rpx;
      }
    }
  }
}

#addAb {
  padding: 0 100rpx;
}

#model_ {
  width: 650rpx;
  padding: 40rpx;
  box-sizing: border-box;
}

#model_title {
  font-size: 38rpx;
  font-weight: 600;
  margin-bottom: 50rpx;
  text-align: center;
}

#btn {
  display: flex;
  justify-content: space-between;
  margin-top: 60rpx;

  .u-button{
    width: 180rpx !important;
  }
}

#label_bookName {
  font-size: 24rpx;
  font-weight: 600;
  margin-bottom: 16rpx;
}
</style>