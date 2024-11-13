<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps<{
    title: {
        type: string,
        default: ''
    },
    leftShow: {
        type: Boolean,
        default: false
    },
}>()

const goBack = () => {
    uni.navigateBack()
}

const rightClick = () => {
    console.log('rightClick')
}
</script>

<template>
    <up-navbar class="navbar" :title="props.title" @rightClick="rightClick" :fixed="false">
        <template #left>
            <view v-if="props.leftShow">
                <up-icon name="arrow-left" @click="goBack" />
            </view>
            <view v-else></view>
        </template>
        <template #right>
            <view>

            </view>
        </template>
    </up-navbar>
</template>

