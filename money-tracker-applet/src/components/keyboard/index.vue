<script setup lang='ts'>
import {
  ref,
  defineProps,
  onMounted,
  defineEmits,
  defineOptions,
  watch,
} from "vue";
import { dealBracket } from "@/utils/cal";
import { addBill, editBill } from "@/api/add";
import { fenToYuan, yuanToFen } from "@/utils/conversion";
import { onShow } from "@dcloudio/uni-app";

defineOptions({
  name: "KeyBoard",
});

const props = defineProps({
  recordForm: {
    type: Object,
    default: null,
  },
  detailId: {
    type: Number,
    default: null,
  },
});

const money = ref("");
const uToastRef: any = ref(null);
const emits = defineEmits(["setData"]);
const buttonType = ref("");
const nowNum = ref("");
const nowType: any = ref([]);
const typeLength = ref();
const formData = ref()

const numInput = (e: any) => {
  let type = e.target.dataset.type;
  nowType.value.push(type);
  typeLength.value = nowType.value.length;
  buttonType.value = e.target.dataset.type;
  let currentMoney = money.value;
  if (type === "num") {
    handleNum(e, currentMoney);
  } else if (type === "del" && currentMoney !== "") {
    nowType.value.splice(typeLength.value - 1, 1);
    nowType.value.splice(typeLength.value - 2, 1);

    if (currentMoney === "0.") {
      money.value = "";
      emits("setData", { money: "" });
      nowType.value = [];
      nowNum.value = "";
    } else {
      money.value = currentMoney.substring(0, currentMoney.length - 1);
      nowNum.value = nowNum.value.substring(0, nowNum.value.length - 1);
      emits("setData", {
        money: currentMoney.substring(0, currentMoney.length - 1),
      });
    }
  } else if (type === "empty") {
    money.value = "";
    emits("setData", { money: "" });
    nowType.value = [];
    nowNum.value = "";
  } else if (type === "op") {
    let op = e.target.dataset.op;
    handleOp(op, currentMoney);
  } else if (type === "equal") {
    ok();
  }
};

// 执行数字，小数点
const handleNum = (e: any, currentMoney: any) => {
  let num = e.target.dataset.num;
  if (nowType.value[typeLength.value - 2] == "op") {
    nowNum.value = "";
  }
  if (num === ".") {
    if (currentMoney == "" || currentMoney == "0.00") {
      // 当前为第一位
      money.value = "0.";
      emits("setData", { money: "0." });
      nowNum.value = "0.";
    } else if (
      currentMoney !== "" &&
      nowType.value[typeLength.value - 2] == "op"
    ) {
      money.value = currentMoney + "0.";
      emits("setData", { money: currentMoney + "0." });
      nowNum.value = "0.";
    } else {
      // 不是第一位
      if (typeLength.value == 2) {
        money.value = currentMoney + num;
        emits("setData", { money: currentMoney + num });
        nowNum.value = nowNum.value + num;
      } else {
        if (nowNum.value.indexOf(".") !== -1) {
          nowNum.value.substring(0, nowNum.value.length - 1);
          return;
        } else {
          money.value = currentMoney + num;
          emits("setData", { money: currentMoney + num });
          nowNum.value = nowNum.value + num;
        }
      }
    }
  } else {
    if (currentMoney === "0") {
      money.value = "0." + num;
      emits("setData", { money: "0." + num });
      nowNum.value = "0." + num;
    } else if (currentMoney === "0.00") {
      money.value = num;
      emits("setData", { money: num });
      nowNum.value = num;
    } else {
      let length = nowNum.value.length;
      let index = nowNum.value.indexOf(".");
      // 限制小数位数最多为 2 位
      if (index !== -1 && length - index > 2) {
        return;
      }
      money.value = currentMoney + num;
      emits("setData", { money: currentMoney + num });
      nowNum.value = nowNum.value + num;
    }
  }
};

// 执行运算符
const handleOp = (op: any, currentMoney: any) => {
  if (currentMoney == "0.00" || currentMoney === "") {
    nowType.value = [];
    // 如果输入为空，则不做任何操作
    return;
  } else {
    nowNum.value = op;
    // 在当前值的末尾添加运算符
    if (
      typeLength.value >= 2 &&
      nowType.value[typeLength.value - 1] == nowType.value[typeLength.value - 2]
    ) {
      currentMoney = currentMoney.substring(0, currentMoney.length - 1);
      let val = currentMoney + op;
      money.value = currentMoney + op;
      emits("setData", { money: val });
    } else {
      let val = currentMoney + op;
      money.value = currentMoney + op;
      emits("setData", { money: val });
    }
  }
};

const handleEnd = () => {
  if ((nowNum.value == "0.")) {
    nowNum.value = "";
    money.value = money.value.substring(0, money.value.length - 1);
    emits("setData", { money: money.value });
  }
  if ( nowType.value[typeLength.value - 2] == 'op') {
    money.value = money.value.substring(0, money.value.length - 1);
    emits("setData", { money: money.value });
  }
};

const ok = () => {
  handleEnd()
  if (money.value == "") {
    uToastRef.value.show({
      type: "warning",
      title: "",
      message: "请输入金额！",
      iconUrl: "",
    });
  } else if (formData.value.bookId == null) {
    uToastRef.value.show({
      type: "warning",
      title: "",
      message: "当前未选择账本，请选择账本或新建账本！",
      iconUrl: "",
    });
  } else {
    const result = dealBracket(money.value);
    emits("setData", { money: result });
    let data = formData.value;
    data.money = data.type == '1' ? -yuanToFen(result * 1) : yuanToFen(result * 1);
    if (props.detailId == null) {
      addBill(data).then((res) => {
        if (res.code == 200 && res.data) {
          uToastRef.value.show({
            type: "success",
            title: "",
            message: "记一笔成功！",
            iconUrl: "",
          });
          nowNum.value = ''
          nowType.value = []
          if (buttonType.value == "equal") {
            money.value = '0.00'
            emits("setData", { money: "0.00" });
          } else {
            back();
            money.value = "";
            emits("setData", { money: "" });
          }
        }
      });
    } else {
      data.id = props.detailId;
      editBill(data).then((res) => {
        if (res.code == 200 && res.data) {
          uToastRef.value.show({
            type: "success",
            title: "",
            message: "修改成功！",
            iconUrl: "",
          });
          if (buttonType.value == "equal") {
            return;
          } else {
            back();
          }
        }
      });
    }
  }
};

const back = () => {
  uni.switchTab({ url: "/pages/index" });
};

watch(
  () => props.recordForm,
  (val) => {
    money.value = String(val.money);
    formData.value = JSON.parse(JSON.stringify(val))
  },
  {
    deep: true
  }
);
</script>
<template>
  <view class="btn-input-wrap">
    <view class="btn-num-wrap">
      <view
        class="btn-num jz-input-add"
        data-op="+"
        data-type="op"
        @click="numInput"
        hover-class="btn-hover"
        >+</view
      >
      <view
        class="btn-num jz-input-sub"
        data-op="-"
        data-type="op"
        @click="numInput"
        hover-class="btn-hover"
        >-</view
      >
      <view
        class="btn-num jz-input-mul"
        data-op="*"
        data-type="op"
        @click="numInput"
        hover-class="btn-hover"
        >*</view
      >
      <view
        class="btn-num jz-input-7"
        data-num="7"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >7</view
      >
      <view
        class="btn-num jz-input-8"
        data-num="8"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >8</view
      >
      <view
        class="btn-num jz-input-9"
        data-num="9"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >9</view
      >
      <view
        class="btn-num jz-input-4"
        data-num="4"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >4</view
      >
      <view
        class="btn-num jz-input-5"
        data-num="5"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >5</view
      >
      <view
        class="btn-num jz-input-6"
        data-num="6"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >6</view
      >
      <view
        class="btn-num jz-input-1"
        data-num="1"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >1</view
      >
      <view
        class="btn-num jz-input-2"
        data-num="2"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >2</view
      >
      <view
        class="btn-num jz-input-3"
        data-num="3"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >3</view
      >
      <view
        class="btn-num jz-input-empty"
        @click="numInput"
        data-type="empty"
        hover-class="btn-hover"
        >清空</view
      >
      <view
        class="btn-num jz-input-dot"
        data-num="."
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >.</view
      >
      <view
        class="btn-num jz-input-0"
        data-num="0"
        data-type="num"
        @click="numInput"
        hover-class="btn-hover"
        >0</view
      >
    </view>
    <view class="btn-op-wrap">
      <view
        class="btn-op jz-input-div"
        data-op="/"
        data-type="op"
        @click="numInput"
        hover-class="btn-hover"
        >/</view
      >
      <view
        class="btn-op jz-input-del"
        @click="numInput"
        data-type="del"
        hover-class="btn-hover"
        >删除</view
      >
      <view
        class="btn-op jz-input-continue"
        data-op="="
        data-type="equal"
        @click="numInput"
        hover-class="btn-hover"
        >确认并继续</view
      >
      <view class="btn-op jz-input-ok" @click="ok" hover-class="btn-hover"
        >确认</view
      >
    </view>
    <up-toast ref="uToastRef"></up-toast>
  </view>
</template>

<style scoped lang='scss'>
.btn-input-wrap {
  font-size: 4vw;
  display: flex;
  font-family: "微软雅黑";
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  .btn-num-wrap {
    // box-sizing: border-box;
    display: flex;
    flex-wrap: wrap;
    width: 75%;
    height: 30vh;
    .btn-num {
      box-sizing: border-box;
      // background-color: #3b414d;
      // color: white;
      width: 33.3%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
  .jz-input-add,
  .jz-input-sub,
  .jz-input-mul,
  .jz-input-7,
  .jz-input-8,
  .jz-input-9,
  .jz-input-4,
  .jz-input-5,
  .jz-input-6,
  .jz-input-1,
  .jz-input-2,
  .jz-input-3,
  .jz-input-empty,
  .jz-input-dot,
  .jz-input-0,
  .jz-input-div,
  .jz-input-del {
    box-shadow: 0 2px 5px rgba(1, 1, 1, 0.1);
    font-size: 35rpx;
  }
  .btn-op-wrap {
    display: flex;
    width: 25%;
    height: 30vh;
    flex-direction: column;
    .btn-op {
      justify-content: center;
      align-items: center;
      display: flex;
      // width: 25vw;
    }

    .jz-input-div,
    .jz-input-continue,
    .jz-input-del {
      height: 6vh;
      box-shadow: 0 2px 5px rgba(1, 1, 1, 0.1);
    }

    .jz-input-continue {
      font-size: 26rpx;
    }

    .jz-input-ok {
      height: 12vh;
      color: #fd9802;
      font-size: 40rpx;
    }
  }
  .btn-hover {
    box-shadow: 0 2px 5px rgba(1, 1, 1, 0.5);
  }
}
</style>