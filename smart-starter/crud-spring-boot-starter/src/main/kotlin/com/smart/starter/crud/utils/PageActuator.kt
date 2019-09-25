package com.smart.starter.crud.utils

import com.github.pagehelper.Page
import com.smart.starter.crud.model.BaseModel

/**
 * 分页执行器
 * @author ming
 * 2019/9/19 上午9:55
 */
object PageActuator {
    private val PAGE_CONTAINER = ThreadLocal<MutableMap<String, Page<out BaseModel>>>()

    private const val LIST_PAGE = "LIST_PAGE"

    /**
     * 获取分页page
     */
    fun <T: BaseModel> getListPage(): Page<T>? {
        return PAGE_CONTAINER.get()?.get(LIST_PAGE) as Page<T>?
    }

    /**
     * 设置分页
     */
    fun setListPage(page: Page<out BaseModel>) {
        val map = PAGE_CONTAINER.get()
        if (map == null) {
            PAGE_CONTAINER.set(mutableMapOf())
        }
        PAGE_CONTAINER.get()[LIST_PAGE] = page
    }

    /**
     * 清除listpage
     */
    fun clearListPage() {
        PAGE_CONTAINER.get()?.remove(LIST_PAGE)
    }
}