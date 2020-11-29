package com.gc.module.file.service;

import com.gc.module.file.model.SysFilePO;
import com.gc.module.file.pojo.bo.SysFileBO;
import com.gc.module.file.pojo.dto.SaveFileDTO;
import com.gc.starter.crud.service.BaseService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * 文件服务层
 * @author shizhongming
 * 2020/1/27 7:50 下午
 */
public interface SysFileService extends BaseService<SysFilePO> {

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param saveFileDTO   文件信息
     * @return 文件实体
     * @throws IOException 异常信息
     */
    @NonNull
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, @NonNull SaveFileDTO saveFileDTO) throws IOException;

    /**
     * 保存文件
     *
     * @param file 文件信息
     * @return 文件信息
     * @throws IOException 异常信息
     */
    @NonNull
    SysFilePO saveFile(@NonNull SysFileBO file) throws IOException;

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @param handlerType   文件执行器类型
     * @return 文件实体信息
     */
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type, String handlerType);

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @return 文件实体信息
     */
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     * @throws IOException IOException
     */
    @Nullable
    SysFilePO deleteFile(@NonNull Long fileId) throws IOException;

    /**
     * 批量删除文件
     *
     * @param fileIds 文件id列表
     * @return 删除是否成功
     * @throws IOException IOException
     */
    boolean batchDeleteFile(@NonNull Collection<Serializable> fileIds) throws IOException;

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFileBO download(@NonNull Long fileId);

    /**
     * 获取文件的绝对路径
     *
     * @param fileId 文件ID
     * @return 文件绝对路径
     */
    String getAbsolutePath(@NonNull Long fileId);

    /**
     * 下载文件
     *
     * @param file 文件实体类信息
     * @return 文件信息
     */
    SysFileBO download(@NonNull SysFilePO file);
}
