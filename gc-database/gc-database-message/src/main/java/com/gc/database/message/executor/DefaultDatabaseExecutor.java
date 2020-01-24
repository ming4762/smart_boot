package com.gc.database.message.executor;

import com.gc.database.message.annotation.DatabaseField;
import com.gc.database.message.constants.ExceptionConstant;
import com.gc.database.message.constants.TypeMappingConstant;
import com.gc.database.message.exception.SmartDatabaseException;
import com.gc.database.message.pojo.bo.*;
import com.gc.database.message.utils.CacheUtils;
import com.gc.database.message.utils.DatabaseUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/18 9:02 下午
 */
public abstract class DefaultDatabaseExecutor implements DatabaseExecutor {

    static {
        DefaultDatabaseExecutor.mappingDatabaseFieldToCache();
        DefaultDatabaseExecutor.initTypeMappingCache();
    }

    /**
     * 获取数据库连接
     * @param databaseConnection 数据库连接信息
     * @return 数据库连接
     */
    @Override
    public @Nullable Connection getConnection(@NotNull DatabaseConnectionBO databaseConnection) throws SQLException {
        final String key = databaseConnection.createConnectionKey();
        Connection connection = CacheUtils.CONNECTION_CACHE.get(key);
        if (connection == null || connection.isClosed()) {
            connection = databaseConnection.connection();
            CacheUtils.CONNECTION_CACHE.put(key, connection);
        }
        return connection;
    }

    /**
     * 测试数据库连接
     * @param connection
     * @return
     * @throws SQLException
     */
    @Override
    public @NotNull Boolean testConnection(Connection connection) throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * 测试数据库连接
     * @param databaseConnection
     * @return
     * @throws SQLException
     */
    @Override
    public @NotNull Boolean testConnection(@NotNull DatabaseConnectionBO databaseConnection) throws SQLException {
        final String key = databaseConnection.createConnectionKey();
        if (StringUtils.isEmpty(key)) {
            return Boolean.FALSE;
        }
        final Connection connection = this.getConnection(databaseConnection);
        if (connection == null) {
            return Boolean.FALSE;
        }
        boolean result = this.testConnection(connection);
        if (!result) {
            CacheUtils.CONNECTION_CACHE.remove(key);
        }
        return result;
    }

    @Override
    public @NotNull List<TableViewBO> listTable(@NotNull DatabaseConnectionBO databaseConnection) throws IllegalAccessException, SQLException, InstantiationException {
        return this.listTable(databaseConnection, "TABLE");
    }

    @Override
    public @NotNull List<TableViewBO> listView(@NotNull DatabaseConnectionBO databaseConnection) throws IllegalAccessException, SQLException, InstantiationException {
        return this.listTable(databaseConnection, "VIEW");
    }

    /**
     * 获取表格信息
     * @param types 类型
     */
    @Override
    public @NotNull List<TableViewBO> listTable(@NotNull DatabaseConnectionBO databaseConnection, String... types) throws SQLException, InstantiationException, IllegalAccessException {
        final Connection connection = this.getConnection(databaseConnection);
        if (connection == null) {
            return Lists.newArrayList();
        }
        ResultSet resultSet = connection.getMetaData().getTables(connection.getCatalog(), connection.getSchema(), null, types);
        Map<String, Field> mapping = CacheUtils.DATABASE_FIELD_MAPPING.get(TableViewBO.class);
        if (mapping == null) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, TableViewBO.class.getName());
        }
        return DatabaseUtils.resultSetToModel(resultSet, TableViewBO.class, mapping);
    }

    /**
     * 查询主键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 标明
     * @return 主键列表
     */
    @Override
    public List<PrimaryKeyBO> listPrimaryKey(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = this.getConnection(databaseConnection);
        if (!this.testConnection(connection)) {
            return Lists.newArrayList();
        }
        ResultSet resultSet = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);
        Map<String, Field> mapping = CacheUtils.DATABASE_FIELD_MAPPING.get(PrimaryKeyBO.class);
        if (mapping == null) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, PrimaryKeyBO.class.getName());
        }
        return DatabaseUtils.resultSetToModel(resultSet, PrimaryKeyBO.class, mapping);
    }

    /**
     * 查询外键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 外键列表
     */
    @Override
    public List<ImportKeyBO> listImportedKeys(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = this.getConnection(databaseConnection);
        if (!this.testConnection(connection)) {
            return Lists.newArrayList();
        }
        ResultSet resultSet = connection.getMetaData().getImportedKeys(connection.getCatalog(), connection.getSchema(), tableName);
        Map<String, Field> mapping = CacheUtils.DATABASE_FIELD_MAPPING.get(ImportKeyBO.class);
        if (mapping == null) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, ImportKeyBO.class.getName());
        }
        return DatabaseUtils.resultSetToModel(resultSet, ImportKeyBO.class, mapping);
    }

    /**
     * 查询主键信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @param unique 是否查询唯一索引
     * @param approximate
     * @return
     */
    @Override
    public List<IndexBO> listIndex(@NotNull DatabaseConnectionBO databaseConnection, String tableName, Boolean unique, Boolean approximate) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = this.getConnection(databaseConnection);
        if (!this.testConnection(connection)) {
            return Lists.newArrayList();
        }
        if (approximate == null) {
            approximate = true;
        }
        if (unique == null) {
            unique = true;
        }
        ResultSet resultSet = connection.getMetaData().getIndexInfo(connection.getCatalog(), connection.getSchema(), tableName, unique, approximate);
        Map<String, Field> mapping = CacheUtils.DATABASE_FIELD_MAPPING.get(IndexBO.class);
        if (mapping == null) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, IndexBO.class.getName());
        }
        return DatabaseUtils.resultSetToModel(resultSet, IndexBO.class, mapping);
    }

    /**
     * 查询列信息
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 列信息
     */
    @Override
    @NotNull
    public List<ColumnBO> listColumn(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = this.getConnection(databaseConnection);
        if (!this.testConnection(connection)) {
            return Lists.newArrayList();
        }
        ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), connection.getSchema(), tableName, null);
        Map<String, Field> mapping = CacheUtils.DATABASE_FIELD_MAPPING.get(ColumnBO.class);
        if (mapping == null) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, ColumnBO.class.getName());
        }
        List<ColumnBO> columnList = DatabaseUtils.resultSetToModel(resultSet, ColumnBO.class, mapping);
        if (!columnList.isEmpty()) {
            Map<String, ColumnBO> columnMap = columnList.stream().collect(Collectors.toMap(ColumnBO :: getColumnName, item -> item));
            // 查询主键信息并设置
            List<PrimaryKeyBO> primaryKeyList = this.listPrimaryKey(databaseConnection, tableName);
            primaryKeyList.forEach(item -> {
                ColumnBO column = columnMap.get(item.getColumnName());
                if (column != null) {
                    column.setPrimaryKey(Boolean.TRUE);
                    column.setKeySeq(item.getKeySeq());
                    column.setPkName(item.getPkName());
                }
            });
            // 查询外键信息并设置
            List<ImportKeyBO> importKeyList = this.listImportedKeys(databaseConnection, tableName);
            importKeyList.forEach(item -> {
                ColumnBO column = columnMap.get(item.getPkcolumnName());
                if (column != null) {
                    column.setImportKey(Boolean.TRUE);
                    column.setImportPkName(item.getPkName());
                }
            });
            // 查询索引信息并设置
            List<IndexBO> indexList = this.listUniqueIndex(databaseConnection, tableName);
            indexList.forEach(item -> {
                ColumnBO column = columnMap.get(item.getColumnName());
                if (column != null) {
                    column.setIndexed(Boolean.TRUE);
                    column.setUnique(!item.getNonUnique());
                    column.setIndexType(item.getType());
                }
            });
            // TODO:查询其他索引
            // 设置其他信息
            columnList.forEach(item -> {
                TypeMappingConstant typeMappingConstant = CacheUtils.TYPE_MAPPING_CACHE.get(item.getDataType());
                if (typeMappingConstant != null) {
                    item.setJavaType(typeMappingConstant.getJavaClass().getName());
                    item.setSimpleJavaType(typeMappingConstant.getJavaClass().getSimpleName());
                }
            });
        }
        return columnList;
    }

    /**
     * 查询唯一索引
     * @param databaseConnection 数据库连接信息
     * @param tableName 表名
     * @return 唯一索引列表
     */
    @Override
    public List<IndexBO> listUniqueIndex(@NotNull DatabaseConnectionBO databaseConnection, String tableName) throws IllegalAccessException, SQLException, InstantiationException {
        return this.listIndex(databaseConnection, tableName, true, true);
    }

    /**
     * 映射数据库字段与实体类属性关系
     */
    private static void mappingDatabaseFieldToCache() {
        if (CacheUtils.DATABASE_FIELD_MAPPING.isEmpty()) {
            CacheUtils.DATABASE_FIELD_MAPPING.put(TableViewBO.class, mappingDatabaseField(TableViewBO.class));
            CacheUtils.DATABASE_FIELD_MAPPING.put(PrimaryKeyBO.class, mappingDatabaseField(PrimaryKeyBO.class));
            CacheUtils.DATABASE_FIELD_MAPPING.put(IndexBO.class, mappingDatabaseField(IndexBO.class));
            CacheUtils.DATABASE_FIELD_MAPPING.put(ImportKeyBO.class, mappingDatabaseField(ImportKeyBO.class));
            CacheUtils.DATABASE_FIELD_MAPPING.put(ColumnBO.class, mappingDatabaseField(ColumnBO.class));
        }
    }

    /**
     * 初始化类型映射
     */
    private static void initTypeMappingCache() {
        CacheUtils.TYPE_MAPPING_CACHE.putAll(
                Arrays.stream(TypeMappingConstant.values())
                .collect(Collectors.toMap(TypeMappingConstant :: getDataType, item -> item))
        );
    }

    /**
     * 映射实体类属性
     * @param clazz
     * @return
     */
    private static Map<String, Field> mappingDatabaseField(Class<? extends DatabaseBaseBO> clazz) {
        final Map<String, Field> mapping = Maps.newHashMap();
        Arrays.asList(clazz.getDeclaredFields())
                .forEach(field -> {
                    final DatabaseField databaseFieldAnnotation = AnnotationUtils.getAnnotation(field, DatabaseField.class);
                    if (databaseFieldAnnotation != null) {
                        final String value = databaseFieldAnnotation.value();
                        if (!StringUtils.isEmpty(value)) {
                            field.setAccessible(true);
                            mapping.put(value, field);
                        }
                    }
                });
        return mapping;
    }
}
