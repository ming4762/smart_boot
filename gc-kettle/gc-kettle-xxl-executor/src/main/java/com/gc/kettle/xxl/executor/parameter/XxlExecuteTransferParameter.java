package com.gc.kettle.xxl.executor.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author shizhongming
 * 2020/7/17 3:25 下午
 */
@Getter
@Setter
@ToString
public class XxlExecuteTransferParameter implements Serializable {
    private static final long serialVersionUID = -377870716025550955L;

    private String transName;

    private String directoryName;

    private String[] params;

    private Map<String, String> variableMap;

    private Map<String, String> parameterMap;
}
