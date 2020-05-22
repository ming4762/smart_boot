package com.gc.starter.crud.model;

import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 * 排序字段
 * @author shizhongming
 * 2020/1/12 4:13 下午
 */
@Getter
public final class Sort {
    @NonNull
    private final String name;

    @NonNull
    private final String order;

    /**
     * 对应的数据库字段
     */
    @NonNull
    private final String dbName;

    public Sort(@NonNull String name, @NonNull String order, @NonNull String dbName) {
        this.name = name;
        this.order = order;
        this.dbName = dbName;
    }
}
