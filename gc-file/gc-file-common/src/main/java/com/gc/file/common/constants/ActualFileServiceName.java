package com.gc.file.common.constants;

/**
 * @author shizhongming
 * 2020/11/29 3:04 上午
 */
public class ActualFileServiceName {

    private ActualFileServiceName() { throw new IllegalStateException("Utility class"); }

    /**
     * 本地文件存储器名字
     */
    public static final String DISK_ACTUAL_FILE_SERVICE = "ActualFileDiskService";

    /**
     * mongo文件存储器名字
     */
    public static final String MONGO_ACTUAL_FILE_SERVICE = "ActualFileMongoService";

}
