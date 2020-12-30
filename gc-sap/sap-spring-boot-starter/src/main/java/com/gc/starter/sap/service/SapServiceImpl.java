package com.gc.starter.sap.service;

import com.gc.sap.core.utils.SapUtils;
import com.gc.starter.sap.jco.RfcManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ShiZhongMing
 * 2020/12/25 15:14
 * @since 1.0
 */
@Slf4j
public class SapServiceImpl implements SapService {

    private final RfcManager rfcManager;

    public SapServiceImpl(RfcManager rfcManager) {
        this.rfcManager = rfcManager;
    }


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
    @Override
    public <T> List<T> execute(@NonNull String functionName, @NonNull String tableName, @NonNull Class<T> clazz, @NonNull Map<String, Object> parameter, Consumer<JCoFunction> beforeHandler) throws JCoException {
        final JCoTable jCoTable = this.execute(functionName, tableName, parameter, beforeHandler);
        return SapUtils.getDataFromSapTable(jCoTable, clazz);
    }


    /**
     * 执行查询
     * @param functionName 函数名
     * @param tableNameList 表格列表
     * @param beforeHandler 执行前回调
     * @return 表格列表
     */
    @Override
    public Map<String, JCoTable> execute(@NonNull String functionName, @NonNull List<String> tableNameList, Consumer<JCoFunction> beforeHandler) throws JCoException {
        if (CollectionUtils.isEmpty(tableNameList)) {
            return Maps.newHashMap();
        }
        // 获取function
        final JCoFunction function = this.rfcManager.getFunction(functionName);
        Assert.notNull(function, functionName + " not found in SAP");
        // 执行前回调
        if (Objects.nonNull(beforeHandler)) {
            beforeHandler.accept(function);
        }
        // 执行查询
        rfcManager.execute(function);
        Map<String, JCoTable> result = Maps.newHashMap();
        // 读取表格
        tableNameList.forEach(tableName -> result.put(tableName, function.getTableParameterList().getTable(tableName)));
        return result;
    }

    /**
     * 执行查询，查询多个表格
     * @param functionName 函数名
     * @param tableNameList 表格列表
     * @param parameter 参数
     * @param beforeHandler 执行前回调
     * @return 表格列表
     */
    @Override
    public Map<String, JCoTable> execute(@NonNull String functionName, @NonNull List<String> tableNameList, @NonNull Map<String, Object> parameter, Consumer<JCoFunction> beforeHandler) throws JCoException {
        return this.execute(functionName, tableNameList, (function -> {
            // 设置参数
            final JCoParameterList input = function.getImportParameterList();
            parameter.forEach(input::setValue);
            // 执行前回调
            if (Objects.nonNull(beforeHandler)) {
                beforeHandler.accept(function);
            }
        }));
    }

    /**
     * 查询单一表格
     * @param functionName 函数名
     * @param tableName 表格列表
     * @param parameter 参数
     * @param beforeHandler 执行前回调
     * @return 结果
     */
    @Override
    public JCoTable execute(@NonNull String functionName, @NonNull String tableName, @NonNull Map<String, Object> parameter, Consumer<JCoFunction> beforeHandler) throws JCoException {
        return this.execute(functionName, Lists.newArrayList(tableName), parameter, beforeHandler).get(tableName);
    }

    /**
     * 执行查询，查询多个表格
     * @param functionName 函数名
     * @param tableNameList 表格列表
     * @param parameter 参数
     * @param tableParameter 表格参数
     * @param beforeHandler 执行前回调
     * @return 表格列表
     */
    @Override
    public Map<String, JCoTable> execute(@NonNull String functionName, @NonNull List<String> tableNameList,  @NonNull Map<String, Object> parameter, String tableParameterName, @NonNull List<Map<String, Object>> tableParameter, Consumer<JCoFunction> beforeHandler) throws JCoException {
        return this.execute(functionName, tableNameList, (function -> {
            // 设置参数
            final JCoParameterList input = function.getImportParameterList();
            parameter.forEach(input::setValue);
            // 设置表格参数
            if (StringUtils.isNotBlank(tableParameterName)) {
                final JCoTable jCoTable = function.getTableParameterList().getTable(tableParameterName);
                tableParameter.forEach(mapParameter -> {
                    jCoTable.appendRow();
                    mapParameter.forEach(jCoTable::setValue);
                });
            }
            // 执行前回调
            if (Objects.nonNull(beforeHandler)) {
                beforeHandler.accept(function);
            }
        }));
    }

    /**
     * 查询单一表格
     * @param functionName 函数名
     * @param tableName 表格列表
     * @param parameter 参数
     * @param tableParameter 表格参数
     * @param beforeHandler 执行前回调
     * @return 结果
     */
    @Override
    public JCoTable execute(@NonNull String functionName, @NonNull String tableName, @NonNull Map<String, Object> parameter, String tableParameterName, @NonNull List<Map<String, Object>> tableParameter, Consumer<JCoFunction> beforeHandler) throws JCoException {
        return this.execute(functionName, Lists.newArrayList(tableName), parameter, tableParameterName, tableParameter, beforeHandler).get(tableName);
    }
}
