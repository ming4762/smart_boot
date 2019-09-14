package com.smart.starter.crud.model

import java.io.Serializable

/**
 * 分页数据
 * @author ming
 * 2019/9/13 下午7:24
 */
class PageData<T: BaseModel>(val rows: List<T>?, val total: Long = 0) : Serializable {
}