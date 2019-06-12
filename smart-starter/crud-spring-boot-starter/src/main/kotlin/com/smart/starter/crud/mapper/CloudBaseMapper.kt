package com.smart.starter.crud.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.smart.starter.crud.model.BaseModel

/**
 * 基础mapper
 * 在mybatis-plus基础上扩展
 * @author ming
 * 2019/6/12 上午10:37
 */
interface CloudBaseMapper<T: BaseModel>: BaseMapper<T> {
}