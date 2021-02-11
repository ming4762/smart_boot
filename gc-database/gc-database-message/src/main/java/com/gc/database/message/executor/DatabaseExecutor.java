package com.gc.database.message.executor;

import com.gc.database.message.constants.TableTypeConstants;
import com.gc.database.message.pojo.bo.ColumnBO;
import com.gc.database.message.pojo.bo.TableViewBO;
import com.gc.database.message.pojo.dbo.*;
import com.gc.database.message.pool.model.DbConnectionConfig;
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
     * 测试数据库连接
     * @param connection 数据库连接
     * @throws SQLException 连接失败错误
     * @return 是否连接成功
     */
    boolean checkConnection(Connection connection) throws SQLException;


    /**
     * 获取数据库表格
     * @param connectionConfig 数据库连接信息
     * @param tableNamePattern 表名
     * @return 表列表
     */
    @NonNull
    default List<TableViewDO> listBaseTable(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern) {
        return this.listBaseTable(connectionConfig, tableNamePattern, TableTypeConstants.TABLE);
    }

    /**
     * 获取数据库视图
     * @param tableNamePattern 表名
     * @param connectionConfig 数据库连接信息
     * @return 表列表
     */
    @NonNull
    default List<TableViewDO> listBaseView(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern) {
        return this.listBaseTable(connectionConfig, tableNamePattern, TableTypeConstants.VIEW);
    }

    /**
     * 获取数据库表格
     * @param connectionConfig 数据库连接信息
     * @param tableNamePattern 表名
     * @param types 视图/表格
     * @return 表列表
     */
    @NonNull
    List<TableViewDO> listBaseTable(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern, TableTypeConstants... types);

    /**
     * 获取数据库表格
     * @param connectionConfig 数据库连接信息
     * @param tableNamePattern 表名
     * @param types 视图/表格
     * @return 表列表
     */
    @NonNull
    default List<TableViewBO> listTable(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern, TableTypeConstants... types) {
        final List<TableViewDO> baseTableList = this.listBaseTable(connectionConfig, tableNamePattern, types);
        final List<TableViewBO> tableList = TableViewBO.batchCreateFromDo(baseTableList);
        tableList.forEach(table -> {
            final List<ColumnBO> columnList = this.listColumn(connectionConfig, table.getTableName());
            table.setColumnList(columnList);
        });
        return tableList;
    }

    /**
     * 获取数据库表格
     * @param connectionConfig 数据库连接信息
     * @param tableNamePattern 表名
     * @return 表列表
     */
    default List<TableViewBO> listTable(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern) {
        return this.listTable(connectionConfig, tableNamePattern, TableTypeConstants.TABLE);
    }

    /**
     * 获取数据库视图
     * @param tableNamePattern 表名
     * @param connectionConfig 数据库连接信息
     * @return 表列表
     */
    default List<TableViewBO> listView(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern) {
        return this.listTable(connectionConfig, tableNamePattern, TableTypeConstants.VIEW);
    }


    /**
     * 查询主键信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 主键列表
     */
    List<PrimaryKeyDO> listPrimaryKey(@NonNull DbConnectionConfig connectionConfig, String tableName);

    /**
     * 查询外键信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 外键列表
     */
    List<ImportKeyDO> listImportedKeys(@NonNull DbConnectionConfig connectionConfig, String tableName);

    /**
     * 查询主键信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @param unique 是否查询唯一索引
     * @param approximate
     * @return
     */
    List<IndexDO> listIndex(@NonNull DbConnectionConfig connectionConfig, String tableName, Boolean unique, Boolean approximate);

    /**
     * 查询唯一索引
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 唯一索引列表
     */
    List<IndexDO> listUniqueIndex(@NonNull DbConnectionConfig connectionConfig, String tableName);

    /**
     * 查询列信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 列信息
     */
    List<ColumnBO> listColumn(@NonNull DbConnectionConfig connectionConfig, @NonNull String tableName);

    /**
     * 查询列基本信息
     * @param connectionConfig 数据库连接
     * @param tableName 表名
     * @return 列基本信息
     */
    List<ColumnDO> listBaseColumn(@NonNull DbConnectionConfig connectionConfig, @NonNull String tableName);

}
