package com.gc.starter.crud.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页查询类
 * @author jackson
 * 2020/3/14 6:55 下午
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PageQueryParameter<K, V> extends SortQueryParameter<K, V> {
    private static final long serialVersionUID = -2738378597906345854L;

    private Integer limit;

    private Integer offset = 0;

    private Integer page;

    private String keyword;
}
