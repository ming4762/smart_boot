package com.gc.kettle.xxl.executor;

import com.gc.kettle.starter.service.KettleService;
import com.gc.kettle.xxl.executor.parameter.BaseJobParameter;
import com.gc.kettle.xxl.executor.parameter.FileTransferParameter;
import com.google.common.collect.Maps;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author shizhongming
 * 2020/7/17 11:31 上午
 */
@Component
public class KettleXxlExecutor {

    private final KettleService kettleService;

    private static final String CLASS_PATH = "classpath:";

    public KettleXxlExecutor(KettleService kettleService) {
        this.kettleService = kettleService;
    }

    /**
     * 执行job
     * TODO: 待完善
     * @param parameter 参数
     * @return 结果
     */
    @XxlJob("executeFileJob")
    public ReturnT<String> executeFileJob(BaseJobParameter parameter) throws KettleException {
        // 执行kettle
        this.kettleService.executeDbJob(
                parameter.getName(),
                parameter.getDirectoryName(),
                Optional.ofNullable(parameter.getParams()).orElse(Maps.newHashMap()),
                Optional.ofNullable(parameter.getParameterMap()).orElse(Maps.newHashMap())
        );
        return ReturnT.SUCCESS;
    }

    /**
     * 执行转换
     * @param parameter 参数
     * @return 结果
     * @throws KettleException Exception
     */
    @XxlJob("executeFileTransfer")
    public ReturnT<String> executeFileTransfer(FileTransferParameter parameter) throws KettleException {
        this.kettleService.executeFileTransfer(
                parameter.getPath(),
                Optional.ofNullable(parameter.getParams()).orElse(new String[]{}),
                Optional.ofNullable(parameter.getVariableMap()).orElse(Maps.newHashMap()),
                Optional.ofNullable(parameter.getParameterMap()).orElse(Maps.newHashMap())
        );
        return ReturnT.SUCCESS;
    }

    /**
     * 执行转换，文件位于classPath
     * @param parameter 参数
     * @return 结果
     * @throws KettleException KettleException
     */
    public ReturnT<String> executeClassPathFileTransfer(FileTransferParameter parameter) throws KettleException {
        // 获取路径
        final String path = CLASS_PATH + parameter.getPath();
        // 执行转换
        this.kettleService.executeFileTransfer(
                path,
                Optional.ofNullable(parameter.getParams()).orElse(new String[]{}),
                Optional.ofNullable(parameter.getVariableMap()).orElse(Maps.newHashMap()),
                Optional.ofNullable(parameter.getParameterMap()).orElse(Maps.newHashMap())
        );
        return ReturnT.SUCCESS;
    }
}
