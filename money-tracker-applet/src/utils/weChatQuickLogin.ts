import { wechatLogin } from "@/api/oauth"
import modal from "@/plugins/modal"
import { setToken } from '@/utils/auth';
import { ref, onMounted } from "vue";
import useUserStore from '@/store/modules/user'
import { previewPictures } from '@/utils/ruoyi'
// const userStore = useUserStore()

export const loginPopup = ref(false)
export let avatarUrl = ref();
export const userName = ref(''); // 账号
export const pwd = ref(''); // 密码
export let timer = ref(0)
// 微信一键登录
export const wxLogin = () => {
    modal.loading('绑定微信中...')
    uni.login({
        provider: 'weixin',
        success: (res) => {
            if (res.code) {
                wechatLogin({
                    "jsCode": res.code,
                    "phone": ""
                }).then(res => {
                    if (res.data.token != null) {
                        setToken(res.data.token);
                        modal.msgSuccess('绑定成功')
                        modal.closeLoading();
                        useUserStore().setAvatar(res.data.avatarUrl);  // 设置头像
                        useUserStore().setName(res.data.nickName);      // 设置名称
                        loginPopup.value = false
                        getAvatarUrl()
                        const pages = getCurrentPages();
                        if (pages.length > 1) {
                            uni.navigateBack();
                        } else {
                            uni.reLaunch({
                                url: '/pages/mine'
                            });
                        }
                    }

                });
            } else {
                console.error('登录失败！' + res.errMsg);
            }
        },
        fail: (err) => {
            console.error('uni.login 调用失败', err);
        }
    });
}

// 账号密码登陆
export const Login = () => {
    loginPopup.value = false
    uni.navigateTo({
        url: '/pages/accountPassword'
    });
}

export const accountLogin = () => {
    modal.loading('登录中...')
    uni.login({
        provider: 'weixin',
        success: (res) => {
            if (res.code) {
                wechatLogin({
                    "jsCode": res.code,
                    "phone": "",
                    bindAccount: true,
                    userName: userName.value,
                    password: pwd.value
                }).then(res => {
                    if (res.data.token != null) {
                        setToken(res.data.token);
                        modal.msgSuccess('登陆成功')
                        modal.closeLoading();
                        useUserStore().setAvatar(res.data.avatarUrl);  // 设置头像
                        useUserStore().setName(res.data.nickName);      // 设置名称
                        getAvatarUrl()
                        uni.navigateBack()
                    }
                });
            } else {
                console.error('登录失败！' + res.errMsg);
            }
        },
        fail: (err) => {
            console.error('uni.login 调用失败', err);
        }
    });
}

export function getAvatarUrl() {
    timer.value++
}