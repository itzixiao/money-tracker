<template>
  <view class="charts-box">
    <qiun-data-charts type="ring" :opts="opts" :chartData="chartData" />
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { fenToYuan } from "@/utils/conversion";
const props = defineProps({
  list: {
    type: Array,
    default: []
  }
});
// 使用 ref 创建响应式数据 chartData，初始为空对象
const chartData = ref({});

// 定义图表的配置选项
const opts = {
  rotate: false,              // 不旋转图表
  rotateLock: false,          // 旋转锁定
  color: [                    // 图表的颜色配置
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
  padding: [5, 0, 0, 5],      // 图表的内边距
  dataLabel: true,
  enableScroll: false,        // 禁用滚动
  legend: {                   // 图例配置
    show: false,               // 显示图例
    position: "right",        // 图例位置
    lineHeight: 25            // 行高
  },
  title: {
    name: "",
    fontSize: 15,
    color: "#666666"
  },
  subtitle: {                 // 图表副标题配置
    name: "",                 // 修改副标题名称
    fontSize: 22,             // 字体大小
    color: "#7cb5ec"          // 字体颜色
  },
  extra: {                    // 额外配置
    ring: {                   // 环形图的配置
      ringWidth: 30,          // 环的宽度
      activeOpacity: 0.5,     // 激活状态的透明度
      activeRadius: 10,       // 激活状态的半径
      offsetAngle: 0,         // 偏移角度
      labelWidth: 15,         // 标签宽度
      border: true,           // 显示边框
      borderWidth: 3,         // 边框宽度
      borderColor: "#FFFFFF", // 边框颜色
      linearType: "custom"    // 自定义线性类型
    }
  }
};

// 定义获取服务器数据的方法
const getServerData = () => {
  // 模拟异步请求数据
  setTimeout(() => {

    props.list.map((item:any) => {
      item.value = fenToYuan(item.money)
    })
    // 假设的返回数据结构（资产类账户统计数据）
    const res = {
      series: [
        {
          data: props.list || []
        }
      ]
    };
    // 更新响应式数据
    chartData.value = res;
  }, 0); // 模拟500ms的延迟
};

// 组件挂载时获取数据
onMounted(() => {
  getServerData(); // 调用获取数据的方法
});
</script>

<style scoped>
.charts-box {
  width: 100%;
  height: 100%;
}
</style>
