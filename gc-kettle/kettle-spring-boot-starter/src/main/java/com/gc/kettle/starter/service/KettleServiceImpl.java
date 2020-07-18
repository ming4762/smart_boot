package com.gc.kettle.starter.service;

import com.gc.kettle.actuator.KettleActuator;
import com.gc.kettle.starter.pool.KettleRepositoryProvider;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * kettle服务层
 * @author shizhongming
 * 2020/7/16 8:59 下午
 */
@Service
public class KettleServiceImpl implements KettleService {

    private final KettleRepositoryProvider kettleRepositoryProvider;

    public KettleServiceImpl(KettleRepositoryProvider kettleRepositoryProvider) {
        this.kettleRepositoryProvider = kettleRepositoryProvider;
    }

    /**
     * 执行资源库转换
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap  命名参数
     * @throws Exception Exception
     */
    @Override
    public void executeDbTransfer(@NonNull String transName, String directoryName, @NonNull String[] params, @NonNull Map<String, String> variableMap, @NonNull Map<String, String> parameterMap) throws Exception {
        KettleDatabaseRepository repository = this.kettleRepositoryProvider.getRepository();
        KettleActuator.excuteDbTransfer(repository, transName, directoryName, params, variableMap, parameterMap);
        kettleRepositoryProvider.returnRepository(repository);
    }

    /**
     * 执行资源库JOB
     * @param name job名
     * @param directoryName job所在目录
     * @param params 参数
     * @param parameterMap 命名参数
     * @throws Exception Exception
     */
    @Override
    public void executeDbJob(@NonNull String name, String directoryName, @NonNull Map<String, String> params, @NonNull Map<String, String> parameterMap) throws Exception {
        KettleDatabaseRepository repository = this.kettleRepositoryProvider.getRepository();
        KettleActuator.excuteDbJob(repository, name, directoryName, params, parameterMap);
        kettleRepositoryProvider.returnRepository(repository);
    }
}
