package com.gc.starter.file.serice;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;

/**
 * 实际的文件执行器
 * @author zhongming
 */
public interface ActualFileService {

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @throws IOException 文件写入失败抛出异常
     * @return 文件id
     */
    @NotNull
    String save(@NotNull File file, String filename) throws IOException;

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @throws IOException 保存文件发生IO错误
     * @return 文件ID
     */
    @NotNull
    String save(@NotNull InputStream inputStream, String filename)  throws IOException;

    /**
     * 删除文件
     * @param id 文件ID
     * @return 是否删除成功
     */
    @NotNull
    Boolean delete(@NotNull String id);


    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     * @return 是否删除成功
     */
    @NotNull
    Boolean batchDelete(@NotNull List<String> fileIdList);

    /**
     * 下载文件
     * @param id 文件id
     * @throws FileNotFoundException 未找到下载文件错误
     * @return 文件流
     */
    InputStream download(@NotNull String id) throws FileNotFoundException;

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流
     * @throws IOException 下载文件发生IO错误
     */
    void download(@NotNull String id, @NotNull OutputStream outputStream) throws IOException;

}
