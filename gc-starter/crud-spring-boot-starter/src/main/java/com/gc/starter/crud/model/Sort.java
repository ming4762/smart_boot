package com.gc.starter.crud.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 排序字段
 * @author shizhongming
 * 2020/1/12 4:13 下午
 */
@Getter
public final class Sort {
    @NotNull
    private String name;

    @NotNull
    private String order;

    /**
     * 对应的数据库字段
     */
    @NotNull
    private String dbName;

    public Sort(@NotNull String name, @NotNull String order, @NotNull String dbName) {
        this.name = name;
        this.order = order;
        this.dbName = dbName;
    }
}
