package com.gc.file.common.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件执行器注册名字
 * @author shizhongming
 * 2020/11/29 3:40 上午
 */
@Getter
@Setter
@Builder
public class ActualFileServiceRegisterName {

    /**
     * spring bean 名称
     */
    private String beanName;

    /**
     * 存储在数据库名字
     */
    private String dbName;
}
