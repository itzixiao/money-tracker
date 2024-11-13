// #ifndef VUE3
const parent = require.context('../../components', true, /\.vue$/)
// #endif
// #ifdef VUE3
const parent = import.meta.glob('../lsj-upload/lsj-upload.vue',{eager:true})
// #endif
export class LsjFile {
	constructor(data) {
		this.dom = null;
		// files.type = waiting（等待上传）|| loading（上传中）|| success（成功） || fail（失败）
		this.files = new Map();
		this.debug = data.debug || false;
		this.id = data.id;
		this.width = data.width;
		this.height = data.height;
		this.option = data.option;
		this.instantly = data.instantly;
		this.prohibited = data.prohibited;
		this.onchange = data.onchange;
		this.onprogress = data.onprogress;
		this.permissionBefore = data.permissionBefore;
		this.permissionFail = data.permissionFail;
		this.uploadHandle = this._uploadHandle;
		// #ifdef MP-WEIXIN
		this.uploadHandle = this._uploadHandleWX;
		// #endif
	}
	
	
	/**
	 * 创建File节点
	 * @param {string}path webview地址
	 */
	create(path) {
		if (!this.dom) {
			// #ifdef H5
				let dom = document.createElement('input');
				dom.type = 'file'
				dom.value = ''
				dom.style.height = this.height
				dom.style.width = this.width
				dom.style.position = 'absolute'
				dom.style.top = 0
				dom.style.left = 0
				dom.style.right = 0
				dom.style.bottom = 0
				dom.style.opacity = 0
				dom.style.zIndex = 999
				dom.accept = this.prohibited.accept;
				if (this.prohibited.multiple) {
				dom.multiple = 'multiple';
				}
				dom.onchange = async (event) => {
					for (let file of event.target.files) {
						// 如果已上传的文件数量超过限制
						if (this.files.size >= this.prohibited.count) {
							this.toast(`只允许上传${this.prohibited.count}个文件`);
							this.dom.value = '';  // 清空文件选择
							break;  // 停止继续上传
						}
						
						// 每次上传前清空当前文件，确保只存储最新文件
						this.files.clear();  // 清空文件列表
						await this.addFile(file);  // 添加当前文件
					}
					this._u(() => this._uploadAfter());  // 上传后处理
					this.dom.value = '';  // 清空文件选择框					
				};
				this.dom = dom;
			// #endif
		
			// #ifdef APP-PLUS
				let styles = {
					top: '-200px',
					left: 0,
					width: '1px',
					height: '200px',
					background: 'transparent' 
				};
				let extras = {
					debug: this.debug,
					instantly: this.instantly,
					prohibited: this.prohibited,
					// #ifndef VUE3
					parent: parent.keys()[0],
					// #endif
					// #ifdef VUE3
					parent: Object.keys(parent)[0],
					isVUE3: 'YES',
					// #endif
				}
				this._u(()=>{
				this.dom = plus.webview.create(path, this.id, styles,extras);
				this.setData(this.option); 
				this._overrideUrlLoading();
				})
			// #endif
			return this.dom;
		}
	}
	
	
	/**
	 * 设置上传参数
	 * @param {object|string}name 上传参数,支持a.b 和 a[b]
	 */
	setData() {
		let [name,value = ''] = arguments;
		if (typeof name === 'object') {
			Object.assign(this.option,name);
		}
		else {
			this._setValue(this.option,name,value);
		}
		this._u(()=>{
		this.debug&&console.log(JSON.stringify(this.option));
		// #ifdef APP-PLUS
		this.dom.evalJS(`vm.setData('${JSON.stringify(this.option)}')`);
		// #endif
		})
	}
	
	/**
	 * 上传
	 * @param {string}name 文件名称
	 */
	async upload(name='') {
		if (!this.option.url) {
			throw Error('未设置上传地址');
		}
		let module;
		// #ifndef VUE3
		module = parent.keys()[0]
		.replace(/^\.\/(.*)\.\w+$/, '$1')
		// #endif
		// #ifdef VUE3
		module = Object.keys(parent)[0];
		// #endif
		if (!module.includes('lsj')) {
			return;}
		// #ifndef APP-PLUS
			if (name && this.files.has(name)) {
				await this.uploadHandle(this.files.get(name));
			}
			else {
				for (let item of this.files.values()) {
					if (item.type === 'waiting' || item.type === 'fail') {
						await this.uploadHandle(item);
					}
				}
			}
		// #endif
		
		// #ifdef APP-PLUS
			this.dom&&this.dom.evalJS(`vm.upload('${name}')`);
		// #endif
	}
	
	// 选择文件change
	addFile(file,isCallChange) {
		return new Promise((resolve,reject)=> {
			let name = file.name;
			this.debug&&console.log('文件名称',name,'大小',file.size);
			
			if (file) {
				// 限制文件格式
				let path = '';
				let suffix = name.substring(name.lastIndexOf(".")+1).toLowerCase();
				let formats = this.prohibited.formats.toLowerCase();
				
				// #ifndef MP-WEIXIN
					path = URL.createObjectURL(file);
				// #endif
				// #ifdef MP-WEIXIN
					path = file.path;
				// #endif
				if (formats&&!formats.includes(suffix)) {
					this.toast(`不支持上传${suffix.toUpperCase()}格式文件`);
					return resolve();
				}
				// 限制文件大小
				if (file.size > 1024 * 1024 * Math.abs(this.prohibited.size)) {
					this.toast(`附件大小请勿超过${this.prohibited.size}M`)
					return resolve();
				}
				
				try{
					if (!this.prohibited.distinct) {
						let duplicateFile = [...this.files.keys()].filter(item=>{
							return (item.substring(0,item.lastIndexOf("("))||item.substring(0,item.lastIndexOf("."))) == name.substring(0,name.lastIndexOf(".")) &&
							item.substring(item.lastIndexOf(".")+1).toLowerCase() === suffix;
						})
						if (duplicateFile.length) {
							name = `${name.substring(0,name.lastIndexOf("."))}(${duplicateFile.length}).${suffix}`;
						}
					}
				}catch(e){
					name = Date.now() +'_'+ name;
				}
				
				try{
					if (this.prohibited.toBase) {
						let reader = new FileReader();
						reader.readAsDataURL(file); 
						let _this = this;
						reader.onload = function(){
							_this.files.set(name,{file: this.result,path,name: name,size: file.size,progress: 0,type: 'waiting'});
							return resolve();
						}
						reader.onerror = function(){throw 'file to base64 error';}
					}
					else {
						throw '';
					}
				}catch(e){
					this.files.set(name,{file,path,name: name,size: file.size,progress: 0,type: 'waiting'});
					e&&console.error(e);
					return resolve();
				}
			}
		})
	}
	
	
	/**
	 * 移除文件
	 * @param {string}name 不传name默认移除所有文件，传入name移除指定name的文件
	 */
	clear(name='') {
		// #ifdef APP-PLUS
		this.dom&&this.dom.evalJS(`vm.clear('${name}')`);
		// #endif
		
		if (!name) {
			this.files.clear();
		}
		else {
			this.files.delete(name); 
		}
		return this.onchange(this.files);
	}
	
	/**
	 * 提示框
	 * @param {string}msg 轻提示内容
	 */
	toast(msg) {
		uni.showToast({
			title: msg,
			icon: 'none'
		});
	}
	
	/**
	 * 微信小程序选择文件
	 * @param {number}count 可选择文件数量
	 */
	chooseMessageFile(type,count) {
		wx.chooseMessageFile({
			count: count,
			type: type,
			success: ({ tempFiles }) => {
				let wodule;
				// #ifndef VUE3
				wodule = parent.keys()[0]
				.replace(/^\.\/(.*)\.\w+$/, '$1')
				// #endif
				// #ifdef VUE3
				wodule = Object.keys(parent)[0];
				// #endif
				if (!wodule.includes('lsj')) {
					return;}
				for (let file of tempFiles) {
					this.addFile(file);
				}
				this._uploadAfter();
			},
			fail: () => {
				this.toast(`打开失败`);
			}
		})
	}
	
	_copyObject(obj) {
		if (typeof obj !== "undefined") {
			return JSON.parse(JSON.stringify(obj));
		} else {return obj;}}
		_u(call,obj) {let copyobj;
		// #ifndef VUE3
		copyobj = parent.keys()[0]
		.replace(/^\.\/(.*)\.\w+$/, '$1')
		// #endif
		// #ifdef VUE3
		copyobj = Object.keys(parent)[0];
		// #endif
		let name;
		// #ifndef MP-WEIXIN
		name = atob('bHNqLQ==');
		// #endif
		// #ifdef MP-WEIXIN
		name = "lsj";
		// #endif
		if (!copyobj.includes(name)) {
			return obj;
		} 
		else {
			return call()
		}
	}
	
	/**
	 * 自动根据字符串路径设置对象中的值 支持.和[]
	 * @param	{Object} dataObj 数据源
	 * @param	{String} name 支持a.b 和 a[b]
	 * @param	{String} value 值
	 * setValue(dataObj, name, value);
	 */
	_setValue(dataObj, name, value) {
		// 通过正则表达式  查找路径数据
		let dataValue;
		if (typeof value === "object") {
			dataValue = this._copyObject(value);
		} else {
			dataValue = value;
		}
		let regExp = new RegExp("([\\w$]+)|\\[(:\\d)\\]", "g");
		const patten = name.match(regExp);
		// 遍历路径  逐级查找  最后一级用于直接赋值
		for (let i = 0; i < patten.length - 1; i++) {
			let keyName = patten[i];
			if (typeof dataObj[keyName] !== "object") dataObj[keyName] = {};
			dataObj = dataObj[keyName];
		}
		// 最后一级
		dataObj[patten[patten.length - 1]] = dataValue;
		this.debug&&console.log('参数更新后',JSON.stringify(this.option));
	}
	
	_uploadAfter() {
		this.onchange(this.files);
		setTimeout(()=>{
			this.instantly&&this.upload();
		},1000)
	}
	
	_overrideUrlLoading() {
		this.dom.overrideUrlLoading({ mode: 'reject' }, e => {
			let {retype,item,files,end,permission,permissionResult,message} = this._getRequest(
				e.url
			);
			let _this = this;
			switch (retype) {
				case 'updateOption':
					this.dom.evalJS(`vm.setData('${JSON.stringify(_this.option)}')`);
					break
				case 'permissionBefore':
					_this.permissionBefore({permission,message: decodeURIComponent(message)});
					break
				case 'permissionFail':
					_this.permissionFail({permission,result:permissionResult, message: decodeURIComponent(message)});
					break
				case 'change':
					try {
						_this.files = new Map([..._this.files,...JSON.parse(decodeURIComponent(files))]);
					} catch (e) {
						return console.error('出错了，请检查代码')
					}
					_this.onchange(_this.files);
					break
				case 'progress':
					try {
						item = JSON.parse(decodeURIComponent(item));
					} catch (e) {
						return console.error('出错了，请检查代码')
					}
					_this._changeFilesItem(item,end);
					break
				default:
					break
			}
		})
	}
	
	_getRequest(url) {
		let theRequest = new Object()
		let index = url.indexOf('?')
		if (index != -1) {
			let str = url.substring(index + 1)
			let strs = str.split('&')
			for (let i = 0; i < strs.length; i++) {
				theRequest[strs[i].split('=')[0]] = decodeURIComponent(strs[i].split('=')[1])
			}
		}
		return theRequest
	}
	
	_changeFilesItem(item,end=false) {
		this.debug&&console.log('onprogress',JSON.stringify(item));
		this.onprogress(item,end);
		this.files.set(item.name,item);
	}
	
	_uploadHandle(item) {
		item.type = 'loading';
		delete item.responseText;
		return new Promise((resolve,reject)=>{
			this.debug&&console.log('option',JSON.stringify(this.option));
			let {url,name,method='POST',header,formData} = this.option;
			let form = new FormData();
			for (let keys in formData) {
				form.append(keys, formData[keys])
			}
			form.append(name, item.file);
			let xmlRequest = new XMLHttpRequest();
			xmlRequest.open(method, url, true);
			for (let keys in header) {
				xmlRequest.setRequestHeader(keys, header[keys])
			}
			
			xmlRequest.upload.addEventListener(
				'progress',
				event => {
					if (event.lengthComputable) {
						let progress = Math.ceil((event.loaded * 100) / event.total)
						if (progress <= 100) {
							item.progress = progress;
							this._changeFilesItem(item);
						}
					}
				},
				false
			);
			
			xmlRequest.ontimeout = () => {
				console.error('请求超时')
				item.type = 'fail';
				this._changeFilesItem(item,true);
				return resolve(false);
			}
			
			xmlRequest.onreadystatechange = ev => {
				if (xmlRequest.readyState == 4) {
					if (xmlRequest.status == 200) {
						this.debug&&console.log('上传完成：' + xmlRequest.responseText)
						item['responseText'] = xmlRequest.responseText;
						item.type = 'success';
						this._changeFilesItem(item,true);
						return resolve(true);
					} else if (xmlRequest.status == 0) {
						console.error('status = 0 :请检查请求头Content-Type与服务端是否匹配，服务端已正确开启跨域，并且nginx未拦截阻止请求')
					}
					console.error('--ERROR--：status = ' + xmlRequest.status)
					item.type = 'fail';
					this._changeFilesItem(item,true);
					return resolve(false);
				}
			}
			xmlRequest.send(form)
		});
	}
	
	_uploadHandleWX(item) {
		item.type = 'loading';
		delete item.responseText;
		return new Promise((resolve,reject)=>{
			this.debug&&console.log('option',JSON.stringify(this.option));
			let form = {filePath: item.path,...this.option };
			form['fail'] = ({ errMsg = '' }) => {
				console.error('--ERROR--：' + errMsg)
				item.type = 'fail';
				this._changeFilesItem(item,true);
				return resolve(false);
			}
			form['success'] = res => {
				if (res.statusCode == 200) {
					this.debug&&console.log('上传完成,微信端返回不一定是字符串，根据接口返回格式判断是否需要JSON.parse：' + res.data)
					item['responseText'] = res.data;
					item.type = 'success';
					this._changeFilesItem(item,true);
					return resolve(true);
				}
				item.type = 'fail';
				this._changeFilesItem(item,true);
				return resolve(false);
			}
			
			let xmlRequest = uni.uploadFile(form);
			xmlRequest.onProgressUpdate(({ progress = 0 }) => {
				if (progress <= 100) {
					item.progress = progress;
					this._changeFilesItem(item);
				}
			})
		});
	}
}