package com.gc.database.message;

import com.gc.common.base.imports.EnableSpringContext;
import com.gc.database.message.converter.*;
import com.gc.database.message.executor.DbExecutorProvider;
import com.gc.database.message.executor.MysqlDatabaseExecutor;
import com.gc.database.message.executor.OracleDatabaseExecutor;
import com.gc.database.message.executor.SqlServerDatabaseExecutor;
import com.gc.database.message.pool.DbConnectionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/29 8:45 下午
 */
@Configuration
@EnableSpringContext
public class DatabaseMessageAutoConfiguration {

    /**
     * 创建默认的 converterProvider
     * @return converterProvider
     */
    @Bean
    @ConditionalOnMissingBean(ConverterProvider.class)
    public ConverterProvider converterProvider() {
        return new DefaultConverterProvider();
    }

    @Bean
    public ConverterInitializer converterInitializer(ConverterProvider converterProvider) {
        return new ConverterInitializer(converterProvider);
    }

    /**
     * 创建默认的DbJavaTypeConverter
     * @return DbJavaTypeConverter
     */
    @Bean
    @ConditionalOnMissingBean(DbJavaTypeConverter.class)
    public DbJavaTypeConverter dbJavaTypeConverter() {
        return new DefaultDbJavaTypeConverter();
    }

    @Bean
    public MysqlDatabaseExecutor mysqlDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        return new MysqlDatabaseExecutor(dbJavaTypeConverter);
    }

    @Bean
    public OracleDatabaseExecutor oracleDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        return new OracleDatabaseExecutor(dbJavaTypeConverter);
    }

    @Bean
    public SqlServerDatabaseExecutor sqlServerDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        return new SqlServerDatabaseExecutor(dbJavaTypeConverter);
    }

    /**
     * 创建 DbExecutorProvider
     * @param applicationContext ApplicationContext
     * @return DbExecutorProvider
     */
    @Bean
    public DbExecutorProvider dbExecutorProvider(ApplicationContext applicationContext) {
        return new DbExecutorProvider(applicationContext);
    }

    /**
     * 创建数据库连接提供器
     * @return DbConnectionProvider
     */
    @Bean
    public DbConnectionProvider dbConnectionProvider() {
        return new DbConnectionProvider();
    }
}
