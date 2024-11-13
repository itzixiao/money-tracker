<script setup lang='ts'>
import { reactive, ref, onMounted } from 'vue';
import expenditureCategory from './expenditureCategory.vue';
import { countClassify } from '@/api/reportForm'
import { fenToYuan, yuanToFen, calculatePercentage } from "@/utils/conversion";
import { previewPictures } from '@/utils/ruoyi'
defineOptions({
    name: "分类",
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
    // { name: '房租', progress: 80 },
]);

const revenue: any = ref([
    // { name: '工资', progress: 90 },
]);

const parameter = reactive({
    "bookId": props.bookId, // 账本id
    "bookTime": props.bookTime, // 记账年月
    "logoType": props.logoType, // 标识 0.今日 1.本月 2.本年
    "userId": props.userId, // 用户唯一标识
    // "type":'' -- 0-收入 1-支出
})
const count: any = reactive({
    "totalExpenditure": 0,   //	总支出金额
    "totalExpenditureCount": 0, //	总支出笔数
    "totalRevenue": 0, //	总收入金额
    "totalRevenueCount": 0 //	总收入笔数
})
const ExpenditurePieChart: any = ref([]) // 支出饼图统计数据
const IncomePieChart: any = ref([]) // 收入饼图统计数据

const getCountClassify = () => {
    ExpenditurePieChart.value = []
    IncomePieChart.value = []
    countClassify({
        ...parameter
    }).then(res => {
        count.totalExpenditure = fenToYuan(res.data.totalExpenditure)
        count.totalExpenditureCount = res.data.totalExpenditureCount
        count.totalRevenue = fenToYuan(res.data.totalRevenue)
        count.totalSpending = res.data.totalRevenueCount

        revenue.value = res.data.revenue || []
        expenditures.value = res.data.expenditures || []
        expenditures.value.map((item: any) => {
            ExpenditurePieChart.value.push({
                name: item.name,
                value: fenToYuan(item.money),
            });
        });
        revenue.value.map((item: any) => {
            IncomePieChart.value.push({
                name: item.name,
                value: fenToYuan(item.money)
            });
        });
    })
}

onMounted(() => {
    getCountClassify()
})
</script>

<template>
    <view class="classification">
        <view class="classification_box">
            <view class="classification_box_header">
                <view>支出分类统计</view>
                <view>
                    <span class="TotalExpenditure">总支出 <span class="price">{{ count.totalExpenditure }}</span></span>
                    <span style="margin-left: 20rpx;">记账笔数 <span class="price">{{ count.totalExpenditureCount
                            }}</span></span>
                </view>
            </view>
            <view style="height: 350rpx;" v-if="ExpenditurePieChart&&ExpenditurePieChart.length>0">
                <expenditureCategory :pieList="ExpenditurePieChart"/>
            </view>
            <view v-else>
                <up-empty  mode="favor" text="暂无数据" ></up-empty>
            </view>
            <view class="con">
                <view v-for="(item, index) in expenditures" :key="index" class="con_list">
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
                                :style="{ width: calculatePercentage(item.money, count.totalExpenditure) + '%' }">
                            </view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <view class="classification_box">
            <view class="classification_box_header">
                <view>收入分类统计</view>
                <view>
                    <span class="TotalExpenditure">总收入 <span class="price">{{ count.totalRevenue }}</span></span>
                    <span style="margin-left: 20rpx;">记账笔数 <span class="price">{{ count.totalSpending }}</span></span>
                </view>
            </view>
            <view style="height: 350rpx;" v-if="IncomePieChart&&IncomePieChart.length>0">
                <expenditureCategory :pieList="IncomePieChart"/>
            </view>
            <view v-else>
                <up-empty  mode="favor" text="暂无数据" ></up-empty>
            </view>
            <view class="con">
                <view v-for="(item, index) in revenue" :key="index" class="con_list">
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
                                :style="{ width: calculatePercentage(item.money, count.totalRevenue) + '%' }"></view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<style scoped lang='scss'>
.classification {
    .classification_box {
        border-radius: 20rpx;
        background: #FBFBFB;
        overflow: hidden;
        margin-bottom: 30rpx;
        padding: 30rpx;
        box-sizing: border-box;

        .classification_box_header {
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
        }

        .price {
            color: #56C4C5;
            font-size: 32rpx;
        }
    }
}

.con {
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
