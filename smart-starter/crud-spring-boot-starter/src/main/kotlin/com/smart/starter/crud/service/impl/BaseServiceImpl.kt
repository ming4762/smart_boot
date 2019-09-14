package com.smart.starter.crud.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.smart.common.utils.ReflectUtil
import com.smart.starter.crud.mapper.CloudBaseMapper
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.query.PageQueryParameter
import com.smart.starter.crud.service.BaseService
import com.smart.starter.crud.utils.MybatisUtil
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 基础service实现类
 * @author ming
 * 2019/6/12 上午10:41
 */
open class BaseServiceImpl<K: CloudBaseMapper<T>, T: BaseModel> : ServiceImpl<K, T>(), BaseService<T> {



    companion object {
        private val logger = LoggerFactory.getLogger(BaseServiceImpl :: class.java)
    }

    /**
     * list方法
     */
    override fun list(queryWrapper: QueryWrapper<T>, parameter: PageQueryParameter<T>, paging: Boolean): List<T> {
        if (!paging && parameter.sortName != null) {
            // 如果不是分页情况，参数中存在排序则添加排序
            val sortName = parameter.sortName
            val sortOrder = parameter.sortOrder
            val clazz = MybatisUtil.getModelClassByType(this.getModelType())!!
            val sortList = MybatisUtil.analysisOrder(sortName!!, sortOrder, clazz)
            sortList.let {
                it.forEach { sort ->
                    if (sort.order.toUpperCase() == "ASC") {
                        queryWrapper.orderByAsc(sort.dbName)
                    } else {
                        queryWrapper.orderByDesc(sort.dbName)
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
    override fun delete(model: T): Int {
        // 获取实体主键
        val keyList = this.getkeyList()
        if (keyList.isEmpty()) {
            logger.warn("未找到实体类主键，无法执行删除")
            return 0
        }
        return if (keyList.size == 1) {
            this.baseMapper.deleteById(ReflectUtil.getFieldValue(model, keyList[0]).toString())
        } else {
            val queryWrapper = QueryWrapper<T>()
            keyList.forEach {
                queryWrapper.eq(it, ReflectUtil.getFieldValue(model, it))
            }
            this.baseMapper.delete(queryWrapper)
        }
    }


    /**
     * 批量删除
     */
    override fun batchDelete(modelList: List<T>): Int {
        var num = 0
        val keyList = this.getkeyList()
        if (keyList.isEmpty()) {
            logger.warn("未找到实体类主键，无法执行删除")
            return 0
        }
        if (keyList.size == 1) {
            val key = keyList[0]
            // 获取ID集合
            val keyValueList = modelList.map { ReflectUtil.getFieldValue(it, key).toString() }
            num = this.baseMapper.deleteBatchIds(keyValueList)
        } else {
            modelList.forEach {
                num += this.delete(it)
            }
        }
        return num
    }

    /**
     * 获取单个对象
     */
    override fun get(model: T): T? {
        val keyList = this.getkeyList()
        if (keyList.isEmpty()) return null
        return if (keyList.size == 1) {
            this.getById(ReflectUtil.getFieldValue(model, keyList[0]).toString())
        } else {
            val queryWrapper = QueryWrapper<T>()
            keyList.forEach {
                queryWrapper.eq(it, ReflectUtil.getFieldValue(model, it))
            }
            this.getOne(queryWrapper)
        }
    }

    override fun queryDetail(t: T): T? {
        return get(t)
    }

    /**
     * 通过ID查询
     */
    override fun listByIds(idList: Collection<Serializable>): MutableCollection<T> {
        if (idList.isEmpty()) return mutableListOf()
        val distinctList = idList.distinct()
        if (distinctList.size == 1) {
            val result = this.getById(distinctList.first())
            return if (result == null) mutableListOf() else mutableListOf(result)
        }
        return super.listByIds(distinctList)
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
        return MybatisUtil.getModelKeysByType(this.getModelType())
    }


}