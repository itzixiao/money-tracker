<script setup lang='ts'>
import { reactive, ref, onMounted, defineProps } from 'vue';
import { countReport, countStatistics, reportTrend } from '@/api/reportForm'
import AssetAccountStatistics from './AssetAccountStatistics.vue';
import MonthlyIncomeExpenseChart from './MonthlyIncomeExpenseChart.vue';
import { fenToYuan, yuanToFen, calculatePercentage } from "@/utils/conversion";
import { previewPictures } from '@/utils/ruoyi'
defineOptions({
    name: "基础统计",
})

const props = defineProps({
    bookId: {
        type: String,
        default: ''
    },
    bookTime: {
        type: String,
        default: ''
    },
    logoType: {
        type: String as any,
        default: ''
    },
    userId: {
        type: String as any,
        default: ''
    },
});

const expenditures: any = ref([
    // { name: '营业收入', progress: 80 },
    // { name: '其他收入', progress: 50 },
]);
const revenue: any = ref([
    // { name: '餐饮', progress: 80 },
    // { name: '日用', progress: 50 },
    // { name: '其他支出', progress: 50 },
]);
const parameter = reactive({
    "bookId": props.bookId, // 账本id
    "bookTime": props.bookTime, // 记账年月
    "logoType": props.logoType, // 标识 0.今日 1.本月 2.本年
    "userId": props.userId, // 用户唯一标识
    // "type":'' -- 0-收入 1-支出
})
const count: any = reactive({
    "balance": 0,//结余
    "pen": 0,//记账笔数
    "totalRevenue": 0,//总收入
    "totalSpending": 0//总支出
})

// 获取统计金额
const CountInfo = () => {
    countStatistics({
        ...parameter,
        "type": null
    }).then((res: any) => {
        if (res.code == 200) {
            count.balance = fenToYuan(res.data.balance)
            count.pen = res.data.pen
            count.totalRevenue = fenToYuan(res.data.totalRevenue)
            count.totalSpending = fenToYuan(res.data.totalSpending)
        }
    });
}

// 获取收入来源/支出分布
const getCountReport = () => {
    countReport({ ...parameter }).then(res => {
        if (res.code == 200) {
            expenditures.value = res.data.expenditures || []
            revenue.value = res.data.revenue || []
        }
    })
}

onMounted(() => {
    CountInfo()
    getCountReport()
})

</script>
<template>
    <div>
        <view class="shadow_ balance">
            <view>
                <view>账本流水统计</view>
                <view>结余</view>
                <view>{{ count.balance || 0 }}</view>
                <view>总收入{{ count.totalRevenue || 0 }} | 总支出{{ count.totalSpending || 0 }}</view>
            </view>
            <view class="balance_info">
                <view>
                    <up-icon name="rmb-circle" color="#2979ff" size="28" class="icon"></up-icon>
                    <text>记账里程碑</text>
                </view>
                <view>
                    <text>记账笔数</text>
                    <text class="price">{{ count.pen }}</text>
                </view>
            </view>
        </view>
        <view class="con">
            <view class="tit">收入来源</view>
            <view v-for="(item, index) in revenue" class="con_list" v-if="revenue && revenue.length > 0">
                <view class="con_top">
                    <view>{{ index + 1 }}</view>
                    <view>
                        <image v-if="item.categoryUrl" :src="previewPictures(item.categoryUrl)" />
                        <image v-else src="@/static/images/reportForm/ncome.png" />
                    </view>
                </view>
                <view class="con_bottom">
                    <view>
                        <view>{{ item.name }}</view>
                        <view>{{ fenToYuan(item.money) }}
                        </view>
                    </view>
                    <!-- 进度条 -->
                    <view class="progress">
                        <view class="progress-bar"
                            :style="{ width: calculatePercentage(item.money, count.totalRevenue) + '%' }"></view>
                    </view>
                </view>
            </view>
            <view v-else>
                <up-empty mode="list"></up-empty>
            </view>
        </view>
        <view class="con">
            <view class="tit">支出分布</view>
            <view v-for="(item, index) in expenditures" class="con_list" v-if="expenditures && expenditures.length > 0">
                <view class="con_top">
                    <view>{{ index + 1 }}</view>
                    <view>
                        <image v-if="item.categoryUrl" :src="previewPictures(item.categoryUrl)" />
                        <image v-else src="@/static/images/reportForm/ncome.png" />
                    </view>
                </view>
                <view class="con_bottom">
                    <view>
                        <view>{{ item.name }}</view>
                        <view>{{ fenToYuan(item.money) }}</view>
                    </view>
                    <!-- 进度条 -->
                    <view class="progress">
                        <view class="progress-bar"
                            :style="{ width: calculatePercentage(item.money, count.totalSpending) + '%' }"></view>
                    </view>
                </view>
            </view>
            <view v-else>
                <up-empty mode="list"></up-empty>
            </view>
        </view>
        <view class="con">
            <view>{{ props.logoType == 1 ? '月' : '年' }}度收支趋势</view>
            <view style="height: 350rpx;">
                <MonthlyIncomeExpenseChart :parameter="parameter" />
            </view>
        </view>
        <!-- <view class="con">
            <view>资产类账户统计</view>
            <view style="height: 350rpx;">
                <AssetAccountStatistics />
            </view>
        </view> -->
    </div>
</template>

<style scoped lang='scss'>
.balance {
    height: 400rpx;
    border-radius: 20rpx;
    background: #FBFBFB;
    overflow: hidden;

    >view:first-child {
        padding: 30rpx;
        box-sizing: border-box;
        color: #fff;
        height: 280rpx;
        background: #FD8702;
        background-size: 100% 100%;
        font-size: 28rpx;

        >view:nth-child(2),
        >view:nth-child(4) {
            margin-top: 25rpx;
            font-size: 30rpx;
        }

        >view:nth-child(3) {
            font-size: 48rpx;
            font-weight: 600;
        }
    }

    .balance_info {
        height: 120rpx;
        padding: 0 25rpx;
        box-sizing: border-box;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 28rpx;

        >view:first-child {
            display: flex;
            align-items: center;

            .icon {
                margin-right: 15rpx;
            }
        }

        >view:last-child {
            font-size: 18rpx;
            color: #888;

            .price {
                font-size: 26rpx;
                margin-left: 15rpx;
            }
        }
    }
}

.con:last-child {
    margin-bottom: 20rpx;
}

.con {
    border-radius: 20rpx;
    background: #FBFBFB;
    padding: 30rpx;
    margin-top: 30rpx;

    .tit {
        font-size: 24rpx;
        font-weight: 600;
        margin-bottom: 20px;
    }

    .con_list:last-child {
        margin-bottom: 0;
    }

    .con_list {
        display: flex;
        justify-content: space-between;
        font-size: 22rpx;
        margin-bottom: 30rpx;

        image {
            width: 50rpx;
            height: 50rpx;
            vertical-align: middle;
        }

        >view {
            display: flex;
        }

        .con_top {
            width: 80rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .con_bottom {
            width: calc(100% - 100rpx);
            display: flex;
            flex-direction: column;
            justify-content: space-between;

            >view:first-child {
                display: flex;
                justify-content: space-between;
            }
        }

        .progress {
            height: 7rpx;
            background: #ccc;
        }

        /* 进度条样式 */
        .progress-bar {
            height: 7rpx;
            background-color: #56C4C5;
            border-radius: 3rpx;
        }
    }
}
</style>