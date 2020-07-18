package com.gc.kettle.xxl.executor;

import com.gc.kettle.starter.service.KettleService;
import com.gc.kettle.xxl.executor.parameter.XxlExecuteJobParameter;
import com.gc.kettle.xxl.executor.parameter.XxlExecuteTransferParameter;
import com.google.common.collect.Maps;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author shizhongming
 * 2020/7/17 11:31 上午
 */
@Component
public class KettleXxlExecutor {

    private final KettleService kettleService;

    public KettleXxlExecutor(KettleService kettleService) {
        this.kettleService = kettleService;
    }

    /**
     * 执行job
     * @param parameter 参数
     * @return 结果
     */
    @XxlJob("executeJob")
    public ReturnT<String> executeJob(XxlExecuteJobParameter parameter) throws Exception {
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
     * @throws Exception Exception
     */
    @XxlJob("executeTransfer")
    public ReturnT<String> executeTransfer(XxlExecuteTransferParameter parameter) throws Exception {
        this.kettleService.executeDbTransfer(
                parameter.getTransName(),
                parameter.getDirectoryName(),
                Optional.ofNullable(parameter.getParams()).orElse(new String[]{}),
                Optional.ofNullable(parameter.getVariableMap()).orElse(Maps.newHashMap()),
                Optional.ofNullable(parameter.getParameterMap()).orElse(Maps.newHashMap())
        );
        return ReturnT.SUCCESS;
    }
}
