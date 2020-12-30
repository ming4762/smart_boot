package com.gc.starter.sap.service;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * SAP服务层
 * @author ShiZhongMing
 * 2020/12/25 15:13
 * @since 1.0
 */
public interface SapService {

    /**
     * 查询表格并转为实体
     * @param functionName 函数名
     * @param tableName 表名
     * @param clazz 实体类
     * @param parameter 参数
     * @param beforeHandler 执行前回调
     * @param <T> 实体类型
     * @return 表格列表
     * @throws JCoException JCoException
     */
    <T> List<T> execute(@NonNull String functionName, @NonNull String tableName, @NonNull Class<T> clazz, @NonNull Map<String, Object> parameter, Consumer<JCoFunction> beforeHandler) throws JCoException;

    /**
     * 执行查询，查询多个表格
     * @param functionName 函数名
     * @param tableNameList 表格列表
     * @param beforeHandler 执行前回调
     * @return 表格列表
     */
    Map<String, JCoTable> execute(@NonNull String functionName, @NonNull List<String> tableNameList, Consumer<JCoFunction> beforeHandler) throws JCoException;


    /**
     * 执行查询，查询多个表格
     * @param functionName 函数名
     * @param tableNameList 表格列表
     * @param parameter 参数
     * @param beforeHandler 执行前回调
     * @return 表格列表
     */
    Map<String, JCoTable> execute(@NonNull String functionName, @NonNull List<String> tableNameList, @NonNull Map<String, Object> parameter, Consumer<JCoFunction> beforeHandler) throws JCoException;

    /**
     * 查询单一表格
     * @param functionName 函数名
     * @param tableName 表格列表
     * @param parameter 参数
     * @param beforeHandler 执行前回调
     * @return 结果
     */
    JCoTable execute(@NonNull String functionName, @NonNull String tableName, @NonNull Map<String, Object> parameter, Consumer<JCoFunction> beforeHandler) throws JCoException;


    /**
     * 执行查询，查询多个表格
     * @param functionName 函数名
     * @param tableNameList 表格列表
     * @param parameter 参数
     * @param tableParameter 表格参数
     * @param beforeHandler 执行前回调
     * @return 表格列表
     */
    Map<String, JCoTable> execute(@NonNull String functionName, @NonNull List<String> tableNameList, @NonNull Map<String, Object> parameter, String tableParameterName, @NonNull List<Map<String, Object>> tableParameter, Consumer<JCoFunction> beforeHandler) throws JCoException;

    /**
     * 查询单一表格
     * @param functionName 函数名
     * @param tableName 表格列表
     * @param parameter 参数
     * @param tableParameter 表格参数
     * @param beforeHandler 执行前回调
     * @return 结果
     */
    JCoTable execute(@NonNull String functionName, @NonNull String tableName, @NonNull Map<String, Object> parameter, String tableParameterName, @NonNull List<Map<String, Object>> tableParameter, Consumer<JCoFunction> beforeHandler) throws JCoException;

}
