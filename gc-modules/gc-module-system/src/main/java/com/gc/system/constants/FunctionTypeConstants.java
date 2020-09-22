package com.gc.system.constants;

import com.gc.validate.common.enums.IEnum;

/**
 * @author shizhongming
 * 2020/9/22 8:56 下午
 */
public enum FunctionTypeConstants implements IEnum {
    /**
     * 目录、菜单、功能
     */
    CATALOG("10"),
    MENU("20"),
    FUNCTION("30")
    ;

    private final String value;

    FunctionTypeConstants(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
