package com.jcwl.admin.book.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 成员类型
 *
 * @author jcwl
 * @date 2024-10-14
 */
public enum AuthType {
    /**
     * 成员
     */
    MEMBER(0),
    /**
     * 管理员
     */
    MANAGER(1),
    /**
     * 创建人
     */
    CREATOR(2);

    private final Integer value;

    private AuthType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    private static final Map<String, AuthType> typeMap;

    static {
        typeMap = new HashMap<>();
        typeMap.put("成员", MEMBER);
        typeMap.put("管理员", MANAGER);
        typeMap.put("创建人", CREATOR);
    }

    public static AuthType getByName(String name) {
        return typeMap.getOrDefault(name, CREATOR);
    }

}