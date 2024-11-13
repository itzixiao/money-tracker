# lsj-upload

### 插件地址：https://ext.dcloud.net.cn/plugin?id=5459

### 不清楚使用方式可点击右侧导入示例项目运行完整示例
### 此次更新2.0与1.0使用方式略有差异，已使用1.0的同学自行斟酌是否更新到2.0版本！！！

使用插件有任何问题欢迎加入QQ讨论群：
### 插件交流群4：413918560
------
- 以下群如无空位会被拒绝
- 群3：667530868（已满）
- 群2：469580165（已满）
- 群1：701468256（已满）

若能帮到你请高抬贵手点亮5颗星~
------
## 重要提示
### 组件如果在scroll-view内使用需要自己监听scroll事件，并在滚动结束的时候调用一次show（查看下方scroll示例）！！

### 控件的height、width应与slot自定义内容宽高度保持一致。
### nvue窗口只能使用固定模式position=absolute 。
### show() 当DOM重排后在this.$nextTick内调用show()，控件定位会更加准确。
### hide() APP端webview层级比view高，如不希望触发点击时，应调用hide隐藏控件，反之调用show。
### APP端请优先联调Android,上传成功后再运行iOS端。
### 若iOS端跨域服务端同学实在配置不好，可把hybrid下html目录放到服务器去，同源则不存在跨域问题。
### 小程序端因hybrid不能使用本地HTML，所以插件提供的是从微信消息列表拉取文件并选择，请知悉。
### 关于很多问file对象打印出来是{}的问题，你可以打印file.name和file.size。
### 因为跨浏览器，file不能完整返回到vue，返回的path是个blobURL，仅供用于图片类文件回显，插件已内置好上传函数，调用上传会自动提交待上传文件，若非要自己拿path去搞上传那你自己处理。
### 不要再问关于返回的path怎么上传失败的问题，path只供用于image标签src属性回显图片，上传文件请使用组件内置上传函数。
------

## 上传报status：0 问题排查
### App如果上传报status: 0，调试方法：关闭manifest.json>h5的代理(若有配置代理)，并运行项目至Chrome浏览器，按F12查看接口详细报错信息（重点：是Chrome,不是HX内置浏览器）
### Android端不存在跨域问题，如上传报status: 0时
  #### 1、如果能进入接口，但收不到任何参数，检查是否在option>header里传入了Content-Type属性（默认不需要传，会自动匹配，若接口要求必须传则需要与接口匹配）
  #### 2、如果未能进入接口，检查nginx网关配置，不清楚可加群查看群相册资料。
### iOS端若出现status：0，排除上面两个问题后只会是跨域问题
  #### 1、后端处理允许上传接口跨域。（前端无需修改）
  #### 2、若后端不放开跨域，可将hybrid>html文件夹放入与接口同源的服务器，同源访问则不存在跨域问题。（可加群查看群相册资料）
------

## 使用说明
| 属性		| 是否必填	|  值类型	| 默认值	| 说明			|
| --------- | -------- 	| -----: 	| --: 	| :------------ |
| permission|	否 		| String	|组件内查看	| 需要校验的系统权限集合					|
| isPermissionInToast|	否 		| Boolean	|true	| 未授权时是否显示内置Toast提示权限permission[].message,建议传true	|
| isPermissionInModal|	否 		| Boolean	|true	| 用户拒绝授权时是否显示内置弹框引导用户开启权限	|
| width		|	否 		| String	|100%	| 容器宽度（App必填且与slot内容宽度一致）		|
| height	|	是 		| String	|80rpx	| 容器高度（App必填且与slot内容高度一致）		|
| debug		|	否 		| Boolean	|false	| 打印调试日志	|
| option	|	是 		| Object	|-		| [文件上传接口相关参数](#p1)|
| toBase	|	否 		| Boolean	|false	| 【2.3.8新增】选择的文件是否转base64返回，APP端使用前请先查看下方js注释说明|
| instantly	|	否 		| Boolean	|true	| instantly=true:选择文件后自动上传,instantly=false不自动上传，可通过调用upload()函数上传所有待上传的文件|
| distinct	|	否 		| Boolean	|false	| 【2.3.0新增】是否去重同名文件（提示：不去重复时前端展示同名文件名称后面+(N)，后端接收到的file.name还是为原名）|
| count		|	否 		| Number	|10		| 文件选择上限(个)|
| size		|	否 		| Number	|10		| 文件大小上限(M)	|
| multiple	|	否 		| Boolean	|true	| 是否允许多选	|
| wxFileType|	否 		| String	|all	| 微信小程序文件选择器格式限制(all=从所有文件选择，video=只能选择视频文件，image=只能选择图片文件，file=可以选择除了图片和视频之外的其它的文件)|
| accept	|	否 		| String	|-		| 文件选择器input file格式限制(有效值自行百度查accept)|
| formats	|	否 		| String	|-		| 限制允许上传的格式，空串=不限制，默认为空，多个格式以逗号隔开，例如png,jpg,pdf|
| childId	|	否 		| String	|lsjUpload| 自定义组件id，在回调函数第二个参数返回（若页面有多个组件可用于区分是哪个组件回调）|
| position	|	否 		| String	|static	| 控件的定位模式(static=控件随页面滚动;absolute=控件在页面中绝对定位，不随窗口内容滚动)|
| top,left,right,bottom	|	否 		| [Number,String]	|0		| 设置控件绝对位置，position=absolute时有效|
| @change	|	否 		| Function	|Map	| 选择文件后触发，返回所有已选择文件Map集合|
| @progress	|	否 		| Function	|Object	| 上传中持续触发，返回正在上传的文件对象，可通过set更新至页面显示上传进度|
| @uploadEnd|	否 		| Function	|Object	| 上传结束回调，返回当前上传的文件对象，type等于success（上传成功）返回responseText属性（服务端返回数据）|
| @permissionBefore|	否 		| Function	|Object	| 某项权限未授权时触发回调，返回{permission,message},仅isPermissionInToast传false时生效 |
| @permissionFail|	否 		| Function	|Object	| 用户拒绝授权时触发回调，返回{permission,message,result} |

## 【2.3.0】新增uni.$emit事件监听
| 方法名			|  说明							 	|
|---- 			| ----								|
| $upload-show  | 调用当前页面所有上传组件的show() 	|
| $upload-hide  | 调用当前页面所有上传组件的hide() 	|

使用示例
``` javascript
this.$nextTick(()=>{
	// 更新当前页所有上传组件在页面中的位置
	uni.$emit('$upload-show',{});
})
```

## $Refs
|作用 | 方法名| 传入参数|  说明|
|---- | --------- | -------- | :--: |
|显示和定位控件点击层| show|-| 控件显示状态下可触发点击|
|隐藏控件点击层| hide|-| 控件隐藏状态下不触发点击|
|动态设置文件列表| setFiles|[Array,Map] files| 传入格式请与组件选择返回格式保持一致，且name为必须属性，可查看下方演示|
|动态更新参数| setData|[String] name,[any] value| name支持a.b 和 a[b]，可查看下方演示|
|移除选择的文件| clear|[String] name| 不传参数清空所有文件，传入文件name时删除该name的文件|
|手动上传| upload|[String] name| 不传参数默认依次上传所有type=waiting的文件，传入文件name时不关心type是否为waiting，单独上传指定name的文件|

## <a id="p1">option说明</a>
|参数 | 是否必填 |  说明|
|---- | ---- | :--: |
|url  |	是	| 上传接口地址|
|name| 否	|上传接口文件key，默认为file|
|header| 否	|上传接口请求头|
|formData| 否	|上传接口额外参数|

## progress返回对象字段说明
|字段 |  说明|
|---- | :--: |
|file | 文件对象|
|name |文件名称|
|size |文件大小|
|path |用于image标签src属性回显图片|
|type |文件上传状态：waiting（等待上传）、loading（上传中）、success（成功） 、fail（失败）|
|responseText|上传成功后服务端返回数据(仅type为success时存在)|

## 以下演示为vue窗口使用方式，nvue使用区别是必须传入控件绝对位置如top，bottom，left，right，且position只能为absolute，如不清楚可点击右侧导入示例项目有详细演示代码。

### vue:
``` javascript
<lsj-upload 
	ref="lsjUpload"
	childId="uploadId"
	:width="width"
	:height="height"
	:option="option"
	:toBase="toBase"
	:size="size"
	:formats="formats"
	:debug="debug"
	:instantly="instantly"
	:distinct="distinct"
	@uploadEnd="onuploadEnd"
	@progress="onprogre"
	@change="change">
		<view class="btn" :style="{width: width,height: height}">选择附件</view>
</lsj-upload>


<view class="padding">
			
	<view>已选择文件列表：</view>
	<view>
	提示：【请使用组件内置上传方法，返回path仅供用于图片视频类文件回显，他用自行处理】
	</view>
	
	<!-- #ifndef MP-WEIXIN -->
	<view v-for="(item,index) in files.values()" :key="index">
		<image style="width: 100rpx;height: 100rpx;" :src="item.path" mode="widthFix"></image>
		<text>blob：{{item.path}}</text>
		<text>{{item.name}}</text>
		<text style="margin-left: 10rpx;">大小：{{item.size}}</text>
		<text style="margin-left: 10rpx;">状态：{{item.type}}</text>
		<text style="margin-left: 10rpx;">进度：{{item.progress}}</text>
		<text style="margin-left: 10rpx;" v-if="item.responseText">服务端返回演示：{{item.responseText}}</text>
		<text @click="resetUpload(item.name)" v-if="item.type=='fail'" style="margin-left: 10rpx;padding: 0 10rpx;border: 1rpx solid #007AFF;">重新上传</text>
		<text @click="clear(item.name)" style="margin-left: 10rpx;padding: 0 10rpx;border: 1rpx solid #007AFF;">删除</text>
	</view>
	<!-- #endif -->
	
	<!-- #ifdef MP-WEIXIN -->
	<view v-for="(item,index) in wxFiles" :key="index">
		<text>{{item.name}}</text>
		<text style="margin-left: 10rpx;">大小：{{item.size}}</text>
		<text style="margin-left: 10rpx;">状态：{{item.type}}</text>
		<text style="margin-left: 10rpx;">进度：{{item.progress}}</text>
		<view>
			<button @click="resetUpload(item.name)">重新上传</button>
			<button @click="clear(item.name)">删除</button>
		</view>
	</view>
	<!-- #endif -->
	
</view>


```

---
* 函数说明


``` javascript
export default {
	data() {
		return {
			// 上传接口参数
			option: {
				// 上传服务器地址，需要替换为你的接口地址
				url: 'http://hl.jw.com/dropbox/document/upload', // 该地址非真实路径，需替换为你项目自己的接口地址
				// 上传附件的key
				name: 'file',
				// 根据你接口需求自定义请求头,默认不要写content-type,让浏览器自适配
				header: {
					// 示例参数可删除
					'Authorization': 'bearer aa',
					'uid': '27',
					'accountid': '27'
				},
				// 根据你接口需求自定义body参数
				formData: { 
					// 'orderId': 1000
				}
			},
			/**
			 * 文件是否转为base64
			 * false: file为文件流，app端跨浏览器不能将文件流返回到当前页面，但不影响上传file，app端如果是图片文件可通过绑定item.path到image标签src回显或者上传成功后服务端返回的服务器文件地址；
			 * 【app和h5】true: file转base64，注意：转base64后若使用组件内置上传，file的值为base64串；
			 * 【微信小程序】true: file转base64返回页面，内置上传用的临时路径item.path
			 * 
			 * 提示：app端因为是跨浏览器的，所以不排除大文件转base64返回时url超长问题，目前只测了一般文件，若遇到那你只能自行改成sqlite或者localStroe
			 * 温馨提示：：：：若无特殊需求不建议转base64，就不解释为什么了。
			 * 温馨提示：：：：若无特殊需求不建议转base64，就不解释为什么了。
			 * 温馨提示：：：：若无特殊需求不建议转base64，就不解释为什么了。
			 */
			toBase: false,
			// 选择文件后是否立即自动上传，true=选择后立即上传
			instantly: true,
			// 选择文件是否去重
			distinct: false,
			// 必传宽高且宽高应与slot宽高保持一致
			width: '180rpx',
			height: '180rpx',
			// 限制允许上传的格式，空串=不限制，默认为空
			formats: '',
			// 文件上传大小限制
			size: 30,
			// 文件数量限制
			count: 2,
			// 是否多选
			multiple: true,
			// 文件回显列表
			files: new Map(),
			// 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
			wxFiles: [],
			// 是否打印日志
			debug: true,
			
			
			// 演示用
			tabIndex: 0,
			list:[], 
		}
	},
	onReady() {
		setTimeout(()=>{
			console.log('----演示动态更新参数-----');
			this.$refs['lsjUpload'+this.tabIndex].setData('formData.orderId','动态设置的参数'); 
			
			console.log('以下注释内容为-动态更新参数更多演示，放开后可查看演示效果');
			// 修改option对象的name属性
			// this.$refs.lsjUpload.setData('name','myFile');
			
			// 修改option对象的formData内的属性
			// this.$refs.lsjUpload.setData('formData.appid','1111');
			
			// 替换option对象的formData
			// this.$refs.lsjUpload.setData('formData',{appid:'222'});
			
			// option对象的formData新增属性
			// this.$refs.lsjUpload.setData('formData.newkey','新插入到formData的属性');
			
			
			// ---------演示初始化值，用于已提交后再次编辑时需带入已上传文件-------
			// 方式1=传入数组
			// let files1 = [{name: '1.png'},{name: '2.png',}];
			
			// 方式2=传入Map对象
			// let files2 = new Map();
			// files2.set('1.png',{name: '1.png'})
			
			// 此处调用setFiles设置初始files
			// this.$refs.lsjUpload.setFiles(files1);
			
			// 初始化tab
			this.onTab(0);
		},2000)
	},
	methods: {
		// APP端 isPermissionInToast=false时，点击按钮检测到某项权限未授权时触发
		// 注意：不建议isPermissionInToast传false!!
		// 因为isPermissionInToast等于false时弹出的授权窗口用户点击同意后不会继续打开系统文件选择界面，需要用户再点一次选择文件按钮
		permissionBefore({permission,message},childId) {
			console.log('permission', permission,message);
		},
		// APP端 用户有权限点击不同意时触发
		// 当isPermissionBuiltInHandle=false不使用组件内置弹框引导用户开启时，可在此函数内自定义弹框
		onpermissionFail({permission,message,result},childId) {
			console.log('permission', permission, message, result);
		},
		// 某文件上传结束回调(成功失败都回调)
		onuploadEnd(item,childId) {
			console.log(`${item.name}已上传结束，上传状态=${item.type}`);
			
			// 更新当前窗口状态变化的文件
			this.files.set(item.name,item);
			
			// ---可删除--演示上传完成后取服务端数据
			if (item['responseText']) {
				console.log('演示服务器返回的字符串JSON转Object对象');
				this.files.get(item.name).responseText = JSON.parse(item.responseText);
			}
			
			// 微信小程序Map对象for循环不显示，所以转成普通数组，
			// 如果你用不惯Map对象，也可以像这样转普通数组，组件使用Map主要是避免反复文件去重操作
			// #ifdef MP-WEIXIN
			this.wxFiles = [...this.files.values()];
			// #endif
			
			// 强制更新视图
			this.$forceUpdate();
			
			
			// ---可删除--演示判断是否所有文件均已上传成功
			let isAll = [...this.files.values()].find(item=>item.type!=='success');
			if (!isAll) {
				console.log('已全部上传完毕');
			}
			else {
				console.log(isAll.name+'待上传');
			}
			
		},
		// 上传进度回调，若md文档显示不出事件名称onprogress，如复制自行加上就行
		onprogress(item,childId) {
			// 更新当前状态变化的文件
			this.files.set(item.name,item);
			
			console.log('打印对象',JSON.stringify(this.files.get(item.name)));
			// 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
			// #ifdef MP-WEIXIN
			this.wxFiles = [...this.files.values()];
			// #endif
			
			// 强制更新视图
			this.$forceUpdate();
			
		},
		// 文件选择回调，若md文档显示不出事件名称onChange，如复制自行加上就行
		onChange(files,childId) {
			console.log('当前选择的文件列表：',JSON.stringify([...files.values()]));
			// 更新选择的文件 
			this.files = files;
			// 强制更新视图
			this.$forceUpdate();
			
			// 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
			// #ifdef MP-WEIXIN
			this.wxFiles = [...this.files.values()];
			// #endif
			
			// ---可删除--演示重新定位覆盖层控件
			this.$nextTick(()=>{
				console.log('演示重新定位 (提示：像示例里文件列表在按钮上方时就需要插入文件后更新webview位置)');
				// 直接更新当前页面所有上传组件webview位置
				uni.$emit('$upload-show',{});
				
				// 也可以通过以下方式指定更新ref对应组件位置
				// this.$refs.lsjUpload0.show();
				// this.$refs.lsjUpload1.show();
				// this.$refs.lsjUpload2.show();
			});
			
		},
		// 手动上传
		upload() {
			// name=指定文件名，不指定则上传所有type等于waiting和fail的文件
			this.$refs['lsjUpload'+this.tabIndex].upload();
		},
		// 指定上传某个文件
		resetUpload(name) {
			this.$refs['lsjUpload'+this.tabIndex].upload(name);
		},
		// 移除某个文件
		clear(name) {
			// name=指定文件名，不传name默认移除所有文件
			this.$refs['lsjUpload'+this.tabIndex].clear(name);
		},
		// ---可删除--演示在scroll中，scroll滚动后重新定位webview
		// 若md文档显示不出事件名称onscroll，如复制自行加上就行
		onscroll() {
			// #ifdef APP
			if (this.scrollTime) {
				clearTimeout(this.scrollTime);
			}
			this.scrollTime = setTimeout(()=> {
				console.log('scroll结束调用show()');
				this.$refs.lsjUpload1.show();
			},500)
			// #endif
		},
		/**
		 * ---可删除--演示在组件上方添加新内容DOM变化
		 */
		add() {
			this.list.push('DOM重排测试');
			// #ifdef APP
			this.$nextTick(()=>{
				// 更新当前页所有上传组件在页面中的位置
				uni.$emit('$upload-show',{});
			})
			// #endif
		},
		/**
		 * ---可删除--演示Tab切换时覆盖层是否能被点击
		 * APP端因为是webview，层级比view高，此时若不希望点击触发选择文件，需要手动调用hide()
		 * 手动调用hide后，需要调用show()才能恢复覆盖层的点击
		 */
		onTab(tabIndex) {
			// 隐藏所有的控件（透明层）
			uni.$emit('$upload-hide',{});
			
			this.tabIndex = tabIndex;
			// 显示允许点击的控件（透明层）
			this.$nextTick(()=>{
				this.$refs['lsjUpload'+this.tabIndex].show();
			})
			
		}
	}
}

```

## 温馨提示
	
* 文件上传
0. 如说明表达还不够清楚，不清楚怎么使用可导入完整示例项目运行体验和查看	
1. APP端请优先联调Android,上传成功后再运行iOS端，如iOS返回status=0则需要后端开启允许跨域；
2. header的Content-Type类型需要与服务端要求一致，否则收不到附件（服务端若没有明文规定则不写，使用默认匹配）
3. 服务端不清楚怎么配置跨域可加群咨询，具体百度~
4. 欢迎加入QQ讨论群：
### 插件交流群4：413918560
------
- 以下群如无空位会被拒绝
- 群3：667530868（已满）
- 群2：469580165（已满）
- 群1：701468256（已满）
