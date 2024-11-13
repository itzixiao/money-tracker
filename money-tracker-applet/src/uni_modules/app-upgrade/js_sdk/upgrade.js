/**
 * @Descripttion: app下载更新
 * @Version: 1.0.0
 * @Author: leefine
 */

import config from '@/upgrade-config.js'
const { upType = 0 } = config.upgrade;
import { getToken } from '@/utils/auth'
class Upgrade {

	// 检测平台
	checkOs(apkUrl) {
		uni.getSystemInfo({
			success: (res) => {
				if (res.osName == "android") {
					if (upType == 1 && packageName) {
						plus.runtime.openURL('market://details?id=' + packageName)
					} else {
						this.downloadInstallApp(apkUrl)
					}
				} else if (res.osName == 'ios' && appleId) {
					// apple id 在 app conection 上传的位置可以看到 https://appstoreconnect.apple.com
					plus.runtime.launchApplication({
						action: `itms-apps://itunes.apple.com/cn/app/id${appleId}?mt=8`
					}, function (err) {
						uni.showToast({
							title: err.message,
							icon: 'none'
						})
					})
				}
			}
		})
	}

	// 下载更新
	downloadInstallApp(apkUrl) {
		const dtask = plus.downloader.createDownload(apkUrl, {}, function (d, status) {
			// 下载完成  
			if (status == 200) {
				plus.runtime.install(plus.io.convertLocalFileSystemURL(d.filename), {}, {}, function (error) {
					uni.showToast({
						title: '安装失败',
						icon: 'none'
					});
				})
			} else {
				uni.showToast({
					title: '更新失败',
					icon: 'none'
				});
			}
		});
		// 设置请求头携带token
		dtask.setRequestHeader('Authorization', 'Bearer ' + getToken());

		this.downloadProgress(dtask);
		
	}

	// 下载进度
	downloadProgress(dtask) {
		try {
			dtask.start(); //开启下载任务
			let prg = 0;
			let showLoading = plus.nativeUI.showWaiting('正在下载');
			dtask.addEventListener('statechanged', function (task, status) {
				// 给下载任务设置监听
				switch (task.state) {
					case 1:
						showLoading.setTitle('正在下载');
						break;
					case 2:
						showLoading.setTitle('已连接到服务器');
						break;
					case 3:
						const totalSize = uni.getStorageSync('totalSize');
						prg = parseInt((task.downloadedSize / totalSize) * 100);
						showLoading.setTitle('正在下载 ' + prg + '%');
						break;
					case 4:
						// 下载完成
						plus.nativeUI.closeWaiting();
						uni.removeStorageSync('totalSize');
						break;
				}
			})
		} catch (e) {
			plus.nativeUI.closeWaiting();
			uni.showToast({
				title: '更新失败',
				icon: 'none'
			})
			uni.removeStorageSync('totalSize');
		}
	}

}

export default new Upgrade()