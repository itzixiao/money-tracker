/**
 * @Descripttion: app升级弹框
 * @Version: 1.0.0
 * @Author: leefine
 */

import config from '@/upgrade-config.js'
import upgrade from './upgrade'

const {
	title = '发现新版本',
		confirmText = '立即更新',
		cancelTtext = '稍后再说',
		confirmBgColor = '#409eff',
		showCancel = true,
		titleAlign = 'left',
		descriAlign = 'left',
		icon
} = config.upgrade;

class AppDialog {
	constructor() {
		this.maskEl = {}
		this.popupEl = {}
		this.screenHeight = 600;
		this.popupHeight = 230;
		this.popupWidth = 300;
		this.viewWidth = 260;
		this.descrTop = 130;
		this.viewPadding = 20;
		this.iconSize = 80;
		this.titleHeight = 30;
		this.textHeight = 18;
		this.textSpace = 10;
		this.popupContent = []
		this.apkUrl = '';
	}

	// 显示
	show(apkUrl, changelog) {
		this.drawView(changelog)
		this.maskEl.show()
		this.popupEl.show()
		this.apkUrl = apkUrl;
	}

	// 隐藏
	hide() {
		this.maskEl.hide()
		this.popupEl.hide()
	}

	// 绘制
	drawView(changelog) {
		this.screenHeight = plus.screen.resolutionHeight;
		this.popupWidth = plus.screen.resolutionWidth * 0.8;
		this.popupHeight = this.viewPadding * 3 + this.iconSize + 100;
		this.viewWidth = this.popupWidth - this.viewPadding * 2;
		this.descrTop = this.viewPadding + this.iconSize + this.titleHeight;
		this.popupContent = [];

		if (icon) {
			this.popupContent.push({
				id: 'logo',
				tag: 'img',
				src: icon,
				position: {
					top: '0px',
					left: (this.popupWidth - this.iconSize) / 2 + 'px',
					width: this.iconSize + 'px',
					height: this.iconSize + 'px'
				}
			});
		} else {
			this.popupContent.push({
				id: 'logo',
				tag: 'img',
				src: '_pic/upgrade.png',
				position: {
					top: '0px',
					left: (this.popupWidth - this.iconSize) / 2 + 'px',
					width: this.iconSize + 'px',
					height: this.iconSize + 'px'
				}
			});
		}

		// 标题
		if (title) {
			this.popupContent.push({
				id: 'title',
				tag: 'font',
				text: title,
				textStyles: {
					size: '18px',
					color: '#333',
					weight: 'bold',
					align: titleAlign
				},
				position: {
					top: this.descrTop - this.titleHeight - this.textSpace + 'px',
					left: this.viewPadding + 'px',
					width: this.viewWidth + 'px',
					height: this.titleHeight + 'px'
				}
			})
		} else {
			this.descrTop -= this.titleHeight;
		}

		this.drawText(changelog)

		// 取消
		if (showCancel) {
			const width = (this.viewWidth - this.viewPadding) / 2;
			const confirmLeft = width + this.viewPadding * 2;
			this.drawBtn('cancel', width, cancelTtext)
			this.drawBtn('confirm', width, confirmText, confirmLeft)
		} else {
			this.drawBtn('confirmBox', this.viewWidth, confirmText)
		}

		this.drawBox(showCancel)
	}

	// 描述内容
	drawText(changelog) {
		if (!changelog) return [];
		const textArr = changelog.split('')
		const len = textArr.length;
		let prevNode = 0;
		let nodeWidth = 0;
		let letterWidth = 0;
		const chineseWidth = 14;
		const otherWidth = 7;
		let rowText = [];

		for (let i = 0; i < len; i++) {
			// 包含中文
			if (/[\u4e00-\u9fa5]|[\uFE30-\uFFA0]/g.test(textArr[i])) {
				// 包含字母
				let textWidth = ''
				if (letterWidth > 0) {
					textWidth = nodeWidth + chineseWidth + letterWidth * otherWidth;
					letterWidth = 0;
				} else {
					// 不含字母
					textWidth = nodeWidth + chineseWidth;
				}

				if (textWidth > this.viewWidth) {
					rowArrText(i, chineseWidth)
				} else {
					nodeWidth = textWidth;
				}
			} else {
				// 不含中文
				// 包含换行符
				if (/\n/g.test(textArr[i])) {
					rowArrText(i, 0, 1)
					letterWidth = 0;
				} else if (textArr[i] == '\\' && textArr[i + 1] == 'n') {
					rowArrText(i, 0, 2)
					letterWidth = 0;
				} else if (/[a-zA-Z0-9]/g.test(textArr[i])) {
					// 包含字母数字
					letterWidth += 1;
					const textWidth = nodeWidth + letterWidth * otherWidth;
					if (textWidth > this.viewWidth) {
						const preNode = i + 1 - letterWidth;
						rowArrText(preNode, letterWidth * otherWidth)
						letterWidth = 0;
					}
				} else {
					if (nodeWidth + otherWidth > this.viewWidth) {
						rowArrText(i, otherWidth)
					} else {
						nodeWidth += otherWidth;
					}
				}
			}
		}

		if (prevNode < len) {
			rowArrText(len, -1)
		}
		this.drawDesc(rowText)

		function rowArrText(i, nWidth = 0, type = 0) {
			const typeVal = type > 0 ? 'break' : 'text';

			rowText.push({
				type: typeVal,
				content: changelog.substring(prevNode, i)
			})

			if (nWidth >= 0) {
				prevNode = i + type;
				nodeWidth = nWidth;
			}
		}
	}

	// 描述
	drawDesc(rowText) {
		rowText.forEach((item, index) => {
			if (index > 0) {
				this.descrTop += this.textHeight;
				this.popupHeight += this.textHeight;
			}

			this.popupContent.push({
				id: 'content' + index + 1,
				tag: 'font',
				text: item.content,
				textStyles: {
					size: '14px',
					color: '#666',
					align: descriAlign
				},
				position: {
					top: this.descrTop + 'px',
					left: this.viewPadding + 'px',
					width: this.viewWidth + 'px',
					height: this.textHeight + 'px'
				}
			})

			if (item.type == 'break') {
				this.descrTop += this.textSpace;
				this.popupHeight += this.textSpace;
			}
		})
	}

	// 按钮
	drawBtn(id, width, text, left = this.viewPadding) {
		let boxColor = confirmBgColor,
			textColor = '#ffffff';
		if (id == 'cancel') {
			boxColor = '#f0f0f0';
			textColor = '#666666';
		}

		this.popupContent.push({
			id: id + 'Box',
			tag: 'rect',
			rectStyles: {
				radius: '6px',
				color: boxColor
			},
			position: {
				bottom: this.viewPadding + 'px',
				left: left + 'px',
				width: width + 'px',
				height: '40px'
			}
		})

		this.popupContent.push({
			id: id + 'Text',
			tag: 'font',
			text: text,
			textStyles: {
				size: '14px',
				color: textColor
			},
			position: {
				bottom: this.viewPadding + 'px',
				left: left + 'px',
				width: width + 'px',
				height: '40px'
			}
		})
	}

	// 内容框
	drawBox(showCancel) {
		this.maskEl = new plus.nativeObj.View('maskEl', {
			top: '0px',
			left: '0px',
			width: '100%',
			height: '100%',
			backgroundColor: 'rgba(0,0,0,0.5)'
		});

		this.popupEl = new plus.nativeObj.View('popupEl', {
			tag: 'rect',
			top: (this.screenHeight - this.popupHeight) / 2 + 'px',
			left: '10%',
			height: this.popupHeight + 'px',
			width: '80%'
		});

		// 白色背景
		this.popupEl.drawRect({
			color: '#ffffff',
			radius: '8px'
		}, {
			top: this.iconSize / 2 + 'px',
			height: this.popupHeight - this.iconSize / 2 + 'px'
		});

		this.popupEl.draw(this.popupContent);

		this.popupEl.addEventListener('click', e => {
			const maxTop = this.popupHeight - this.viewPadding;
			const maxLeft = this.popupWidth - this.viewPadding;
			const buttonWidth = (this.viewWidth - this.viewPadding) / 2;
			if (e.clientY > maxTop - 40 && e.clientY < maxTop) {
				if (showCancel) {
					// 取消
					// if(e.clientX>this.viewPadding && e.clientX<maxLeft-buttonWidth-this.viewPadding){}
					// 确定
					if (e.clientX > maxLeft - buttonWidth && e.clientX < maxLeft) {
						upgrade.checkOs(this.apkUrl)
					}
				} else {
					if (e.clientX > this.viewPadding && e.clientX < maxLeft) {
						upgrade.checkOs(this.apkUrl)
					}
				}
				this.hide()
			}
		});
	}
}

export default new AppDialog()
