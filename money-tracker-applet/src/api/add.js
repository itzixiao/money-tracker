import request from '@/utils/request'

// 获取分类列表
export function getType(data) {
  return request({
    url: '/category/category/tree',
    method: 'post',
    data
  })
}

// 新增分类
export function addClassifyType(data) {
  return request({
    url: '/category/category',
    method: 'post',
    data
  })
}

// 修改分类
export function editClassifyType(data) {
  return request({
    url: '/category/category',
    method: 'put',
    data
  })
}

// 删除分类
export function deleteClassifyType(ids) {
  return request({
    url: `/category/category/${ids}`,
    method: 'delete'
  })
}

// 新增账本明细
export function addBill(data) {
  return request({
    url: '/book/money',
    method: 'post',
    data: data
  })
}

// 获取账本列表
export function getBookList(data) {
  return request({
    url: '/book/book/list',
    method: 'post',
    data
  })
}

// 获取账本明细详细信息
export function getBillDetail(id) {
  return request({
    url: `/book/money/${id}`,
    method: 'get',
  })
}

// 修改账本明细
export function editBill(data) {
  return request({
    url: `/book/money`,
    method: 'put',
    data
  })
}

// 导入支付宝对账单Json
export function updateAliFile(params,data) {
  return request({
    url: `/book/money/import/ali/pay/json`,
    method: 'post',
    params: params,
    data
  })
}

// 导入微信对账单
export function updateWXFile(params,data) {
  return request({
    url: `/book/money/import/wechat/pay/json`,
    method: 'post',
    params: params,
    data
  })
}

// 查询账本用户列表
export function getUserList(data) {
  return request({
    url: `/book/user/list`,
    method: 'post',
    data
  })
}

// 查询账户树
export function getAccountList(data) {
  return request({
    url: `/account/account/tree`,
    method: 'post',
    data
  })
}

// 查询分类图标
export function getIconList(data) {
  return request({
    url: `/category/icon/list`,
    method: 'post',
    data
  })
}