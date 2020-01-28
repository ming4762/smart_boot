package com.gc.module.file.service;

import com.gc.module.file.model.SysFilePO;
import com.gc.module.file.pojo.bo.SysFileBO;
import com.gc.module.file.pojo.dto.SaveFileDTO;
import com.gc.starter.crud.service.BaseService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

/**
 * @author jackson
 * 2020/1/27 7:50 下午
 */
public interface SysFileService extends BaseService<SysFilePO> {

    /**
     * 保存文件
     * @param multipartFile
     * @param saveFileDTO
     * @return
     */
    @NotNull
    SysFilePO saveFile(@NotNull MultipartFile multipartFile, @NotNull SaveFileDTO saveFileDTO) throws IOException;

    /**
     * 保存文件
     * @param file
     * @return
     */
    @NotNull
    SysFilePO saveFile(@NotNull SysFileBO file) throws IOException;

    /**
     * 保存文件
     * @param multipartFile
     * @param type
     * @return
     */
    SysFilePO saveFile(@NotNull MultipartFile multipartFile, String type) throws IOException;

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Nullable
    SysFilePO deleteFile(@NotNull Long fileId);

    /**
     * 批量删除文件
     * @param fileIds 文件id列表
     * @return 删除是否成功
     */
    @NotNull
    Boolean batchDeleteFile(@NotNull Collection<Long> fileIds);

    /**
     * 下载文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFileBO downLoad(@NotNull Long fileId) throws FileNotFoundException;

    /**
     * 下载文件
     * @param file 文件实体类信息
     * @return 文件信息
     */
    SysFileBO download(@NotNull SysFilePO file) throws FileNotFoundException;
}
