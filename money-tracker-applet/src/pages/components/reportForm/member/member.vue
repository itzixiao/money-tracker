<script setup lang='ts'>
import { reactive, ref, onMounted } from 'vue';
import MemberCategory from './MemberCategory.vue';
import MemberBookkeeping from './MemberBookkeeping.vue';
import { memberAccount, memberCompare, memberIncome } from '@/api/reportForm'
import { previewPictures } from '@/utils/ruoyi'
import { fenToYuan, yuanToFen, calculatePercentage } from "@/utils/conversion";
defineOptions({
    name: "",
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

const memberColumn: any = ref([]) // 成员报表记账数据
// const expenditure = reactive([
//     { name: '老婆', progress: 80 },
// ]);

const expenditure: any = ref([]);
const totalAllRevenue: any = ref(0);
const totalAllSpending: any = ref(0);


const income: any = ref([
    // { name: '家庭公用', progress: 90 },
]);
const expenses: any = ref([
    // { name: '家庭公用', progress: 90 },
]);
const parameter = reactive({
    "bookId": props.bookId, // 账本id
    "bookTime": props.bookTime, // 记账年月
    "logoType": props.logoType, // 标识 0.今日 1.本月 2.本年
    "userId": props.userId, // 用户唯一标识
    // "type":'' -- 0-收入 1-支出
})

// 成员记账数据
const getMemberAccount = () => {
    memberAccount({ ...parameter }).then(res => {
        if (res.code == 200) {
            memberColumn.value = res.data
        }
    })
}

// 成员支出对比
const getMemberCompare = () => {
    memberCompare({ ...parameter }).then(res => {
        totalAllRevenue.value = fenToYuan(res.data.totalAllRevenue) || 0
        totalAllSpending.value = fenToYuan(res.data.totalAllSpending) || 0
        expenditure.value = res.data.compareVOS || []
    })
}

// 获取成员支出/收入统计
const getMemberIncome = (id: any) => {
    memberIncome({
        ...parameter,
        type: id
    }).then(res => {
        if (res.code == 200) {
            if (id == 0) {
                income.value = res.data
                income.value.money = fenToYuan(income.value.money) || 0
            } else if (id == 1) {
                expenses.value = res.data
                expenses.value.money = fenToYuan(expenses.value.money) || 0
            }
        }
    })
}
onMounted(() => {
    getMemberAccount()
    getMemberCompare()
    getMemberIncome(0) // 收入
    getMemberIncome(1) // 支出
})
</script>

<template>
    <view class="classification">
        <view class="classification_box">
            <view class="classification_box_header">
                <view>成员记账数据</view>
                <view>
                    <span class="TotalExpenditure">记账笔数 <span class="price">{{ memberColumn.total }}</span></span>
                </view>
            </view>
            <view style="height: 350rpx;" v-if="memberColumn.x && memberColumn.x.length>0">
                <MemberBookkeeping :list="memberColumn" />
            </view>
            <view v-else>
                <up-empty  mode="favor" text="暂无数据" ></up-empty>
            </view>
        </view>
        <!-- 对比 -->
        <view class="classification_box contrast">
            <view class="classification_box_header">
                <view>成员收支对比</view>
            </view>
            <view class="con">
                <view v-for="(item, index) in expenditure" :key="index" class="con_list"v-if="expenditure && expenditure.length > 0">
                    <view class="con_top">
                        <view></view>
                        <view>
                            <image v-if="item.avatar" :src="previewPictures(item.avatar)" />
                            <image v-else src="@/static/images/reportForm/ncome.png" />
                        </view>
                    </view>
                    <view class="con_bottom">
                        <view>
                            <view>{{ item.name }}</view>
                            <view>{{}}</view>
                        </view>
                        <!-- 进度条 -->
                        <view>
                            <view class="progress">
                                <view class="progress-bar"
                                    :style="{ width: calculatePercentage(item.totalSpending, totalAllSpending) + '%' }">
                                </view>
                            </view>
                            <view class="progress" style="margin-top: 5rpx;">
                                <view class="progress-bars"
                                    :style="{ width: calculatePercentage(item.totalRevenue, totalAllRevenue) + '%' }">
                                </view>
                            </view>
                        </view>
                        <view class="contrast_price">
                            <view>总支出 {{ fenToYuan(item.totalSpending) }}</view>
                            <view>总收入 {{ fenToYuan(item.totalRevenue) }}</view>
                        </view>
                    </view>
                </view>
                <view v-else>
                    <up-empty mode="list"></up-empty>
                </view>
            </view>
        </view>
        <view class="classification_box">
            <view class="classification_box_header">
                <view>成员支出统计</view>
                <view>
                    <span class="TotalExpenditure">总支出 <span class="price">{{ expenses.money }}</span></span>
                </view>
            </view>
            <view style="height: 350rpx;" v-if="expenses.vos && expenses.vos.length>0">
                <memberCategory :list="expenses.vos" />
            </view>
            <view v-else>
                <up-empty  mode="favor" text="暂无数据" ></up-empty>
            </view>
            <view class="con">
                <view v-for="(item, index) in expenses.vos" :key="index" class="con_list">
                    <view class="con_top">
                        <view>{{ index + 1 }}</view>
                        <view>
                            <image src="@/static/images/reportForm/ncome.png" />
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
                                :style="{ width: calculatePercentage(item.money, expenses.money) + '%' }"></view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <view class="classification_box">
            <view class="classification_box_header">
                <view>成员收入统计</view>
                <view>
                    <span class="TotalExpenditure">总收入 <span class="price">{{ income.money }}</span></span>
                </view>
            </view>
            <view style="height: 350rpx;" v-if="income.vos && income.vos.length>0">
                <memberCategory :list="income.vos"/>
            </view>
            <view v-else>
                <up-empty  mode="favor" text="暂无数据" ></up-empty>
            </view>
            <view class="con">
                <view v-for="(item, index) in income.vos" :key="index" class="con_list">
                    <view class="con_top">
                        <view>{{ index + 1 }}</view>
                        <view>
                            <image src="@/static/images/reportForm/ncome.png" />
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
                                :style="{ width: calculatePercentage(item.money, income.money) + '%' }"></view>
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

// 对比
.contrast {
    .contrast_price {
        display: flex;

        >view {
            display: flex;
            align-items: center;
        }

        >view:first-child {
            margin-right: 30rpx;
        }

    }

    .contrast_price>view::before {
        content: '';
        display: block;
        width: 10rpx;
        height: 10rpx;
        border-radius: 50%;
        background-color: #56C4C5;
        margin-right: 10rpx;
    }

    .contrast_price>view:last-child:before {
        background-color: #FD8702;
    }

    .con_list {
        align-items: baseline;
    }

    .con_bottom {
        height: 100rpx;
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
            border-radius: 10rpx;
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
        .progress-bar,
        .progress-bars {
            height: 7rpx;
            background-color: #56C4C5;
            border-radius: 3rpx;
        }

        /* 进度条收入样式 */
        .progress-bars {
            background-color: #FD8702;
        }
    }
}
</style>
