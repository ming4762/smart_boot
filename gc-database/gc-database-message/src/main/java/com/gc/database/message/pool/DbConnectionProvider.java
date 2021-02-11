package com.gc.database.message.pool;

import com.gc.database.message.pool.model.DbConnectionConfig;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.util.Map;

/**
 * 数据库连接Provider
 * @author shizhongming
 * 2021/2/11 5:57 下午
 */
@Slf4j
public class DbConnectionProvider implements InitializingBean, DisposableBean {

    /**
     * 存储连接池
     */
    private static final Map<DbConnectionConfig, GenericKeyedObjectPool<DbConnectionConfig, Connection>> POOL_MAP = Maps.newConcurrentMap();


    /**
     * 获取数据库连接
     * @param config 数据库配置信息
     * @return 数据库连接
     */
    public Connection getConnection(DbConnectionConfig config) {
        try {
            // 获取连接池
            if (!POOL_MAP.containsKey(config)) {
                this.createPool(config);
            }
            final GenericKeyedObjectPool<DbConnectionConfig, Connection> pool = POOL_MAP.get(config);
            return pool.borrowObject(config);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 归还连接
     * @param config DbConnectionConfig
     * @param connection Connection
     */
    public void returnConnection(DbConnectionConfig config, Connection connection) {
        final GenericKeyedObjectPool<DbConnectionConfig, Connection> pool = POOL_MAP.get(config);
        Assert.notNull(pool, "系统发生未知错误，未找到连接池");
        pool.returnObject(config, connection);
    }

    /**
     * 创建连接池
     * @param config DbConnectionConfig
     */
    private void createPool(DbConnectionConfig config) {
        log.info("============ 初始化数据库连接池，数据库连接：【{}】============", config.getUrl());
        final DbConnectionFactory dbConnectionFactory = new DbConnectionFactory();
        final GenericKeyedObjectPool<DbConnectionConfig, Connection> pool = new GenericKeyedObjectPool<>(dbConnectionFactory);
        POOL_MAP.put(config, pool);
    }

    @Override
    public void destroy() {
        // 关闭连接池
        POOL_MAP.values().forEach(GenericKeyedObjectPool::close);
        log.info("============= 数据库连接池销毁完毕 ==============");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("============= 数据库连接池创建完毕 ==============");
    }
}
