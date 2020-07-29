package com.gc.database.message.executor;

import com.gc.database.message.constants.TableTypeConstants;
import com.gc.database.message.pojo.bo.DatabaseConnectionBO;
import com.gc.database.message.pojo.dbo.ColumnDO;
import com.gc.database.message.pojo.dbo.TableViewDO;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2020/1/19 8:18 下午
 */
@Component
public class SqlServerDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    /**
     * 查询表格备注SQL
     */
    private static final String TABLE_COMMENTS_SQL  = "SELECT a.NAME TABLE_NAME,CAST (isnull(e.[value], '') AS nvarchar (100)) AS REMARK FROM sys.tables a INNER JOIN sys.objects c ON a.object_id = c.object_id LEFT JOIN sys.extended_properties e ON e.major_id = c.object_id AND e.minor_id = 0 AND e.class = 1 where c.name in %in";

    /**
     * 查询列备注SQL
     */
    private static final String COLUMN_COMMENTS_SQL = "select a.NAME COLUMN_NAME, c.name TABLE_NAME,cast(isnull(e.[value],'') as nvarchar(100)) as REMARK from sys.columns a inner join sys.objects c on a.object_id=c.object_id and c.type='u' left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name in %in";


    @Override
    public String getUrl(@NonNull DatabaseConnectionBO databaseConnection) {
        return String.format("%s;database=%s", databaseConnection.getUrl(), databaseConnection.getDatabaseName());
    }

    /**
     * 获取表格信息
     * @param databaseConnection 数据库连接信息
     * @param tableNamePattern 表名
     * @param types 视图/表格
     * @return 表格列表
     */
    @Override
    @NonNull
    public List<TableViewDO> listBaseTable(@NonNull DatabaseConnectionBO databaseConnection, String tableNamePattern, TableTypeConstants... types) {
        List<TableViewDO> tableList = super.listBaseTable(databaseConnection, tableNamePattern, types);
        this.queryTableRemark(databaseConnection, tableList, TABLE_COMMENTS_SQL);
        return tableList;
    }

    /**
     * 查询列信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 列信息
     */
    @NonNull
    @Override
    public List<ColumnDO> listBaseColumn(@NonNull DatabaseConnectionBO databaseConnection, @NonNull String tableName) {
        List<ColumnDO> columnList = super.listBaseColumn(databaseConnection, tableName);
        this.queryColumnRemark(databaseConnection, columnList, COLUMN_COMMENTS_SQL);
        return columnList;
    }
}
