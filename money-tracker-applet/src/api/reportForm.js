import request from '@/utils/request'

// *** 基础统计 
// 基础统计账本报表 收入来源/支出分布
export function countStatistics(data) {
    return request({
        url: '/book/money/count/flow/statistics',
        method: 'post',
        data: data
    })
}

// 获取统计金额
export function countReport(data) {
    return request({
        url: '/book/money/count/report',
        method: 'post',
        data: data
    })
}

// 收支趋势
export function reportTrend(data) {
    return request({
        url: '/book/money/count/report/trend',
        method: 'post',
        data: data
    })
}

// *** 分类 
// 分类报表统计
export function countClassify(data) {
    return request({
        url: '/book/money/count/classify',
        method: 'post',
        data: data
    })
}

// *** 账户
// 账户资产明细
export function accountDdetail(data) {
    return request({
        url: '/book/money/count/account/detail',
        method: 'post',
        data: data
    })
}

// 账户资产折线图
export function accountReport(data) {
    return request({
        url: '/book/money/count/account/line',
        method: 'post',
        data: data
    })
}

// *** 成员
// 成员报表记账数据
export function memberAccount(data) {
    return request({
        url: '/book/money/count/member',
        method: 'post',
        data: data
    })
}

// 成员支出对比 
export function memberCompare(data) {
    return request({
        url: '/book/money/count/member/compare',
        method: 'post',
        data: data
    })
}


// 成员支出/收入统计
export function memberIncome(data) {
    return request({
        url: '/book/money/count/expenditure/income',
        method: 'post',
        data: data
    })
}


