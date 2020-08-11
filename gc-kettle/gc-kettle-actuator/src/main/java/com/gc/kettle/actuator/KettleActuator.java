package com.gc.kettle.actuator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * kettle执行器
 * @author shizhongming
 * 2020/6/18 11:39 下午
 */
@Slf4j
public class KettleActuator {

    /**
     * 执行转换
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    public static void excuteTransfer(@NonNull String ktrPath) throws KettleException {
        excuteTransfer(ktrPath, new String[0], new HashMap<>(0), new HashMap<>(0));
    }

    /**
     * 执行转换
     * @param ktrPath 转换路径
     * @param params 参数
     * @param variableMap 变量
     * @param parameter 命名参数
     * @throws KettleException 转换异常
     */
    public static void excuteTransfer(@NonNull String ktrPath, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameter) throws KettleException {
        // 初始化kettle环境
        // TODO: 开发中
        System.setProperty("KETTLE_DISABLE_CONSOLE_LOGGING", "Y");
        KettleEnvironment.init();
        EnvUtil.environmentInit();
        TransMeta transMeta = new TransMeta(ktrPath);
        KettleActuator.doExecuteTransfer(transMeta, params, variableMap, parameter);
    }


    /**
     * 执行转换
     * @param transMeta 转换参数
     * @param params 参数
     * @param variableMap 变量
     * @param parameter 命名参数
     * @throws KettleException 转换异常
     */
    private static void doExecuteTransfer(@NonNull TransMeta transMeta, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameter) throws KettleException {
        final Trans trans = new Trans(transMeta);
        // 设置变量
        variableMap.forEach(trans :: setVariable);
        // 设置命名参数
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            trans.setParameterValue(key, value);
        }
        KettleLogStore.getAppender().addLoggingEventListener((KettleLoggingEvent kettleLoggingEvent) -> {
            log.debug("=====================");
            log.debug(kettleLoggingEvent.getLevel() + ":" + kettleLoggingEvent.getMessage());
        });
        trans.setLogLevel(LogLevel.DEBUG);
        // 执行转换
        trans.execute(params);
        // 等待转换完成
        trans.waitUntilFinished();
        if (trans.getErrors() > 0) {
            throw new KettleException("There are errors during transformation exception!(传输过程中发生异常)");
        }
        // 日志处理
        String logChannelId = trans.getLogChannelId();
        LoggingBuffer appender = KettleLogStore.getAppender();
        String logText = appender.getBuffer(logChannelId, true).toString();
//        log.info(logText);

    }

    /**
     * 执行数据库trans
     * @param repository 资源库
     * @param transName 转换名
     * @param directoryName 目录名
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap 命名参数
     * @throws KettleException KettleException
     */
    public static void excuteDbTransfer(@NonNull KettleDatabaseRepository repository, @NonNull String transName, String directoryName, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameterMap) throws KettleException {
        final RepositoryDirectoryInterface directoryInterface = getDirectoryInterface(repository, directoryName);
        // 获取转换
        final TransMeta transMeta = repository.loadTransformation(transName, directoryInterface,null,true,null);
        doExecuteTransfer(transMeta, params, variableMap, parameterMap);
    }

    /**
     * 执行资源库JOB
     * @param repository 资源库
     * @param jobName job名称
     * @param directoryName 目录
     * @param params job参数
     * @param parameterMap 命名参数
     * @throws KettleException Exception
     */
    public static void excuteDbJob(@NonNull KettleDatabaseRepository repository, String jobName, String directoryName, @NonNull Map<String, String> params, @NonNull Map<String, String> parameterMap) throws KettleException {
        final RepositoryDirectoryInterface directoryInterface = getDirectoryInterface(repository, directoryName);
        final JobMeta jobMeta = repository.loadJob(jobName, directoryInterface, null, null);
        final Job job = new Job(repository, jobMeta);
        doExcuteJob(job, params, parameterMap);
    }

    /**
     * 执行JOB
     * @param job job对象
     * @param params job参数
     * @param parameterMap 命名参数
     * @throws KettleException Exception
     */
    private static void doExcuteJob(@NonNull Job job, @NonNull Map<String, String> params, @NonNull Map<String, String> parameterMap) throws KettleException {
        params.forEach(job :: setVariable);
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            job.setParameterValue(key, value);
        }
        job.start();
        job.waitUntilFinished();
        if (job.getErrors() > 0) {
            throw new KettleException("There are errors during job exception!(执行job发生异常)");
        }
    }

    /**
     * 获取资源路径
     * @param repository 资源库
     * @param directoryName 目录名
     * @return 目录路径
     * @throws KettleException KettleException
     */
    private static RepositoryDirectoryInterface getDirectoryInterface(@NonNull KettleDatabaseRepository repository, String directoryName) throws KettleException {
        RepositoryDirectoryInterface directoryInterface = repository.loadRepositoryDirectoryTree();
        if (StringUtils.isNotBlank(directoryName)) {
            directoryInterface = directoryInterface.findDirectory(directoryName);
        }
        return directoryInterface;
    }
}
