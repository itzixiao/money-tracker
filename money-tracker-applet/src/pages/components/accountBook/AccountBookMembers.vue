<script setup lang='ts'>
import { reactive, ref, onMounted, getCurrentInstance } from 'vue'
import { bookUser, updateUser, inviteBookUser } from '@/api/ccountBook/index';
import { useRoute } from 'vue-router';
import { previewPictures } from '@/utils/ruoyi'
import { onLoad } from "@dcloudio/uni-app"
defineOptions({
  name: "",
})
const route = useRoute();
const memberId = ref('');
const bookList: any = ref([
  // { name: '老婆', id: 0 },
]);
const title = ref('邀请成员')
const formData = ref({
  nickName: ''
})
const bookListModel = ref<boolean>(false)
// 邀请成员
const InviteMembers = () => {
  clear()
  title.value = '邀请成员'
  bookListModel.value = true;
}
const handleCancel = () => {
  bookListModel.value = false;
  clear()
};

// 确定
const handelSure = () => {
  if (title.value == '邀请成员') {
    inviteBookUser({
      bookId: memberId.value,
      userName: formData.value.nickName
    }).then(res => {
      if (res.code == 200) {
        uni.showToast({
          title: '邀请成功',
          duration: 1000,
        });
        bookListModel.value = false;
        getBookUser()
      }
    })
  } else {
    updateUser({ ...formData.value }).then(res => {
      if (res.code == 200) {
        uni.showToast({
          title: '修改成功',
          duration: 1000,
        });
        bookListModel.value = false;
        getBookUser()
      }
    })
  }
};

// 清空表单
const clear = () => {
  formData.value = {
    nickName: ''
  }
}

// 修改 
const handelBook = (row, id) => {
  clear()
  title.value = '修改账本用户'
  formData.value = JSON.parse(JSON.stringify(row));
  bookListModel.value = true;
}

// 列表
const getBookUser = () => {
  bookUser({
    bookId: memberId.value
  }).then(res => {
    if (res.code == 200) {
      bookList.value = res.rows
    }
  })
}

onLoad((option:any)=>{
  memberId.value = option.id
  getBookUser()
})
onMounted(() => {
})
</script>
<template>
  <view>
    <NavBar :title="'账本成员'" :leftShow="true"></NavBar>
    <view id="warp">
      <view id="header">
        <view>已加入成员（{{ bookList.length }}）</view>
        <!-- <view style="color: #FD8702;">角色管理</view> -->
      </view>
      <view id="mian">
        <view id="mian_headeer">
          <view>管理员</view>
          <view>除不能删除账本,拥有和账本主人一致的最大权限</view>
        </view>
        <view id="list" v-for="(item, index) in bookList" @click="handelBook(item, index)">
          <view>
            <image v-if="item.avatar" :src="previewPictures(item.avatar)"> </image>
            <image v-else></image>
            <view>
              <view>{{ item.userName }}</view>
              <view>{{ item.nickName }}</view>
            </view>
            <view id="owner" v-if="item.auth == 0">成员</view>
            <view id="owner" v-if="item.auth == 1">管理员</view>
          </view>
          <view><up-icon name="arrow-right" color="#ccc" size="15"></up-icon></view>
        </view>
      </view>
      <view id="addAb">
        <up-button text="邀请成员" @click="InviteMembers" icon="plus-circle" type="warning" style="background: #FD8702;"
          shape="circle"></up-button>
      </view>
      <!-- 邀请成员 -->
      <up-popup :show="bookListModel" :round="5" mode="center" @close="handleCancel">
        <view id="model_">
          <view id="model_title">{{ title }}</view>
          <up-input placeholder="请输入" border="surround" v-model="formData.nickName"></up-input>
          <view id="btn">
            <up-button text="确定" @click="handelSure" type="primary" color="#FD8702"></up-button>
            <up-button text="取消" @click="handleCancel"></up-button>
          </view>
        </view>
      </up-popup>
    </view>
  </view>
</template>

<style scoped lang='scss'>
#warp {
  padding: 0 20rpx 120rpx 20rpx;
  #header {
    display: flex;
    justify-content: space-between;
    padding: 10rpx;
    font-size: 24rpx;
    color: #888;
  }

  #mian {
    overflow: hidden;
    background: #fff;
    border-radius: 10rpx;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    #mian_headeer {
      padding: 20rpx 20rpx;
      box-sizing: border-box;

      >view:last-child {
        margin-top: 5rpx;
        font-size: 22rpx;
        color: #888;
      }
    }

    #list {
      border-top: 2rpx solid #eee;
      height: 90rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 20rpx;

      >view:first-child {
        display: flex;
        font-size: 28rpx;
        align-items: center;
      }

      #owner {
        background: #FDEFED;
        color: #FD8702;
        font-size: 20rpx;
        padding: 4rpx 10rpx;
        border-radius: 5rpx;
        margin-left: 15rpx;
      }

      image {
        vertical-align: middle;
        width: 50rpx;
        height: 50rpx;
        border-radius: 50%;
        margin-right: 15rpx;
      }
    }
  }

  #addAb {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    background: #fff;
    width: 100%;
    height: 100rpx;
    position: fixed;
    bottom: 0;
    left: 0;
    padding: 20rpx;
    box-sizing: border-box;
  }

  #model_ {
    width: 600rpx;
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

    .u-button {
      width: 200rpx;
    }
  }

}
</style>