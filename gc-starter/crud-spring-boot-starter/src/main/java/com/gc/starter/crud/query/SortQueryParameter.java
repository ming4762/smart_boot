package com.gc.starter.crud.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 排序参数类
 * @author jackson
 * 2020/3/14 6:52 下午
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SortQueryParameter<K, V> extends QueryParameter<K, V> {

    private static final long serialVersionUID = -8278647627006548908L;

    private String sortName;

    private String sortOrder;
}
