<template>
  <view class="charts-box">
    <qiun-data-charts type="line" :opts="opts" :chartData="chartData" :ontouch="true" :canvas2d="true" />
  </view>
</template>

<script setup>
import { accountReport } from '@/api/reportForm'
import { ref, onMounted } from 'vue';
import { parseTime } from '@/utils/ruoyi'
const props = defineProps({
  parameter: {
    type: Object,
    default: {}
  }
});

const chartData = ref({});  // 使用 ref 创建响应式数据
const opts = {
  color: ["#FD8702", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4", "#ea7ccc"],
  padding: [30, 10, 10, 10],
  enableScroll: true,
  legend: {
    show: false,
    position: 'top', // 调整图例位置
    top: '5%', // 进一步调整图例的位置，靠上
  },
  xAxis: {
    disableGrid: true,
    scrollShow: true,
    itemCount: 8,
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
  const reponse = await accountReport({ ...props.parameter });
  if (reponse.code === 200) {
    let x = [];
    if (props.parameter.logoType == 2) {
      x = reponse.data.x.map((item) => `${parseInt(parseTime(item, '{m}'))}月`);
    } else {
      x = reponse.data.x.map((item) => `${parseInt(parseTime(item, '{d}'))}日`);
    }
    const res = {
      categories: x || [],
      series: [
        {
          name: "净资产",
          data: reponse.data.y  || [],
        },
      ]
    };
    chartData.value = res;
  }
};

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
