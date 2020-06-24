package com.gc.kettle.actuator;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.EnvUtil;
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
public class KettleActuator {

    /**
     * 执行转换
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    public static void excuteTransfer(@NonNull String ktrPath) throws KettleException {
        excuteTransfer(ktrPath, new String[0], new HashMap<>(), new HashMap<>());
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
        // 执行转换
        trans.execute(params);
        // 等待转换完成
        trans.waitUntilFinished();
        if (trans.getErrors() > 0) {
            throw new KettleException("There are errors during transformation exception!(传输过程中发生异常)");
        }
    }

    public static void main(String[] args) throws KettleException {
        KettleActuator.excuteTransfer("/Users/shizhongming/Documents/temp/test/kettle/ceshi1.ktr");
    }
}
