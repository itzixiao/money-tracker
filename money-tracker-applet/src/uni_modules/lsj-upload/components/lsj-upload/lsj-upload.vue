<template>
	<view class="lsj-file" :class="[webId]" :style="[getStyles]">
		<view ref="lsj" class="hFile" :style="[getStyles]" @click="onClick">
			<slot><view class="defview" :style="[getStyles]">附件上传</view></slot>
		</view>
	</view>
</template>

<script>
// 查看文档：https://ext.dcloud.net.cn/plugin?id=5459
import {LsjFile} from './LsjFile.js' 
export default {
	name: 'Lsj-upload',
	props: {
		// 打印日志
		debug: {type: Boolean,default: false},
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
		toBase: {type: Boolean,default: false},
		// 是否去重文件（同名文件覆盖）
		distinct: {type: Boolean,default: false},
		// 自动上传
		instantly: {type: Boolean,default: true},
		// 上传接口参数设置
		option: {type: Object,default: ()=>{}},
		// 文件大小上限
		size: { type: Number, default: 10 },
		// 文件选择个数上限,超出后不触发点击
		count: { type: Number, default: 9 },
		// 是否允许多选文件
		multiple: {type:Boolean, default: false},
		// 允许上传的文件格式（多个以逗号隔开）
		formats: { type: String, default:''},
		// input file选择限制
		accept: {type: String,default: ''},
		// APP端权限校验 ---有效值查看https://ext.dcloud.net.cn/plugin?id=594
		permission: {
			type: Object,
			default:()=>{return {
				// ios一般无需自定义校验，如需要可像android一样配置，权限表地址https://ext.dcloud.net.cn/plugin?id=594
				'ios':false,
				'android':[
					// {
					// 	value: 'android.permission.CAMERA',
					// 	message: '文件选择器包含相机拍照上传，需要您授权相机权限'
					// },
					{
						value: 'android.permission.READ_EXTERNAL_STORAGE',
						message: '文件选择需要您授权外部存储(含相册)读取权限'
					},
					// 若不需要可注释
					// {
					// 	value: 'android.permission.RECORD_AUDIO',
					// 	message: '文件选择器包含录音上传，需要您授权麦克风权限'
					// }
				]
			}}
		},
		// 检测到未授权时顶部显示toast轻提示，内容为permission的message,,若传false不弹出内置Toast,
		// 可通过@permissionBefore回调自行处理，isPermissionInToast为false时才回调
		isPermissionInToast: {
			type: Boolean,
			default: true
		},
		// 用户未授权时显示弹框引导用户去系统开启权限,若传false不弹出内置Modal,
		// 可通过@permissionFail回调自行处理
		// 跳转系统界面表可查看 https://ask.dcloud.net.cn/question/14732
		isPermissionInModal: {
			type: Boolean,
			default: true
		},
		// 微信选择文件类型 
		//all=从所有文件选择，
		//video=只能选择视频文件，
		//image=只能选择图片文件，
		//file=可以选择除了图片和视频之外的其它的文件
		wxFileType: { type: String, default: 'all' },
		// webviewID需唯一，不同窗口也不要同Id
		childId: { type: String, default: 'lsjUpload'  },
		// 文件选择触发面宽度
		width: { type: String, default: '100%' },
		// 文件选择触发面高度
		height: { type: String, default: '80rpx' },
		
		// top,left,bottom,right仅position=absolute时才需要传入
		top: { type: [String, Number], default: '' },
		left: { type: [String, Number], default: '' },
		bottom: { type: [String, Number], default: '' },
		right: { type: [String, Number], default: '' },
		// nvue不支持跟随窗口滚动
		position: { 
			type: String,
			// #ifdef APP-NVUE
			 default: 'absolute',
			// #endif
			// #ifndef APP-NVUE
			default: 'static',
			// #endif
		},
	},
	data() {
		return {
			webId: ''
		}
	},
	computed: {
		getStyles() {
			let styles = {
				width: this.width,
				height: this.height
			}
			if (this.position == 'absolute') {
				styles['top'] = this.top
				styles['bottom'] = this.bottom
				styles['left'] = this.left
				styles['right'] = this.right
				styles['position'] = 'fixed'
			}

			return styles
		}
	},
	watch: {
		option(v) {
			// #ifdef APP-PLUS
			this.lsjFile&&this.show();
			// #endif
		}
	},
	updated() {
		// #ifdef APP-PLUS
			if (this.isShow) {
				this.lsjFile&&this.show();
			}
		// #endif
	},
	created() {
		uni.$on('$upload-show',this.emitShow);
		uni.$on('$upload-hide',this.hide);
	},
	beforeDestroy() {
		uni.$off('$upload-show',this.emitShow);
		uni.$off('$upload-hide',this.hide);
		// #ifdef APP-PLUS
		this.lsjFile.dom.close();
		// #endif
	},
	mounted() {
		let pages = getCurrentPages();
		this.myRoute = pages[pages.length - 1].route;
		this._size = 0;
		let WEBID = 'lsj_' + this.childId + this.getRandomString(10) + new Date().getTime();
		this.webId = WEBID;
		this.lsjFile = new LsjFile({
			id: WEBID,
			debug: this.debug,
			width: this.width,
			height: this.height,
			option: this.option,
			instantly: this.instantly,
			// 限制条件
			prohibited: {
				// 文件是否转base64
				toBase: this.toBase,
				// 是否去重
				distinct: this.distinct,
				// 大小
				size: this.size,
				// 允许上传的格式
				formats: this.formats,
				// 限制选择的格式
				accept: this.accept,
				count: this.count,
				// 是否多选
				multiple: this.multiple,
				permission: this.permission,
				isPermissionInToast: this.isPermissionInToast,
				isPermissionInModal: this.isPermissionInModal
			},
			onchange: this.onchange,
			onprogress: this.onprogress,
			permissionBefore: this.permissionBefore,
			permissionFail: this.permissionFail,
		});
		this.create();
	},
	methods: {
		getRandomString(length) {
		  var result = '';
		  var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
		  var charactersLength = characters.length;
		  for (var i = 0; i < length; i++) {
		    result += characters.charAt(Math.floor(Math.random() * charactersLength));
		  }
		  return result;
		},
		permissionBefore(res) {
			this.$emit('permissionBefore',res,this.childId);
		},
		permissionFail(res) {
			this.$emit('permissionFail',res,this.childId);
		},
		setFiles(array) {
			if (array instanceof Map) {
				for (let [key, item] of array) {
					item['progress'] = 100;
					item['type'] = 'success';
					this.lsjFile.files.set(key,item);
				}
			}
			else if (Array.isArray(array)) {
				array.forEach(item=>{
					if (item.name) { 
						item['progress'] = 100;
						item['type'] = 'success';
						this.lsjFile.files.set(item.name,item);
					}
				});
			}
			this.onchange(this.lsjFile.files);
		},
		setData() {
			this.lsjFile&&this.lsjFile.setData(...arguments);
		},
		getDomStyles(callback) {
			// #ifndef APP-NVUE
			let view = uni
				.createSelectorQuery()
				.in(this)
				.select('.'+this.webId)
			view.fields(
				{
					size: true,
					rect: true
				},
				({ height, width, top, left, right, bottom }) => {
					uni.createSelectorQuery()
					.selectViewport()
					.scrollOffset(({ scrollTop }) => {
						return callback({
							top: parseInt(top) + parseInt(scrollTop) + 'px',
							left: parseInt(left) + 'px',
							width: parseInt(width) + 'px',
							height: parseInt(height) + 'px'
						})
					})
					.exec()
				}
			).exec()
			// #endif
			// #ifdef APP-NVUE
			const dom = weex.requireModule('dom')
			dom.getComponentRect(this.$refs.lsj, ({ size: { height, width, top, left, right, bottom } }) => {
				return callback({
					top: parseInt(top) + 'px',
					left: parseInt(left) + 'px',
					width: parseInt(width) + 'px',
					height: parseInt(height) + 'px',
					right: parseInt(right) + 'px',
					bottom: parseInt(bottom) + 'px'
				})
			})
			// #endif
		},
		emitShow() {
			let pages = getCurrentPages();
			let route = pages[pages.length - 1].route;
			if (route === this.myRoute) {
				return this.show();
			}
		},
		show() {
			this.debug&&console.log('触发show函数');
			if (this._size && (this._size >= this.count)) {
				return;
			}
			this.isShow = true;
			// #ifdef APP-PLUS
			this.lsjFile&&this.getDomStyles(styles => {
				this.lsjFile.dom.setStyle(styles)
			});
			// #endif
			// #ifdef H5
			this.lsjFile.dom.style.display = 'inline'
			// #endif
		},
		hide() {
			this.debug&&console.log('触发hide函数');
			this.isShow = false;
			// #ifdef APP-PLUS
			this.lsjFile&&this.lsjFile.dom.setStyle({
				top: '-100px',
				left:'0px',
				width: '1px',
				height: '100px',
			});
			// #endif
			// #ifdef H5
			this.lsjFile.dom.style.display = 'none'
			// #endif
		},
		/**
		 * 手动提交上传
		 * @param {string}name 文件名称，不传则上传所有type等于waiting和fail的文件
		 */
		upload(name) {
			this.lsjFile&&this.lsjFile.upload(name);
		},
		/**
		 * @returns {Map} 已选择的文件Map集
		 */
		onchange(files) {
			this.$emit('changeUp',files,this.childId);
			this._size = files.size;
			return files.size >= this.count ? this.hide() : this.show();
		},
		/**
		 * @returns {object} 当前上传中的对象
		 */
		onprogress(item,end=false) {
			this.$emit('progress',item,this.childId);
			if (end) {
				setTimeout(()=>{
					this.$emit('uploadEnd',item,this.childId);
				},0);
			}
		},
		/**
		 * 移除组件内缓存的某条数据
		 * @param {string}name 文件名称,不指定默认清除所有文件
		 */
		clear(name) {
			this.lsjFile.clear(name);
		},
		// 创建选择器
		create() {
			// 若iOS端服务端处理不了跨域就将hybrid目录内的html放到服务端去，并将此处path改成服务器上的地址
			let path = '/uni_modules/lsj-upload/hybrid/html/uploadFile.html';
			let dom = this.lsjFile.create(path);
			// #ifdef H5
			this.$refs.lsj.$el.appendChild(dom);
			// #endif
			// #ifndef APP-PLUS
			this.show();
			// #endif
			// #ifdef APP-PLUS
			dom.setStyle({position: this.position});
			dom.loadURL(path);
			setTimeout(()=>{
				// #ifdef APP-NVUE
				plus.webview.currentWebview().append(dom);
				// #endif
				// #ifndef APP-NVUE
				this.$root.$scope.$getAppWebview().append(dom);
				// #endif
				this.show();
			},300)
			// #endif
		},
		// 点击选择附件
		onClick() {
			if (this._size >= this.count) {
				this.toast(`只允许上传${this.count}个文件`);
				return;
			}
			
			// #ifdef MP-WEIXIN
			if (!this.isShow) {return;}
			let count = this.count - this._size;
			this.lsjFile.chooseMessageFile(this.wxFileType,count);
			// #endif
		},
		toast(msg) {
			uni.showToast({
				title: msg,
				icon: 'none'
			});
		}
	}
}
</script>

<style scoped>
.lsj-file {
	display: inline-block;
}
.defview {
	background-color: #007aff;
	color: #fff;
	border-radius: 10rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 28rpx;
}
.hFile {
	position: relative;
	overflow: hidden;
}
</style>
