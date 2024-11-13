<script setup lang='ts'>
import { reactive, ref, onMounted } from 'vue';
import accountChart from './accountChart.vue';
import { accountDdetail, countStatistics, accountReport } from '@/api/reportForm'
import { previewPictures } from '@/utils/ruoyi'
import { fenToYuan, calculatePercentage } from "@/utils/conversion";
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

const accountAssetline = ref([])
const assetDetails: any = ref([
    // { name: '银行卡', progress: 80 },
]);
const liabilityDetails: any = ref([
    // { name: '花呗', progress: 80 },
]);

const count: any = reactive({
    "balance": 0,//结余
    "totalRevenue": 0,//总收入
    "totalSpending": 0//总支出
})

const parameter = reactive({
    "bookId": props.bookId, // 账本id
    "bookTime": props.bookTime, // 记账年月
    "logoType": props.logoType, // 标识 0.今日 1.本月 2.本年
    "userId": props.userId, // 用户唯一标识
    // "type":'' -- 0-收入 1-支出
})
// 资产明细/负债明细
const getAccountDdetail = () => {
    accountDdetail({ ...parameter }).then(res => {
        if (res.code == 200) {
            assetDetails.value = res.data.assetDetails || []
            liabilityDetails.value = res.data.liabilityDetails || []
        }
    })
}

// 获取统计金额
const CountInfo = () => {
    countStatistics({
        ...parameter,
        "type": null
    }).then((res: any) => {
        if (res.code == 200) {
            count.balance = fenToYuan(res.data.balance)
            count.totalRevenue = fenToYuan(res.data.totalRevenue)
            count.totalSpending = fenToYuan(res.data.totalSpending)
        }
    });
}


onMounted(() => {
    CountInfo()
    getAccountDdetail()
})

</script>
<template>
    <div>
        <view class="balance shadow_">
            <view>
                <view>账户统计</view>
                <view>净资产</view>
                <view>{{ count.balance }}</view>
                <view>资产{{ count.totalRevenue }} | 负债{{ count.totalSpending }}</view>
            </view>
            <view style="height: 400rpx;">
                <accountChart :parameter="parameter" />
            </view>
        </view>
        <view class="con">
            <view class="tit">资产明细</view>
            <view v-for="(item, index) in assetDetails" class="con_list" v-if="assetDetails && assetDetails.length > 0">
                <view class="con_top">
                    <view>{{ index + 1 }}</view>
                    <view>
                        <image v-if="item.accountUrl" :src="previewPictures(item.accountUrl)" />
                        <image v-else src="@/static/images/reportForm/ncome.png" />
                    </view>
                </view>
                <view class="con_bottom">
                    <view>
                        <view>{{ item.name }}</view>
                        <view>{{ fenToYuan(item.balance) }}</view>
                    </view>
                    <!-- 进度条 -->
                    <view class="progress">
                        <view class="progress-bar"
                            :style="{ width: calculatePercentage(item.balance, count.totalRevenue) + '%' }"></view>
                    </view>
                </view>
            </view>
            <view v-else>
                <up-empty mode="list"></up-empty>
            </view>
        </view>
        <view class="con">
            <view class="tit">负债明细</view>
            <view v-for="(item, index) in liabilityDetails" class="con_list"
                v-if="liabilityDetails && liabilityDetails.length > 0">
                <view class="con_top">
                    <view>{{ index + 1 }}</view>
                    <view>
                        <image v-if="item.accountUrl" :src="previewPictures(item.accountUrl)" />
                        <image v-else src="@/static/images/reportForm/ncome.png" />
                    </view>
                </view>
                <view class="con_bottom">
                    <view>
                        <view>{{ item.name }}</view>
                        <view>{{ fenToYuan(item.balance) }}</view>
                    </view>
                    <!-- 进度条 -->
                    <view class="progress">
                        <view class="progress-bar"
                            :style="{ width: calculatePercentage(item.balance, count.totalSpending) + '%' }"></view>
                    </view>
                </view>
            </view>
            <view v-else>
                <up-empty mode="list"></up-empty>
            </view>
        </view>
    </div>
</template>

<style scoped lang='scss'>
.balance {
    padding-bottom: 20rpx;
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