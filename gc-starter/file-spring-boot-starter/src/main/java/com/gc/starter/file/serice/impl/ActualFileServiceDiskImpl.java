package com.gc.starter.file.serice.impl;

import com.gc.common.base.utils.security.Md5Utils;
import com.gc.starter.file.pojo.bo.DiskFilePathBO;
import com.gc.starter.file.serice.ActualFileService;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
    public @NotNull String save(@NotNull InputStream inputStream, String filename) throws IOException {
        try (
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final ByteArrayInputStream writerInputStream = new ByteArrayInputStream(outputStream.toByteArray())
                ) {
            IOUtils.copy(inputStream, outputStream);
            // 计算md5
            final String md5 = Md5Utils.md5(new ByteArrayInputStream(outputStream.toByteArray()));
            final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, md5, filename);
            // 获取文件路径
            final Path folderPath = Paths.get(diskFilePath.getFolderPath());
            if (Files.notExists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            final String filePath = diskFilePath.getFilePath();
            final Path inPath = Paths.get(filePath);
            if (!Files.exists(inPath)) {
                Files.createFile(inPath);
            }
            Files.copy(writerInputStream, inPath);
            return diskFilePath.getFileId();
        }
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    @Override
    public void delete(@NotNull String id) throws IOException {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NotNull List<String> fileIdList) throws IOException {
        for (String fileId : fileIdList) {
            this.delete(fileId);
        }
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
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @Override
    public void download(@NotNull String id, @NotNull OutputStream outputStream) throws IOException {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
