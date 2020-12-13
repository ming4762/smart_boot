package com.gc.starter.file.nfs.service;

import com.gc.common.base.exception.BaseException;
import com.gc.common.base.exception.IORuntimeException;
import com.gc.common.base.utils.security.Md5Utils;
import com.gc.common.jcraft.exception.SftpExceptionRuntimeException;
import com.gc.common.jcraft.utils.JcraftUtils;
import com.gc.file.common.common.ActualFileServiceRegisterName;
import com.gc.file.common.constants.ActualFileServiceConstants;
import com.gc.file.common.pojo.bo.DiskFilePathBO;
import com.gc.file.common.properties.SmartFileProperties;
import com.gc.file.common.service.ActualFileService;
import com.gc.starter.file.nfs.provider.JcraftChannelProvider;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2020/12/7 14:56
 * @since 1.0
 */
public class ActualFileServiceNfsImpl implements ActualFileService {

    private final JcraftChannelProvider<ChannelSftp> jcraftChannelProvider;

    private final String basePath;

    public ActualFileServiceNfsImpl(JcraftChannelProvider<ChannelSftp> jcraftChannelProvider, SmartFileProperties properties) {
        this.jcraftChannelProvider = jcraftChannelProvider;
        if (org.apache.commons.lang3.StringUtils.isBlank(properties.getNfs().getBasePath())) {
            throw new BaseException("使用NFS必须设置基础路径：gc:file:nfs:base-path");
        }
        this.basePath = properties.getNfs().getBasePath();
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
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return this.save(inputStream, StringUtils.isEmpty(filename) ? file.getName() : filename);
        }
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
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 获取channel
            final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
            IOUtils.copy(inputStream, outputStream);
            // 计算md5
            final String md5 = Md5Utils.md5(new ByteArrayInputStream(outputStream.toByteArray()));
            final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, md5, filename);
            // 创建并进入路径
            JcraftUtils.createDirectories(channelSftp, diskFilePath.getFolderPath());
            // 执行保存
            channelSftp.put(new ByteArrayInputStream(outputStream.toByteArray()), diskFilePath.getDiskFilename());
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
            return diskFilePath.getFileId();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (SftpException e) {
            throw new SftpExceptionRuntimeException(e);
        }
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    @Override
    public void delete(@NonNull String id) {
        try {
            // 获取channel
            final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
            final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
            channelSftp.cd(diskFile.getFolderPath());
            channelSftp.rm(diskFile.getDiskFilename());
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
        } catch (SftpException e) {
            throw new SftpExceptionRuntimeException(e);
        }
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NonNull List<String> fileIdList) {
        fileIdList.forEach(this::delete);
    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) throws FileNotFoundException {
        try {
            // 获取channel
            final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
            final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
            channelSftp.cd(diskFile.getFolderPath());
            final InputStream inputStream = channelSftp.get(diskFile.getDiskFilename());
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
            return inputStream;
        } catch (SftpException e) {
            throw new SftpExceptionRuntimeException(e);
        }
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {
        try {
            // 获取channel
            final ChannelSftp channelSftp = this.jcraftChannelProvider.getChannel();
            final DiskFilePathBO diskFile = DiskFilePathBO.createById(id, this.basePath);
            channelSftp.cd(diskFile.getFolderPath());
            try (final InputStream inputStream = channelSftp.get(diskFile.getDiskFilename())) {
                IOUtils.copy(inputStream, outputStream);
            }
            // 归还连接
            this.jcraftChannelProvider.returnChannel(channelSftp);
        } catch (SftpException e) {
            throw new SftpExceptionRuntimeException(e);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取文件的绝对路径
     * @param id 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull String id) {
        return DiskFilePathBO.createById(id, this.basePath).getFilePath();
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
