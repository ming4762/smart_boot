package com.gc.kettle.starter.pool;

import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

/**
 * @author shizhongming
 * 2020/7/16 8:18 下午
 */
public interface KettleRepositoryProvider {

    /**
     * 获取资源库
     * @return kettle资源库
     */
    KettleDatabaseRepository getRepository();

    /**
     * 归还资源库连接池
     * @param repository kettle资源库
     */
    void returnRepository(KettleDatabaseRepository repository);
}
