import upload from '@/utils/upload'
import request from '@/utils/request'

// 1.版本文件上传 
export function updateVersion(formData) {
  return request({
    url: '/version/updates/upload/version',
    method: 'post',
    data: formData,
    // headers: {
    //   'Content-Type': 'multipart/form-data'
    // }
  })
}

// 查询版本文件
export function searchVersion(data) {
    return request({
      url: '/version/updates/query/file',
      method: 'post',
      data: data
    })
}
  
// 下载
export function downloadVersion(objectName) {
  return request({
    url: `/minio/download/${objectName}`,
    method: 'get'
  })
}

