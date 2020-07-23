package com.gc.kettle.xxl.executor.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author shizhongming
 * 2020/7/17 3:21 下午
 */
@Getter
@Setter
@ToString
public class BaseJobParameter implements Serializable {


    private static final long serialVersionUID = 8843801592146285210L;

    /**
     * kettle名字
     */
    private String name;

    private String directoryName;

    private Map<String, String> params;

    private Map<String, String> parameterMap;
}
