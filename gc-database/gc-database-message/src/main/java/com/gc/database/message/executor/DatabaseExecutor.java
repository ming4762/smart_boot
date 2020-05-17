package com.gc.database.message.executor;

import com.gc.database.message.pojo.bo.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库执行器
 * @author shizhongming
 */
public interface DatabaseExecutor {

    /**
     * 获取数据库连接
     * @param databaseConnection 数据库连接信息
     * @throws SQLException 连接失败错误
     * @return 数据库连接
     */
    @Nullable
    Connection getConnection(@NonNull DatabaseConnectionBO databaseConnection) throws SQLException;

    /**
     * 测试数据库连接
     * @param connection 数据库连接
     * @throws SQLException 连接失败错误
     * @return 是否连接成功
     */
    boolean testConnection(Connection connection) throws SQLException;

    /**
     * 测试数据库连接
     * @param databaseConnection 数据库连接信息
     * @throws SQLException 连接失败错误
     * @return 是否连接成功
     */
    @NonNull
    Boolean testConnection(@NonNull DatabaseConnectionBO databaseConnection) throws SQLException;

    /**
     * 获取数据库表格
     * @param databaseConnection 数据库连接信息
     * @return 表列表
     */
    @NonNull
    List<TableViewBO> listTable(@NonNull DatabaseConnectionBO databaseConnection);

    /**
     * 获取数据库实体
     * @param databaseConnection 数据库连接信息
     * @return 表列表
     */
    @NonNull
    List<TableViewBO> listView(@NonNull DatabaseConnectionBO databaseConnection);

    /**
     * 获取数据库表格
     * @param databaseConnection 数据库连接信息
     * @param types 视图/表格
     * @return 表列表
     */
    @NonNull
    List<TableViewBO> listTable(@NonNull DatabaseConnectionBO databaseConnection, String... types);

    /**
     * 查询主键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 主键列表
     */
    List<PrimaryKeyBO> listPrimaryKey(@NonNull DatabaseConnectionBO databaseConnection, String tableName);

    /**
     * 查询外键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 外键列表
     */
    List<ImportKeyBO> listImportedKeys(@NonNull DatabaseConnectionBO databaseConnection, String tableName);

    /**
     * 查询主键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @param unique 是否查询唯一索引
     * @param approximate
     * @return
     */
    List<IndexBO> listIndex(@NonNull DatabaseConnectionBO databaseConnection, String tableName, Boolean unique, Boolean approximate);

    /**
     * 查询唯一索引
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 唯一索引列表
     */
    List<IndexBO> listUniqueIndex(@NonNull DatabaseConnectionBO databaseConnection, String tableName);

    /**
     * 查询列信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 列信息
     */
    List<ColumnBO> listColumn(@NonNull DatabaseConnectionBO databaseConnection, String tableName);

}