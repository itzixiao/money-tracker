<template>
    <view class="containers">
        <!-- 表单组件 -->
        <div id="formRef">
            <up-form ref="formRef">
                <!-- 手机号输入框 -->
                <up-form-item label="">
                    <up-input v-model="mobile" type="number" placeholder="请输入手机号" />
                </up-form-item>

                <!-- 短信验证码输入框 -->
                <up-form-item label="">
                    <up-input placeholder="请输入验证码" v-model="smsCode">
                        <template #suffix>
                            <up-code ref="uCodeRef" @change="codeChange" seconds="60" changeText="X秒重新获取"></up-code>
                            <up-button @tap="getCode" :text="tips" id="sms_b" type="success" size="mini"></up-button>
                        </template>
                    </up-input>
                </up-form-item>

                <!-- 密码输入框 -->
                <up-form-item label="">
                    <up-input v-model="password" type="password" placeholder="设置密码" />
                </up-form-item>

                <!-- 确认密码输入框 -->
                <up-form-item label="">
                    <up-input v-model="confirmPassword" type="password" placeholder="确认密码" />
                </up-form-item>

                <!-- 用户协议勾选框 -->
                <up-form-item>
                    <up-checkbox-group v-model="agree">
                        <up-checkbox :value="true"></up-checkbox>
                        <text class="text-blue" style="margin: 8rpx 0 0 5rpx;">勾选同意《同意用户服务协议》</text>
                    </up-checkbox-group>
                </up-form-item>

                <!-- 注册按钮 -->
                <up-button type="success" @click="handleSubmit" formType="submit" id="btn">立即注册</up-button>

                <view @click="AlreadyHaveAnAccount" id="Account">已有账户，立即登录</view>
            </up-form>
        </div>
    </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { register } from '@/api/login'

// 响应式变量
const mobile = ref('');           // 手机号
const smsCode = ref('');          // 短信验证码
const password = ref('');         // 密码
const confirmPassword = ref('');  // 确认密码
const agree = ref([]);            // 是否同意协议，使用数组来支持多个复选框

// 按钮状态
const uCodeRef: any = ref(null);
const tips = ref('');

// 获取短信验证码函数
const getCode = () => {
    if (!/^1[3-9]\d{9}$/.test(mobile.value)) {
        uni.showToast({ title: '请输入有效的手机号', icon: 'none' });
        return;
    }
    if (uCodeRef.value.canGetCode) {
        // 模拟向后端请求验证码
        uni.showLoading({
            title: '正在获取验证码',
        });
        setTimeout(() => {
            uni.hideLoading();
            // 这里此提示会被start()方法中的提示覆盖
            uni.showToast({ title: '验证码已发送', icon: 'none' });
            // 通知验证码组件内部开始倒计时
            uCodeRef.value.start();
        }, 2000);
    } else {
        uni.showToast({ title: '倒计时结束后再发送', icon: 'none' });
    }

};


// 表单提交处理函数
const handleSubmit = () => {
    // 校验手机号
    if (!/^1[3-9]\d{9}$/.test(mobile.value)) {
        uni.showToast({ title: '请输入有效的手机号', icon: 'none' });
        return;
    }

    // 校验短信验证码
    if (!smsCode.value) {
        uni.showToast({ title: '验证码不能为空', icon: 'none' });
        return;
    }

    // 校验密码
    if (!password.value) {
        uni.showToast({ title: '密码不能为空', icon: 'none' });
        return;
    }

    // 校验确认密码
    if (confirmPassword.value !== password.value) {
        uni.showToast({ title: '密码不匹配', icon: 'none' });
        return;
    }

    // 校验协议
    if (agree.value.length === 0) {
        uni.showToast({ title: '请同意用户服务协议', icon: 'none' });
        return;
    }
    
    // 注册逻辑
    let data = {
        username: mobile.value,
        smsCode: smsCode.value,
        password: password.value
    }
    register(data).then(res => {
        if(res.code == 200) {
            uni.showToast({ title: '注册成功', icon: 'success' });
            uni.navigateTo({
                url: '/pages/login'
            });
        }
    })
};

const AlreadyHaveAnAccount = () => {
    uni.navigateBack();
}
// 处理验证码变化的逻辑
const codeChange = (text: any) => {
    tips.value = text;
};

</script>
<style scoped>
.containers {
    background: #fff;
    box-sizing: border-box;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

#formRef {
    width: 90%;
}

#btn {
    background: #FD8702;
    border: none;
    border-radius: 50px;
    margin: 40rpx 0;
}


.disabled {
    opacity: 0.5;
}

#sms_b {
    background: none;
    border: none;
    color: #FD8702;
}

#Account {
    text-align: center;
    color: #B0B0B0;
    margin-top: 40rpx;
}
</style>