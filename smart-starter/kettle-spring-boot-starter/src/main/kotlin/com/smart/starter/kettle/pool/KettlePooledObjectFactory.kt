package com.smart.starter.kettle.pool

import com.smart.kettle.common.config.DatabaseMetaProperties
import com.smart.kettle.meta.CustomDatabaseMeta
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.PooledObjectFactory
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.pentaho.di.core.KettleEnvironment
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta

/**
 * 连接池对象工厂
 * @author ming
 * 2019/7/11 下午2:21
 */
class KettlePooledObjectFactory : PooledObjectFactory<KettleDatabaseRepository> {

    private lateinit var properties: DatabaseMetaProperties

    constructor()

    constructor(properties: DatabaseMetaProperties) {
        this.properties = properties
        KettleEnvironment.init()
    }

    /**
     * 创建资源库对象
     */
    override fun makeObject(): PooledObject<KettleDatabaseRepository> {
        // 创建资源库
        val repository = KettleDatabaseRepository()
        val databaseMeta = CustomDatabaseMeta(this.properties.name, this.properties.type,
                this.properties.access, this.properties.host, this.properties.db,
                this.properties.port, this.properties.dbUser, this.properties.dbPassword)
        val kettleDatabaseRepositoryMeta = KettleDatabaseRepositoryMeta("kettle","kettle","Transformation description",databaseMeta)
        repository.init(kettleDatabaseRepositoryMeta)
        //连接资源库
        repository.connect(this.properties.resUser,this.properties.resPassword)
        return DefaultPooledObject(repository)
    }

    override fun validateObject(pooledObject: PooledObject<KettleDatabaseRepository>): Boolean {
        return pooledObject.`object`.isConnected
    }

    override fun activateObject(pooledObject: PooledObject<KettleDatabaseRepository>) {
    }

    /**
     * 销毁对象
     */
    override fun destroyObject(pooledObject: PooledObject<KettleDatabaseRepository>) {
        pooledObject.`object`?.disconnect()
    }

    override fun passivateObject(pooledObject: PooledObject<KettleDatabaseRepository>) {
    }
}