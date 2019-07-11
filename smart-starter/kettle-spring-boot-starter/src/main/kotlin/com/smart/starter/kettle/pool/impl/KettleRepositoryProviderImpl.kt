package com.smart.starter.kettle.pool.impl

import com.smart.kettle.common.config.DatabaseMetaProperties
import com.smart.starter.kettle.pool.KettlePooledObjectFactory
import com.smart.starter.kettle.pool.KettleRepositoryProvider
import org.apache.commons.pool2.impl.GenericObjectPool
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.springframework.beans.factory.InitializingBean

/**
 * kettle资源库提供者
 * @author ming
 * 2019/7/11 下午3:23
 */
class KettleRepositoryProviderImpl(private var databaseMetaProperties: DatabaseMetaProperties) : KettleRepositoryProvider, InitializingBean {

    /**
     * 连接池
     */
    private lateinit var objectPool: GenericObjectPool<KettleDatabaseRepository>

    /**
     * 获取资源库连接
     */
    override fun getRepository(): KettleDatabaseRepository {
        return objectPool.borrowObject()
    }

    /**
     * 归还对象
     */
    override fun returnRepository(repository: KettleDatabaseRepository) {
        this.objectPool.returnObject(repository)
    }

    override fun afterPropertiesSet() {
        // 初始化连接工厂
        val factory = KettlePooledObjectFactory(databaseMetaProperties)
        this.objectPool = GenericObjectPool(factory)
    }
}