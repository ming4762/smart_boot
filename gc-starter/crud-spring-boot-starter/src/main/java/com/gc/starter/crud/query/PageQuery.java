package com.gc.starter.crud.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 分页参数基类
 * @author shizhongming
 * 2020/6/24 7:40 下午
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PageQuery implements Serializable {

    private static final long serialVersionUID = -149737684094213921L;
    private Integer limit;

    private Integer offset = 0;

    private String keyword;
}
