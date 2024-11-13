<template>
  <view class="charts-box">
    <qiun-data-charts type="area" :opts="opts" :chartData="chartData" :ontouch="true" :canvas2d="true" />
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue';
import { reportTrend } from '@/api/reportForm';
import { parseTime } from '@/utils/ruoyi'
import { fenToYuan } from "@/utils/conversion";
// 定义 props
const props = defineProps<{
  parameter: Record<string, any>;
}>();

const chartData = ref({});  // 创建响应式数据
const opts = {
  color: ["#4a7f4a", "#f15252", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4", "#ea7ccc"],
  padding: [0, 0, 0, 0],
  enableScroll: true,
  legend: {
    position: 'top',
    top: '5%',
  },
  xAxis: {
    disableGrid: true,
    scrollShow: true,
    itemCount: 8,
    // props.parameter.logoType == 2 ? 8 : 8,
    axisLine: {
      show: true,
      color: '#000',
      width: 1
    },
    tickLine: {
      show: true,
      length: 5,
      color: '#000'
    }
  },
  yAxis: {
    gridType: "dash",
    dashLength: 2,
    axisLine: {
      show: true,
      color: '#000',
      width: 1
    },
    tickLine: {
      show: true,
      length: 5,
      color: '#000'
    }
  },
  extra: {
    area: {
      type: "curve",
      opacity: 0.3,
      addLine: true,
      width: 2,
      gradient: true,
      activeType: "hollow"
    },
    tooltip: {
      background: '#ffffff',
      borderColor: '#000000',
      borderWidth: 1,
      padding: 10,
      radius: 5,
      textStyle: {
        color: '#333'
      }
    }
  }
};

const getServerData = async () => {
  const res = await reportTrend({ ...props.parameter });
  if (res.code === 200) {
    let x = [];
    if (props.parameter.logoType == 2) {
      x = res.data.x.map((item: any) => `${parseInt(parseTime(item, '{m}'))}月`);
    } else {
      x = res.data.x.map((item: any) => `${parseInt(parseTime(item, '{d}'))}日`);
    }
    if (res.data.revenue && res.data.revenue.length > 0) {
      res.data.revenue = res.data.revenue.map((item: any) => {
        return fenToYuan(item);
      });
    }
    if (res.data.expenditures && res.data.expenditures.length > 0) {
      res.data.expenditures = res.data.expenditures.map((item: any) => {
        return fenToYuan(item);
      });
    }
    chartData.value = {
      categories: x,
      series: [
        { name: "收入", data: res.data.revenue || [] },
        { name: "支出", data: res.data.expenditures || [] },
      ]
    };
  }
};


// const getServerData = () => {
//   setTimeout(() => {
//     const res = {
//       categories: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
//       series: [
//         {
//           name: "收入",
//           data: [1200, 1500, 800, 1700, 2200, 1300, 1900, 1600, 2100, 2400, 3000, 2800],
//         },
//         {
//           name: "支出",
//           data: [1000, 800, 1600, 1500, 1800, 1200, 1100, 1700, 2000, 2200, 2400, 2500],
//         },
//       ]
//     };
//     chartData.value = res;
//   }, 500);
// };

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
