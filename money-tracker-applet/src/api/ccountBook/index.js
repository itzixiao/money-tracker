import request from '@/utils/request'

//新增账本
export function addBook(data) {
    return request({
        url: '/book/book',
        method: 'post',
        data: data
    })
}

// 修改账本
export function updateBook(data) {
    return request({
      url:'/book/book',
      method: 'put',
      data: data
    })
  }
  

//新增账本明细
export function bookMoney(data) {
    return request({
        url: '/book/money',
        method: 'post',
        data: data
    })
}

//账本列表
export function bookALL(data) {
    return request({
        url: '/book/book/list',
        method: 'post',
        data: data
    })
}

// 删除账本
export function delBook(ids) {
    return request({
      url: '/book/book/' + ids,
      method: 'delete'
    })
  }
  

// 查询账本成员列表
export function bookUser(data) {
  return request({
      url: '/book/user/list',
      method: 'post',
      data: data
  })
}

// 修改账本成员名称 
export function updateUser(data) {
  return request({
    url:'/book/user',
    method: 'put',
    data: data
  })
}

// 邀请用户加入账本
export function inviteBookUser(data) {
  return request({
      url: '/book/user/invite/user',
      method: 'post',
      data: data
  })
}