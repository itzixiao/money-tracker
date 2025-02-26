
const suitArr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']
const signArr = ['+', '-', '*', '/']
// 处理多重括号:类比入栈和出栈操作：以一对()为最小执行单位
export function dealBracket(str: any) {
  // 在执行操作前去除多余的符号
  str = dropSign(str)
  // 判断解括号次数,防止死循环
  let bracketLength = str.split(')').length - 1
  for (let i = 0; i < bracketLength; i++) {
    let rightBracketIdx = str.indexOf(')')
    let strInclueBracket = str.substr(0, rightBracketIdx + 1)
    let lastLeftBracketIdx = strInclueBracket.lastIndexOf('(')
    let fragment = str.slice(lastLeftBracketIdx, rightBracketIdx + 1)
    str = str.slice(0, lastLeftBracketIdx) + dealNeedlessSign(fragment, 1) + str.slice(rightBracketIdx + 1, str.length)
  }
  let newArr = cutStr(str)
  let value = simpleOperation(newArr)
  return value
}
// 去除多余的符号:取最后一个
export function dealNeedlessSign(str: any, type: any) {
  // 去除首尾括号
  if (type === 1) { str = str.slice(1, str.length - 1) }
  let arr = cutStr(str)
  // 计算运算和
  let resultVal = simpleOperation(arr)
  return resultVal
}
// 简单运算:字符串加减乘除
export function simpleOperation(newArr: any) {
  while (newArr.length >= 3) {
    // 先判断乘号分支;
    let mulSign = newArr.indexOf('*') > 0
    let diviSign = newArr.indexOf('/') > 0
    if (mulSign) {
      // 判断是否有除号:有则判断顺序执行,无则执行乘法
      if (diviSign) {
        if (mulSign < diviSign) {
          newArr = doOperation(newArr, '*')
        } else { newArr = doOperation(newArr, '/') }
      } else {
        newArr = doOperation(newArr, '*')
      }
    } else {
      // 是否有除:有则执行
      if (diviSign) {
        newArr = doOperation(newArr, '/')
      } else {
        // 然后判断加减分支
        let addSign = newArr.indexOf('+') > 0
        let subSign = newArr.indexOf('-') > 0
        if (addSign) {
          // 判断是否有加号:有则判断顺序执行,无则执行减法
          if (subSign) {
            if (addSign < subSign) {
              newArr = doOperation(newArr, '+')
            } else { newArr = doOperation(newArr, '-') }
          } else {
            newArr = doOperation(newArr, '+')
          }
        } else {
          // 是否有减:有则执行
          if (subSign) {
            newArr = doOperation(newArr, '-')
          }
        }
      }
    }
  }
  return newArr[0]
}
// 符号运算
export function doOperation(arr: any, sign: any) {
  while (arr.indexOf(sign) > 0 && arr.indexOf(sign) < (arr.length - 1)) {
    let idx = arr.indexOf(sign)
    let concatVal: any = ''
    if (sign === '*') { concatVal = Number(arr[idx - 1]) * Number(arr[idx + 1]) }
    if (sign === '/') { concatVal = Number(arr[idx - 1]) / Number(arr[idx + 1]) }
    if (sign === '+') { concatVal = Number(arr[idx - 1]) + Number(arr[idx + 1]) }
    if (sign === '-') { concatVal = Number(arr[idx - 1]) - Number(arr[idx + 1]) }
    arr.splice(idx - 1, 3, concatVal)
  }
  return arr
}
// 将字符串分割成数字和符号的数组
export function cutStr(str: any) {
  let newArr = []
  let tempStr = ''
  for (let i = 0; i < str.length; i++) {
    if (i === str.length - 1) {
      tempStr += str.charAt(i)
      newArr.push(tempStr)
    } else {
      if (signArr.indexOf(str.charAt(i)) >= 0) {
        // 是符号且为减号
        if (str.charAt(i) === '-') {
          if (tempStr.indexOf('-') < 0) {
            if (tempStr !== '') { newArr.push(tempStr) }
            tempStr = '-'
          } else {
            if (tempStr === '-') {
              tempStr = ''
            } else {
              newArr.push(tempStr)
              newArr.push(str.charAt(i))
              tempStr = ''
            }
          }
        } else {
          if (tempStr !== '') { newArr.push(tempStr) }
          newArr.push(str.charAt(i))
          tempStr = ''
        }
      } else {
        tempStr += str.charAt(i)
      }
    }
  }
  // 最后将减号变负数留下的空隙补'+'
  let idxArr = []
  for (let i = 0; i < newArr.length; i++) {
    if (!isNaN(Number(newArr[i - 1])) && !isNaN(Number(newArr[i]))) {
      idxArr.push(i)
    }
  }
  idxArr.reverse().forEach(item => { newArr.splice(item, 0, '+') })
  return newArr
}
// 丢弃多余的符号
export function dropSign(str: any) {
  let arr = str.split('')
  // 数组:符号对应下标
  let idxArr = []
  // 数组:丢弃符号下标
  let lostArr: any = []
  for (let idx in arr) {
    if (signArr.indexOf(arr[idx]) >= 0) { idxArr.push(Number(idx)) }
  }
  if (idxArr.length > 1) {
    idxArr.reduce(function (x, y) {
      if (Number(y) - Number(x) === 1) { lostArr.push(Number(x)) }
      return y
    })
  }
  lostArr.reverse().forEach((item: any) => { arr.splice(item, 1) })
  return arr.reduce(function (x: any, y: any) { return String(x) + y })
}