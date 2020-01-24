package com.gc.starter.crud.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 带有创建/修改用户信息的基础类
 * @author jackson
 * 2020/1/21 10:04 下午
 */
@Getter
@Setter
public abstract class BaseModelUserTime extends BaseModelCreateUserTime {
    private static final long serialVersionUID = 2257266050574705690L;

    private Long updateUserId;

    private Date updateTime;
}
