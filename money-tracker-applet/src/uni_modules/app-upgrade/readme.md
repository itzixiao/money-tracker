# app-upgrade

> uni-app 开发 APP 检查更新，弹窗提示，整包升级，可覆盖 tabbar，支持 android 和 ios.

### 使用介绍

1. 使用 HBuilderx 导入插件，在相关文件中引入

> `app_upgrade(callback,type)` 方法接收两个参数：   
>> `callback(versionCode)` 回调函数携带参数为当前应用版本号，例如 100，用来比较是否有新版本，返回对象 *{'status': 0 无新版本 | 1 有新版本,'changelog':'更新内容', 'path': '新apk地址'}*   
>>
>> `type` 0 主动升级，1 用户检测升级，默认为 0

2. **主动升级**，在 App.vue 中引入，在 onLaunch 应用生命周期中调用

```
import app_upgrade from '@/uni_modules/app-upgrade/js_sdk/index.js'

export default {
	onLaunch: function() {
		app_upgrade(async (versionCode)=>{
			//查询是否更新
			const { statusCode,data }=await uni.request({
				url:'https://xxx',
				data:{
					'versionCode':versionCode
				},
				method:'POST'
			})
			
			if(statusCode==200){
				return {
					changelog:data.changelog,
					status:data.status, // 0 无新版本 | 1 有新版本
					path:data.path // 新apk地址
				}
			}
		})
	}
}
```

3. **用户检测升级**，在检测页面引入并调用

```
import app_upgrade from '@/uni_modules/app-upgrade/js_sdk/index.js'

onUpdate(){
	app_upgrade(async (versionCode)=>{
		//查询是否更新
		const { statusCode,data }=await uni.request({
			url:'https://xxx',
			data:{
				'versionCode':versionCode
			},
			method:'POST'
		})
		
		if(statusCode==200){
			return {
				changelog:data.changelog,
				status:data.status,
				path:data.path
			}
		}else{
			uni.showToast({
				title:'当前已是最新版',
				icon:'none'
			})
		}
	},1)
}
```

4. 在项目根目录下创建 `upgrade-config.js` 配置文件，写入以下配置，下列对象的键值根据自己实际情况设置

```
export default {
	upgrade:{
		packageName:'',
		appleId:'',
		upType:0,
		timer:24,
		icon:'/static/logo.png',
		title:'发现新版本',
		confirmText:'立即更新',
		cancelTtext:'稍后再说',
		confirmBgColor:'#409eff',
		showCancel:true,
		titleAlign:'left',
		descriAlign:'left'
	}
}
```

5. upgrade-config.js `upgrade` 配置说明，全部非必填

|字段|描述|默认值|
|--	|--	|-- |
|upType|安卓升级方式，0 应用内更新 / 1 去应用商店更新|0|
|packageName|android 包名，upType=1 时必填| |
|appleId|ios 应用 id，<a href="https://appstoreconnect.apple.com" target="_blank">app store</a>中获取，ios必填| |
|timer|主动检测间隔小时|24|
|icon|弹窗图标| 如果为空使用内置Base64图片|
|title|弹窗标题|发现新版本|
|titleAlign|弹窗标题对齐方式，left / center|left|
|confirmText|弹窗确定文字|立即更新|
|confirmBgColor|弹窗确定背景颜色|#409eff|
|cancelTtext|弹窗取消文字|稍后再说|
|showCancel|弹窗是否显示取消，true / false|true|
|descriAlign|更新描述对齐方式，left / center|left|