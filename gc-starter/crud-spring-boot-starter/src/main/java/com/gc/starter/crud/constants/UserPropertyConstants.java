package com.gc.starter.crud.constants;

import lombok.Getter;

/**
 * 用户属性
 * @author shizhongming
 * 2020/3/30 7:00 下午
 */
public enum UserPropertyConstants {

    /**
     * 创建人员ID
     */
    CREATE_USER_ID("createUserId"),
    // 创建时间
    CREATE_TIME("createTime"),
    // 更新人员ID
    UPDATE_USER_ID("updateUserId"),
    // 更新时间
    UPDATE_TIME("updateTime"),
    CREATE_USER("createUser"),
    UPDATE_USER("updateUser");


    @Getter
    private String name;

    UserPropertyConstants(String name) {
        this.name = name;
    }
}
