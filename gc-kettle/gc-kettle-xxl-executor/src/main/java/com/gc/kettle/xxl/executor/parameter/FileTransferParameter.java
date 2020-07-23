package com.gc.kettle.xxl.executor.parameter;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件类型转换参数
 * @author ShiZhongMing
 * 2020/7/22 22:29
 * @since 0.0.6
 */
@Getter
@Setter
public class FileTransferParameter extends BaseTransferParameter {
    private static final long serialVersionUID = 4595673103596063414L;

    /**
     * 转换路经
     */
    private String path;
}
