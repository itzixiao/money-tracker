package com.jcwl.admin.book.enums;

import com.jcwl.common.exception.BizPreconditions;

public enum QueryPeriodEnum {
    CURRENT_DAY(0, "当日"),
    CURRENT_MONTH(1, "本月"),
    CURRENT_YEAR(2, "本年");

    private Integer value;
    private String description;

    QueryPeriodEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static QueryPeriodEnum fromValue(Integer value) {
        for (QueryPeriodEnum period : values()) {
            if (period.getValue().equals(value)) {
                return period;
            }
        }
        BizPreconditions.thrException("Invalid value for QueryPeriodEnum: " + value);
        return null;
    }

}
