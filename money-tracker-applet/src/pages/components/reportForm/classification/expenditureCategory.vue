<template>
  <view class="charts-box">
    <qiun-data-charts type="ring" :opts="opts" :chartData="chartData" />
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
const props = defineProps({
  pieList: {
    type: Array,
    default: []
  }
});


// 创建响应式数据 chartData，初始为空对象
const chartData = ref<{ series: { data: { name: string; value: number }[] }[] }>({ series: [] });

// 定义图表的配置选项
const opts = {
  rotate: false,
  rotateLock: false,
  color: [
    "#1890FF",
    "#91CB74",
    "#FAC858",
    "#EE6666",
    "#73C0DE",
    "#3CA272",
    "#FC8452",
    "#9A60B4",
    "#ea7ccc"
  ],
  padding: [5, 0, 0, 5],
  dataLabel: true,
  enableScroll: false,
  legend: {
    show: false,
    position: "right",
    lineHeight: 25
  },
  title: {
    name: "",
    fontSize: 15,
    color: "#666666"
  },
  subtitle: {
    name: "",
    fontSize: 22,
    color: "#7cb5ec"
  },
  extra: {
    ring: {
      ringWidth: 30,
      activeOpacity: 0.5,
      activeRadius: 10,
      offsetAngle: 0,
      labelWidth: 15,
      border: true,
      borderWidth: 3,
      borderColor: "#FFFFFF",
      linearType: "custom"
    }
  }
};

// 定义获取服务器数据的方法
const getServerData = () => {
  setTimeout(() => {
    const res = {
      series: [
        {
          // data: [
          //   { name: "其他", value: 40 },
          //   { name: "高速公路费", value: 30 },
          //   { name: "晚餐", value: 20 },
          //   { name: "加油", value: 10 },
          //   { name: "火车", value: 5 }
          // ]
          // labelText
          data: props.pieList
        }
      ]
    };
    // 更新响应式数据
    chartData.value = res;
  }, 0);
};

// 组件挂载时获取数据
onMounted(() => {
  getServerData();
});
</script>

<style scoped>
.charts-box {
  width: 100%;
  height: 100%;
}
</style>
