package com.gc.kettle.starter.service;

import com.google.common.collect.Maps;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author shizhongming
 * 2020/7/16 8:54 下午
 */
public interface KettleService {

    /**
     * 执行资源库转换
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap  命名参数
     * @throws Exception Exception
     */
    void executeDbTransfer(@NonNull String transName, String directoryName, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameterMap) throws Exception;

    /**
     * 执行资源库转换
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @throws Exception Exception
     */
    default void executeDbTransfer(@NonNull String transName, String directoryName) throws Exception {
        this.executeDbTransfer(transName, directoryName, new String[]{}, Maps.newHashMap(), Maps.newHashMap());
    }

    /**
     * 执行资源库转换
     * @param transName 转换名
     * @throws Exception Exception
     */
    default void executeDbTransfer(@NonNull String transName) throws Exception {
        this.executeDbTransfer(transName, null);
    }

    /**
     * 执行资源库JOB
     * @param name job名
     * @param directoryName job所在目录
     * @param params 参数
     * @param parameterMap 命名参数
     * @throws Exception Exception
     */
    void executeDbJob(@NonNull String name, String directoryName, @NonNull Map<String, String> params, @NonNull Map<String, String> parameterMap) throws Exception;

    /**
     * 执行资源库JOB
     * @param name job名
     * @param directoryName job所在目录
     * @throws Exception Exception
     */
    default void executeDbJob(@NonNull String name, String directoryName) throws Exception {
        this.executeDbJob(name, directoryName, Maps.newHashMap(), Maps.newHashMap());
    }

    /**
     * 执行资源库JOB
     * @param name job名
     * @throws Exception Exception
     */
    default void executeDbJob(@NonNull String name) throws Exception {
        this.executeDbJob(name, null);
    }
}
