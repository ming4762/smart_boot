package com.smart.starter.kettle.pool.impl

import com.smart.kettle.common.config.DatabaseMetaProperties
import com.smart.starter.kettle.pool.KettlePooledObjectFactory
import com.smart.starter.kettle.pool.KettleRepositoryProvider
import org.apache.commons.pool2.impl.GenericObjectPool
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

/**
 * kettle资源库提供者
 * @author ming
 * 2019/7/11 下午3:23
 */
class KettleRepositoryProviderImpl(private var databaseMetaProperties: DatabaseMetaProperties) : KettleRepositoryProvider, InitializingBean {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KettleRepositoryProviderImpl :: class.java)
    }

    /**
     * 连接池
     */
    private lateinit var objectPool: GenericObjectPool<KettleDatabaseRepository>

    /**
     * 获取资源库连接
     */
    override fun getRepository(): KettleDatabaseRepository {
        if (this.objectPool.numWaiters > 0) {
            LOGGER.warn("线程池暂无空闲资源库对象")
        }
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