package com.gc.kettle.starter.service;

import com.gc.kettle.actuator.KettleActuator;
import com.google.common.collect.Maps;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
     * @throws KettleException Exception
     */
    void executeDbTransfer(@NonNull String transName, String directoryName, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameterMap) throws KettleException;

    /**
     * 执行资源库转换
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @throws KettleException Exception
     */
    default void executeDbTransfer(@NonNull String transName, String directoryName) throws KettleException {
        this.executeDbTransfer(transName, directoryName, new String[]{}, Maps.newHashMap(), Maps.newHashMap());
    }

    /**
     * 执行资源库转换
     * @param transName 转换名
     * @throws KettleException Exception
     */
    default void executeDbTransfer(@NonNull String transName) throws KettleException {
        this.executeDbTransfer(transName, null);
    }

    /**
     * 执行资源库JOB
     * @param name job名
     * @param directoryName job所在目录
     * @param params 参数
     * @param parameterMap 命名参数
     * @throws KettleException Exception
     */
    void executeDbJob(@NonNull String name, String directoryName, @NonNull Map<String, String> params, @NonNull Map<String, String> parameterMap) throws KettleException;

    /**
     * 执行资源库JOB
     * @param name job名
     * @param directoryName job所在目录
     * @throws KettleException Exception
     */
    default void executeDbJob(@NonNull String name, String directoryName) throws KettleException {
        this.executeDbJob(name, directoryName, Maps.newHashMap(), Maps.newHashMap());
    }

    /**
     * 执行资源库JOB
     * @param name job名
     * @throws KettleException Exception
     */
    default void executeDbJob(@NonNull String name) throws KettleException {
        this.executeDbJob(name, null);
    }

    /**
     * 执行转换
     * @param ktrPath 转换路径
     * @param params 参数
     * @param variableMap 变量
     * @param parameter 命名参数
     * @throws KettleException 转换异常
     */
    default void executeFileTransfer(@NonNull String ktrPath, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameter) throws KettleException {
        KettleActuator.excuteTransfer(ktrPath, params, variableMap, parameter);
    }

    /**
     * 执行转换
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    default void executeFileTransfer(@NonNull String ktrPath) throws KettleException {
        this.executeFileTransfer(ktrPath, new String[]{}, Maps.newHashMap(), Maps.newHashMap());
    }

    /**
     * 执行转换
     * 文件位于classpath
     * @param ktrPath 转换路径
     * @param params 参数
     * @param variableMap 变量
     * @param parameter 命名参数
     * @throws KettleException 转换异常
     */
    default void executeClasspathFileTransfer(@NonNull String ktrPath, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameter) throws IOException, KettleException {
        final URL base = Thread.currentThread().getContextClassLoader().getResource("");
        Assert.notNull(base, "获取classpath路径发生错误");
        // 获取文件路径
        String path = new File(base.getFile(), ktrPath).getCanonicalPath();
        executeFileTransfer(path, params, variableMap, parameter);
    }

    /**
     * 执行转换
     * 文件位于classpath
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    default void executeClasspathFileTransfer(@NonNull String ktrPath) throws KettleException, IOException {
        this.executeClasspathFileTransfer(ktrPath, new String[]{}, Maps.newHashMap(), Maps.newHashMap());
    }

}
