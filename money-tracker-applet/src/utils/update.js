export default {
    // 比较两个版本号
    compareVersions(v1, v2) {
        // console.log(v1, v2) 
        const [a, b] = [v1, v2].map(v => v.split('.').map(Number));
        for (let i = 0; i < Math.max(a.length, b.length); i++) {
            const [partA, partB] = [a[i] || 0, b[i] || 0];
            if (partA !== partB) return partA - partB;
        }
        return 0;
    },
    // 封装上传Apk
    $uploadForm(url, data, filePath,token) {
        return new Promise((resolve, reject) => {
            uni.uploadFile({
                url: url,
                filePath: filePath, // 这里应该传递文件路径字符串
                name: 'file',
                formData: data,
                header: {
                    Authorization: `Bearer ${token}` // 添加 token 到 header
                },
                success: (res) => {
                    resolve(res);
                },
                fail: (res) => {
                    reject(res);
                }
            })
        })
    },

}