package com.auc.common.enums;

/**
 * 系统角色
 */
public enum RoleEnum {

    /**
     * 普通用户
     */
    USER("普通用户"),
    /**
     * 管理员
     */
    ADMIN("管理员"),
    /**
     * 超级
     */
    SUPER("超级");

    private String meaning;

    public String getMeaning() {
        return meaning;
    }

    RoleEnum() {
    }

    RoleEnum(String meaning) {
        this.meaning = meaning;
    }
}
