import { login, logout, getInfo } from "@/api/login";
import { getBookList } from "@/api/add";
import { getToken, setToken, removeToken } from "@/utils/auth";
import defAva from "@/static/images/profile.jpg";
import { defineStore } from "pinia";
import { previewPictures } from '@/utils/ruoyi'
export interface LoginForm {
  username: string;
  password: string;
  code: string;
  uuid: string;
}

const useUserStore = defineStore("user", {
  state: () => ({
    token: getToken(),
    name: uni.getStorageSync('name') || '',
    avatar: uni.getStorageSync('avatar') || '',
    roles: Array(),
    permissions: [],
    bookId: null,
    bookList: Array()
  }),
  actions: {
    // 设置用户头像
    setAvatar(avatar: any) {
      this.avatar = avatar;
      uni.setStorageSync('avatar', avatar)
    },
    // 设置用户名
    setName(name: any) {
      this.name = name;
      uni.setStorageSync('name', name)
    },
    // 登录
    login(userInfo: LoginForm) {
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid)
          .then((res: any) => {
            setToken(res.token);
            this.token = res.token;
            resolve(null);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取用户信息
    getInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res: any) => {
            uni.setStorageSync('userId', JSON.stringify(res.user.userId));
            const user = res.user;
            const avatar =
              user.avatar == "" || user.avatar == null
                ? defAva
                : user.avatar;

            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = res.roles;
              this.permissions = res.permissions;
            } else {
              this.roles = ["ROLE_DEFAULT"];
            }
            useUserStore().setAvatar(avatar);  
            useUserStore().setName(user.userName);      
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 退出系统
    logOut() {
      return new Promise<null>((resolve, reject) => {
        logout()
          .then(() => {
            this.token = "";
            this.roles = [];
            this.permissions = [];
            this.name = "";
            this.avatar = "";
            removeToken();
            resolve(null);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取默认账本
    getBookForm() {
      return new Promise((resolve, reject) => {
        getBookList({}).then((res) => {
          if (res.code == 200) {
            this.bookList = res.rows.map((item: any) => {
              item.text = item.bookName;
              item.value = item.id;
              return item;
            });
            uni.setStorageSync('bookList', this.bookList);
            if (uni.getStorageSync('bookId') == '') {
              this.bookId = res.rows[0]?.id;
              uni.setStorageSync('bookId', this.bookId);
            }
            resolve(null);
          }
        })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
});

export default useUserStore;
