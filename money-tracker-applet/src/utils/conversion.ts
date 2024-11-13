export function fenToYuan(fen: any): number {
    const fenValue = parseFloat(fen);
    if (!isNaN(fenValue)) {
        const yuanValue = fenValue / 100; // 将分转换为元
        return parseFloat(yuanValue.toFixed(2)); // 保留两位小数
    } else {
        return 0;
    }
}

export function yuanToFen(yuan: any): number {
    const yuanValue = parseFloat(yuan);
    if (!isNaN(yuanValue)) {
        const fenValue = yuanValue * 100; // 将元转换为分
        return parseFloat(fenValue.toFixed(2)); // 保留两位小数
    } else {
        return 0;
    }
}

export function formatFenToYuan(fen: any): string {
    const fenValue = parseFloat(fen);
    if (!isNaN(fenValue)) {
        const yuanValue = fenValue / 100; // 将分转换为元
        return `￥${yuanValue.toFixed(2)}`; // 格式化为￥0.00
    } else {
        return '￥0.00';
    }
}

// // 计算百分比
// export const calculatePercentage = (money: any, total: any) => {
//     return total === 0 ? 0 : ((fenToYuan(money) / total) * 100).toFixed(2);
// };

export const calculatePercentage = (money: any, total: any) => {
    if (total === 0) return 0;
    const percentage = (fenToYuan(money) / total) * 100;
    return percentage > 100 ? 100 : percentage.toFixed(2);
};
