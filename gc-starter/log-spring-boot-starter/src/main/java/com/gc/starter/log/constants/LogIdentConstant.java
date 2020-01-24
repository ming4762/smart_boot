package com.gc.starter.log.constants;

import lombok.Getter;

/**
 * @author jackson
 * 2020/1/22 2:34 下午
 */
@Getter
public enum LogIdentConstant {
    /**
     * 系统自动
     */
    AUTO("10");

    private String value;

    LogIdentConstant(String value) {
        this.value = value;
    }
}
