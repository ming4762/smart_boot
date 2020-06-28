package com.gc.system.constants;

import com.gc.validate.common.enums.IEnum;
import lombok.Getter;

/**
 * @author shizhongming
 * 2020/6/2 4:51 下午
 */
@Getter
public enum UserTypeConstants implements IEnum {

    /**
     * 系统用户
     */
    SYSTEM_USER("10", "系统用户"),
    BUSINESS_USER("20", "业务用户");

    private final String value;

    private final String name;

    UserTypeConstants(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
