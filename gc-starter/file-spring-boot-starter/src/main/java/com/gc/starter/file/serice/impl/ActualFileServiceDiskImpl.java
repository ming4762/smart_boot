package com.gc.starter.file.serice.impl;

import com.gc.starter.file.pojo.bo.DiskFilePathBO;
import com.gc.starter.file.serice.ActualFileService;
import com.smart.common.base.utils.security.MD5Utils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地磁盘文件执行器
 * @author shizhongming
 * 2020/1/15 8:14 下午
 */
public class ActualFileServiceDiskImpl implements ActualFileService {

    private String basePath;

    public ActualFileServiceDiskImpl(String basePath) {
        this.basePath = basePath;
    }

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @return 文件id
     */
    @Override
    public @NotNull String save(@NotNull File file, String filename) throws IOException {
        return this.save(new FileInputStream(file), StringUtils.isEmpty(filename) ? file.getName() : filename);
    }

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @return 文件ID
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public @NotNull String save(@NotNull InputStream inputStream, String filename) throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            // 计算md5
            final String md5 = MD5Utils.md5(new ByteArrayInputStream(outputStream.toByteArray()));
            final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, md5, filename);
            // 获取文件路径
            final String folderPath = diskFilePath.getFolderPath();
            final File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final String filePath = diskFilePath.getFilePath();
            final File uploadFile = new File(filePath);
            if (!uploadFile.exists()) {
                uploadFile.createNewFile();
            }
            IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), new FileOutputStream(uploadFile));
            return diskFilePath.getFileId();
        }
    }

    /**
     * 删除文件
     * @param id 文件ID
     * @return 是否删除成功
     */
    @Override
    public @NotNull Boolean delete(@NotNull String id) {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return Boolean.FALSE;
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     * @return 是否删除成功
     */
    @Override
    public @NotNull Boolean batchDelete(@NotNull List<String> fileIdList) {
        final List<Boolean> result= fileIdList
                .stream()
                .map(this::delete)
                .filter(item -> !item)
                .collect(Collectors.toList());
        return result.isEmpty();
    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NotNull String id) throws FileNotFoundException {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        return new FileInputStream(file);
    }

    /**
     * 下载文件
     * @param id
     * @param outputStream
     */
    @Override
    public void download(@NotNull String id, @NotNull OutputStream outputStream) throws IOException {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        IOUtils.copy(new FileInputStream(file), outputStream);
    }
}
