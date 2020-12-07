package com.gc.file.common.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2020/12/7 15:01
 * @since 1.0
 */
@Getter
public enum ActualFileServiceConstants {

    DISK("ActualFileDiskService"),
    MONGO("ActualFileMongoService"),
    NFS("ActualFileNfsService");


    private final String serviceName;

    ActualFileServiceConstants(String serviceName) {
        this.serviceName = serviceName;
    }
}
