package com.gc.starter.file.nfs.service;

import com.gc.file.common.common.ActualFileServiceRegisterName;
import com.gc.file.common.constants.ActualFileServiceConstants;
import com.gc.file.common.service.ActualFileService;
import com.gc.starter.file.nfs.provider.JcraftChannelProvider;
import com.jcraft.jsch.ChannelSftp;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:56
 * @since 1.0
 */
public class ActualFileServiceNfsImpl implements ActualFileService {

    private final JcraftChannelProvider<ChannelSftp> jcraftChannelProvider;


    public ActualFileServiceNfsImpl(JcraftChannelProvider<ChannelSftp> jcraftChannelProvider) {
        this.jcraftChannelProvider = jcraftChannelProvider;
    }

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @return 文件id
     */
    @Override
    @NonNull
    public String save(@NonNull File file, String filename) throws IOException {
        return null;
    }


    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @return 文件ID
     */
    @Override
    @NonNull
    public String save(@NonNull InputStream inputStream, String filename) {
        return null;
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    @Override
    public void delete(@NonNull String id) {

    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NonNull List<String> fileIdList) {

    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) throws FileNotFoundException {
        return null;
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {

    }

    /**
     * 获取文件的绝对路径
     * @param id 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull String id) {
        return null;
    }

    /**
     * 获取注册名字
     * @return 注册名字
     */
    @Override
    public ActualFileServiceRegisterName getRegisterName() {
        return ActualFileServiceRegisterName.builder()
                .dbName(ActualFileServiceConstants.NFS.name())
                .beanName(ActualFileServiceConstants.NFS.getServiceName())
                .build();
    }
}
