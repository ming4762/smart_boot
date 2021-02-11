package com.gc.database.message.executor;

import com.gc.database.message.converter.DbJavaTypeConverter;
import org.springframework.stereotype.Component;

/**
 * mysql数据库执行器
 * @author shizhongming
 * 2020/1/19 8:17 下午
 */
public class MysqlDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    private final DbJavaTypeConverter dbJavaTypeConverter;

    public MysqlDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        super(dbJavaTypeConverter);
        this.dbJavaTypeConverter = dbJavaTypeConverter;
    }
}
