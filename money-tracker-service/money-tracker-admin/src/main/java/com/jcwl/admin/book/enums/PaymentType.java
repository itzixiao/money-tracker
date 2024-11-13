package com.jcwl.admin.book.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付类型
 *
 * @author jcwl
 * @date 2024-10-14
 */
public enum PaymentType {

    /**
     * 收入
     */
    INCOME(0),
    /**
     * 支出
     */
    EXPENDITURE(1),
    /**
     * 不计收支
     */
    EXCEPT(2);

    private final Integer value;

    private PaymentType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    private static final Map<String, PaymentType> typeMap;

    static {
        typeMap = new HashMap<>();
        typeMap.put("收入", INCOME);
        typeMap.put("支出", EXPENDITURE);
        typeMap.put("不计收支", EXCEPT);
    }

    public static PaymentType getByName(String name) {
        return typeMap.getOrDefault(name, EXCEPT);
    }

}