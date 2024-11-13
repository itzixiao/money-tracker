<template>
	<view class="login-bg">
		<image src='@/static/images/banner/banner.png' id="imgs"></image>
		<view class="t-login">
			<form class="cl">
				<view class="t-a">
					<image src="https://zhoukaiwen.com/img/loginImg/user.png"></image>
					<input name="userName" placeholder="请输入账号" maxlength="11" v-model="userName" />
				</view>
				<view class="t-a">
					<image src="https://zhoukaiwen.com/img/loginImg/pwd.png"></image>
					<input type="password" name="code"  placeholder="请输入密码" v-model="pwd" />
				</view>
				<button @click.prevent="accountLogin()">登 录</button>
				<view class="t-c">
					<text class="t-c-txt" @click.prevent="reg()">注册账号</text>
					<text @click="goBack()">返回首页</text>
				</view>
			</form>
			<view class="t-f"><text>—————— 其他登录方式 ——————</text></view>
			<view class="t-e cl">
				<!-- <view class="t-g" @click="wxLogin()">
					<image src="https://zhoukaiwen.com/img/loginImg/wx2.png"></image>
				</view>
				<view class="t-g" @click="zfbLogin()">
					<image src="https://zhoukaiwen.com/img/loginImg/qq2.png"></image>
				</view>
				<view class="t-g" @click="wbLogin()">
					<image src="https://zhoukaiwen.com/img/loginImg/wb.png"></image>
				</view> -->
				<up-button icon="weixin-fill" shape="circle" color="#05c160" class="login-btn" type="primary" text="微信一键登录"
				@click="wxLogin()">
				</up-button>
			</view>
		</view>
		<image class="img-a" src="https://zhoukaiwen.com/img/loginImg/bg1.png"></image>
	</view>
</template>

<script setup>
import { ref } from 'vue';
import { wxLogin, userName, pwd ,accountLogin} from '@/utils/weChatQuickLogin.ts'

// 当前登录按钮操作
const login = () => {
	if (!userName.value) {
		uni.showToast({ title: '请输入账号', icon: 'none' });
		return;
	}
	// if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(userName.value)) {
	// 	uni.showToast({ title: '请输入正确手机号', icon: 'none' });
	// 	return;
	// }
	if (!pwd.value) {
		uni.showToast({ title: '请输入密码', icon: 'none' });
		return;
	}

};

// 立刻注册
const reg = () => {
	uni.showToast({ title: '注册账号', icon: 'none' });
};

const goBack = () => {
	uni.navigateBack();
};

const zfbLogin = () => {
	// 支付宝登录逻辑
};

const wbLogin = () => {
	// 微博登录逻辑
};
</script>

<style scoped>
.img-a {
	width: 100%;
	position: absolute;
	bottom: 0;
}

.login-bg {
	width: 100%;
	height: 100vh;
	overflow: hidden;
}

#imgs {
	width: 100%;
	height: 100%;
}

.t-login {
	width: 700rpx;
	padding: 55rpx;
	font-size: 28rpx;
	background-color: #ffffff;
	border-radius: 20rpx;
	box-shadow: 0 5px 7px 0 rgba(0, 0, 0, 0.15);
	z-index: 9;
	position: fixed;
	left: 50%;
	top: 300rpx;
	margin-left: -350rpx;
}

.t-login button {
	font-size: 28rpx;
	background: linear-gradient(to right, #F6C41C, #F36A27);
	color: #fff;
	height: 90rpx;
	line-height: 90rpx;
	border-radius: 50rpx;
}

.t-login input {
	padding: 0 20rpx 0 120rpx;
	height: 90rpx;
	line-height: 90rpx;
	margin-bottom: 50rpx;
	background: #f6f6f6;
	border: 1px solid #f6f6f6;
	font-size: 28rpx;
	border-radius: 50rpx;
}

.t-login .t-a {
	position: relative;
}

.t-login .t-a image {
	width: 40rpx;
	height: 40rpx;
	position: absolute;
	left: 40rpx;
	top: 28rpx;
}

.t-login .t-c {
	display: flex;
	justify-content: space-between;
	color: #666666;
	margin: 30rpx 0 40rpx 0;
}

.t-login .t-e {
	text-align: center;
	width: 600rpx;
	margin: 40rpx auto 0;
}

.t-login .t-g {
	float: left;
	width: 33.33%;
}

.t-login .t-e image {
	width: 70rpx;
	height: 70rpx;
}

.t-login .t-f {
	text-align: center;
	margin: 80rpx 0 0 0;
	color: #999;
}

.cl {
	zoom: 1;
}

.cl:after {
	clear: both;
	display: block;
	visibility: hidden;
	height: 0;
	content: '\20';
}
</style>