package com.gc.kettle.starter.pool.impl;

import com.gc.kettle.actuator.properties.DatabaseMetaProperties;
import com.gc.kettle.starter.pool.KettlePooledObjectFactory;
import com.gc.kettle.starter.pool.KettleRepositoryProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

/**
 * @author shizhongming
 * 2020/7/16 8:44 下午
 */
@Slf4j
public class DefaultKettleRepositoryProviderImpl implements KettleRepositoryProvider, InitializingBean {

    /**
     * 资源库连接池
     */
    private GenericObjectPool<KettleDatabaseRepository> objectPool;

    /**
     * 资源库连接信息
     */
    private final DatabaseMetaProperties databaseMetaProperties;

    public DefaultKettleRepositoryProviderImpl(DatabaseMetaProperties databaseMetaProperties) {
        this.databaseMetaProperties = databaseMetaProperties;
    }

    /**
     * 获取资源库连接
     */
    @Override
    @Nullable
    public KettleDatabaseRepository getRepository() {
        if (this.objectPool.getNumWaiters() > 0) {
            log.warn("线程池暂无空闲资源库对象");
        }
        try {
            return objectPool.borrowObject();
        } catch (Exception e) {
            log.warn("从连接池中获取资源库发生错误", e);
            return null;
        }
    }

    /**
     * 归还资源库连接池
     * @param repository kettle资源库
     */
    @Override
    public void returnRepository(KettleDatabaseRepository repository) {
        this.objectPool.returnObject(repository);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建资源库连接池
        final KettlePooledObjectFactory factory = new KettlePooledObjectFactory(this.databaseMetaProperties);
        this.objectPool = new GenericObjectPool<>(factory);
    }
}
