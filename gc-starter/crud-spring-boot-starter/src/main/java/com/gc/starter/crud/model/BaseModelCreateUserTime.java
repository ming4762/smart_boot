package com.gc.starter.crud.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author jackson
 * 2020/1/22 2:17 下午
 */
@Getter
@Setter
public abstract class BaseModelCreateUserTime extends BaseModel {

    private static final long serialVersionUID = 5157863351256809974L;
    private Long createUserId;

    private Date createTime;
}