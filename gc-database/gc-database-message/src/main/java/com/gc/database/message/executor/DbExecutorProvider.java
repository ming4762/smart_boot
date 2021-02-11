package com.gc.database.message.executor;

import com.gc.database.message.constants.DatabaseTypeConstant;
import com.gc.database.message.pool.model.DbConnectionConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * DB 执行器 Provider
 * @author shizhongming
 * 2021/2/11 9:16 下午
 */
public class DbExecutorProvider {

    private final ApplicationContext applicationContext;

    public DbExecutorProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取数据库执行器
     * @param typeConstant 数据库类型
     * @return 获取数据库执行器
     */
    public DatabaseExecutor getDatabaseExecutor(DatabaseTypeConstant typeConstant) {
        final DatabaseExecutor databaseExecutor = this.applicationContext.getBean(typeConstant.getExecuterClass());
        Assert.notNull(databaseExecutor, "获取数据库执行器失败，spring上下文中未找到bean：" + typeConstant.getExecuterClass());
        return databaseExecutor;
    }

    /**
     * 获取数据库执行器
     * @param connectionConfig 数据库配置
     * @return 获取数据库执行器
     */
    public DatabaseExecutor getDatabaseExecutor(DbConnectionConfig connectionConfig) {
        return this.getDatabaseExecutor(connectionConfig.getType());
    }
}
