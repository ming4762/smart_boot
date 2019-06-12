package com.smart.starter.crud.service.impl

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.smart.common.utils.ReflectUtil
import com.smart.starter.crud.mapper.CloudBaseMapper
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.service.BaseService
import com.smart.starter.crud.utils.MybatisUtil
import org.slf4j.LoggerFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.stream.Collectors

/**
 * 基础service实现类
 * @author ming
 * 2019/6/12 上午10:41
 */
open class BaseServiceImpl<K: CloudBaseMapper<T>, T: BaseModel> : ServiceImpl<K, T>(), BaseService<T> {



    companion object {
        private val logger = LoggerFactory.getLogger(BaseServiceImpl :: class.java)
        private const val SORT_NAME = "sortName"
        private const val SORT_ORDER = "sortOrder"
    }

    /**
     * list方法
     */
    override fun list(queryWrapper: Wrapper<T>, parameters: Map<String, Any?>, paging: Boolean): List<T> {
        if (!paging) {
            // 如果不是分页情况，参数中存在排序则添加排序
            val sortName = parameters[SORT_NAME] as String?
            val sortOrder = parameters[SORT_ORDER] as String?
            val clazz = MybatisUtil.getModelClass(this.getModelType())
            val sortList = MybatisUtil.analysisOrder(sortName, sortOrder, clazz)
            sortList?.let {
                queryWrapper as QueryWrapper<T>
                it.forEach { sort ->
                    if (sort.dbName != null) {
                        if (sort.order.toUpperCase() == "ASC") {
                            queryWrapper.orderByAsc(sort.dbName)
                        } else {
                            queryWrapper.orderByDesc(sort.dbName)
                        }
                    }
                }
            }
        }
        return super<ServiceImpl>.list(queryWrapper)
    }

    /**
     * 删除操作
     * TODO: 无法找到主键使用对象匹配删除
     */
    override fun delete(t: T): Int {
        // 获取实体主键
        val keyList = this.getkeyList()
        if (keyList.isEmpty()) {
            logger.warn("未找到实体类主键，无法执行删除")
            return 0
        }
        if (keyList.size == 1) {
            return this.baseMapper.deleteById(ReflectUtil.getFieldValue(t, keyList[0]).toString())
        } else {
            val queryWrapper = QueryWrapper<T>()
            keyList.forEach {
                queryWrapper.eq(it, ReflectUtil.getFieldValue(t, it))
            }
            return this.baseMapper.delete(queryWrapper)
        }
    }


    /**
     * 批量删除
     */
    override fun batchDelete(tList: List<T>): Int {
        var num = 0
        val keyList = this.getkeyList()
        if (keyList.isEmpty()) {
            logger.warn("未找到实体类主键，无法执行删除")
            return 0
        }
        if (keyList.size == 1) {
            val key = keyList[0]
            // 获取ID集合
            val keyValueList = tList.stream().map {
                ReflectUtil.getFieldValue(it, key).toString()
            }.collect(Collectors.toList())
            num = this.baseMapper.deleteBatchIds(keyValueList)
        } else {
            tList.forEach {
                num += this.delete(it)
            }
        }
        return num
    }

    /**
     * 获取单个对象
     */
    override fun get(t: T): T? {
        // 获取实体类主键
        // 获取实体类类型
        val clazz = MybatisUtil.getModelClass(this.getModelType())
        if (clazz != null) {
            val keyList = MybatisUtil.getModelKeyField(clazz)
            if (keyList.size == 1) {
                return this.getById(ReflectUtil.getFieldValue(t, keyList[0]).toString())
            } else {
                val queryWrapper = QueryWrapper<T>()
                keyList.forEach {
                    queryWrapper.eq(it, ReflectUtil.getFieldValue(t, it))
                }
                return this.getOne(queryWrapper)
            }
        }
        return null
    }


    /**
     * 获取实体类类型
     */
    private fun getModelType(): Type {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    }

    /**
     * 获取实体类的ID
     */
    private fun getkeyList(): List<String> {
        val keyList = MybatisUtil.getkeyList(this.getModelType())
        return if (keyList == null) arrayListOf() else keyList

    }

}