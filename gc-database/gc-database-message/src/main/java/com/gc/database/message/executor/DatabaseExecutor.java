package com.gc.database.message.executor;

import com.gc.database.message.pojo.bo.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @return 数据库连接
     */
    @Nullable
    Connection getConnection(@NotNull DatabaseConnectionBO databaseConnection) throws SQLException;

    /**
     * 测试数据库连接
     * @param connection 数据库连接
     * @return
     */
    @NotNull
    Boolean testConnection(Connection connection) throws SQLException;

    /**
     * 测试数据库连接
     * @param databaseConnection 数据库连接信息
     * @return
     */
    @NotNull
    Boolean testConnection(@NotNull DatabaseConnectionBO databaseConnection) throws SQLException;

    /**
     * 获取数据库表格
     * @param databaseConnection 数据库连接信息
     * @return
     */
    @NotNull
    List<TableViewBO> listTable(@NotNull DatabaseConnectionBO databaseConnection) throws IllegalAccessException, SQLException, InstantiationException;

    /**
     * 获取数据库视图
     * @param databaseConnection 数据库连接信息
     * @return
     */
    @NotNull
    List<TableViewBO> listView(@NotNull DatabaseConnectionBO databaseConnection) throws IllegalAccessException, SQLException, InstantiationException;

    /**
     * 获取数据库表格
     * @param databaseConnection 数据库连接信息
     * @param types 视图/表格
     * @return
     */
    @NotNull
    List<TableViewBO> listTable(@NotNull DatabaseConnectionBO databaseConnection, String... types) throws SQLException, InstantiationException, IllegalAccessException;

    /**
     * 查询主键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 主键列表
     */
    List<PrimaryKeyBO> listPrimaryKey(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws SQLException, InstantiationException, IllegalAccessException;

    /**
     * 查询外键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 外键列表
     */
    List<ImportKeyBO> listImportedKeys(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws SQLException, InstantiationException, IllegalAccessException;

    /**
     * 查询主键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @param unique 是否查询唯一索引
     * @param approximate
     * @return
     */
    List<IndexBO> listIndex(@NotNull DatabaseConnectionBO databaseConnection, String tableName, Boolean unique, Boolean approximate) throws SQLException, InstantiationException, IllegalAccessException;

    /**
     * 查询唯一索引
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 唯一索引列表
     */
    List<IndexBO> listUniqueIndex(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws IllegalAccessException, SQLException, InstantiationException;

    /**
     * 查询列信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 列信息
     */
    List<ColumnBO> listColumn(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws SQLException, InstantiationException, IllegalAccessException;

}