package com.gc.starter.crud.mapper;


import com.gc.starter.crud.model.BaseModel;

/**
 * 基础服务层
 * @param <T>
 */
public interface BaseMapper<T extends BaseModel> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
}
