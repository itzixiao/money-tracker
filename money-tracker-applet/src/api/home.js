import request from '@/utils/request'

// 账本明细列表
export function bookDetailList(data) {
  return request({
    url: '/book/money/group/list',
    method: 'post',
    data: data
  })
}

// 账本明细列表
export function deleteBookDetail(id) {
  return request({
    url: `/book/money/${id}`,
    method: 'delete',
  })
}

// 统计账本明细信息
export function getCountInfo(data) {
  return request({
    url: `/book/money/count/info`,
    method: 'post',
    data
  })
}

// 查询账本明细日历聚合列表
export function getCalendarList(data) {
  return request({
    url: `/book/money/calendar/group/list`,
    method: 'post',
    data
  })
}
