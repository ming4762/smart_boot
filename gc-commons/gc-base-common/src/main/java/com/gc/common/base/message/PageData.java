package com.gc.common.base.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据
 * @author shizhongming
 * 2020/1/12 8:30 下午
 */
@AllArgsConstructor
@Getter
@Setter
public final class PageData<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 3546130315339330080L;

    private List<T> rows;

    private Long total = 0L;
}
