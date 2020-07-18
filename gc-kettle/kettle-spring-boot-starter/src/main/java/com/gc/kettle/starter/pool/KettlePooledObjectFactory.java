package com.gc.kettle.starter.pool;

import com.gc.kettle.actuator.meta.CustomDatabaseMeta;
import com.gc.kettle.actuator.properties.DatabaseMetaProperties;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

import java.util.Objects;
import java.util.Optional;

/**
 * @author shizhongming
 * 2020/7/16 8:20 下午
 */
public class KettlePooledObjectFactory implements PooledObjectFactory<KettleDatabaseRepository> {

    private final DatabaseMetaProperties properties;

    public KettlePooledObjectFactory(DatabaseMetaProperties databaseMetaProperties) {
        this.properties = databaseMetaProperties;
    }

    /**
     * 创建资源库对象
     */
    @Override
    public PooledObject<KettleDatabaseRepository> makeObject() throws Exception {
        final KettleDatabaseRepository repository = new KettleDatabaseRepository();
        // 创建连接信息
        final CustomDatabaseMeta databaseMeta = new CustomDatabaseMeta(this.properties.getName(), this.properties.getType(),
                this.properties.getAccess(), this.properties.getHost(), this.properties.getDb(),
                this.properties.getPort(), this.properties.getDbUser(), this.properties.getDbPassword());
        final KettleDatabaseRepositoryMeta kettleDatabaseRepositoryMeta = new KettleDatabaseRepositoryMeta("kettle","kettle","Transformation description",databaseMeta);
        // 初始化资源库
        repository.init(kettleDatabaseRepositoryMeta);
        //连接资源库
        repository.connect(this.properties.getResUser(),this.properties.getResPassword());
        return new DefaultPooledObject<>(repository);
    }

    /**
     * 销毁对象
     */
    @Override
    public void destroyObject(PooledObject<KettleDatabaseRepository> pooledObject) throws Exception {
        Optional.ofNullable(pooledObject.getObject())
                .ifPresent(KettleDatabaseRepository :: disconnect);
    }

    @Override
    public boolean validateObject(PooledObject<KettleDatabaseRepository> pooledObject) {
        final KettleDatabaseRepository repository = pooledObject.getObject();
        return Objects.nonNull(repository) && repository.isConnected();
    }

    @Override
    public void activateObject(PooledObject<KettleDatabaseRepository> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<KettleDatabaseRepository> pooledObject) throws Exception {

    }
}
