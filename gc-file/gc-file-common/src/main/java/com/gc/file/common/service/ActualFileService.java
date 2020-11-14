package com.gc.file.common.service;


import org.springframework.lang.NonNull;

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
    @NonNull
    String save(@NonNull File file, String filename) throws IOException;

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @throws IOException 保存文件发生IO错误
     * @return 文件ID
     */
    @NonNull
    String save(@NonNull InputStream inputStream, String filename)  throws IOException;

    /**
     * 删除文件
     * @param id 文件ID
     * @throws IOException IOException
     */
    void delete(@NonNull String id) throws IOException;


    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     * @throws IOException IOException
     */
    void batchDelete(@NonNull List<String> fileIdList) throws IOException;

    /**
     * 下载文件
     * @param id 文件id
     * @throws FileNotFoundException 未找到下载文件错误
     * @return 文件流
     */
    InputStream download(@NonNull String id) throws FileNotFoundException;

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流
     * @throws IOException 下载文件发生IO错误
     */
    void download(@NonNull String id, @NonNull OutputStream outputStream) throws IOException;

    /**
     * 获取文件的绝对路径
     * @param id 文件ID
     * @return 文件绝对路径
     */
    String getAbsolutePath(@NonNull String id);

}
