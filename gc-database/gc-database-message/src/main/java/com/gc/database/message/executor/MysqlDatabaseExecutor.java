package com.gc.database.message.executor;

import com.gc.database.message.pojo.bo.DatabaseConnectionBO;
import org.springframework.lang.NonNull;

/**
 * mysql数据库执行器
 * @author shizhongming
 * 2020/1/19 8:17 下午
 */
public class MysqlDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    /**
     * 获取连接URL
     * @param databaseConnection 连接信息
     * @return 连接URL
     */
    @Override
    public String getUrl(@NonNull DatabaseConnectionBO databaseConnection) {
        return String.format("%s/%s", databaseConnection.getUrl(), databaseConnection.getDatabaseName());
    }
}
