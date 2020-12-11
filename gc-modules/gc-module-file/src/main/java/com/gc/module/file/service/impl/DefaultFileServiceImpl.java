package com.gc.module.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.exception.BaseException;
import com.gc.file.common.exception.SmartFileException;
import com.gc.file.common.properties.SmartFileProperties;
import com.gc.file.common.service.ActualFileService;
import com.gc.module.file.constants.FileTypeConstants;
import com.gc.module.file.mapper.SysFileMapper;
import com.gc.module.file.model.SysFilePO;
import com.gc.module.file.pojo.bo.SysFileBO;
import com.gc.module.file.pojo.dto.SaveFileDTO;
import com.gc.module.file.service.SysFileService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件处理层实现类
 * @author shizhongming
 * 2020/1/27 7:50 下午
 */
@Slf4j
public class DefaultFileServiceImpl extends BaseServiceImpl<SysFileMapper, SysFilePO> implements SysFileService {



    private final Map<String, ActualFileService> actualFileServiceMap;

    private final SmartFileProperties fileProperties;

    public DefaultFileServiceImpl(ApplicationContext applicationContext, SmartFileProperties fileProperties) {
        this.actualFileServiceMap = DefaultFileServiceImpl.initActualFileService(applicationContext);
        this.fileProperties = fileProperties;
    }

    private static Map<String, ActualFileService> initActualFileService(ApplicationContext applicationContext) {
        // 获取所有执行器
        return applicationContext.getBeansOfType(ActualFileService.class)
                .values().stream()
                .collect(Collectors.toMap(item -> item.getRegisterName().getDbName(), item -> item));
    }

    /**
     * 获取文件执行器
     * @return 文件执行器
     */
    protected ActualFileService getActualFileService(String dbName) {
        ActualFileService actualFileService = this.actualFileServiceMap.get(dbName);
        if (Objects.isNull(actualFileService)) {
            // 获取默认的文件执行器
            actualFileService = this.actualFileServiceMap.get(this.fileProperties.getDefaultHandler());
        }
        if (Objects.isNull(actualFileService)) {
            throw new SmartFileException(String.format("获取文件执行器失败，为找到对应的文件执行器，执行器名称：%s", dbName));
        }
        return actualFileService;
    }

    /**
     * 保存文件
     * @param multipartFile 文件信息
     * @param saveFileDto 文件传输对象
     * @return 保存的文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public @NonNull
    SysFilePO saveFile(@NonNull MultipartFile multipartFile, @NonNull SaveFileDTO saveFileDto) {
        return this.saveFile(this.createSysFileBo(multipartFile, saveFileDto.getFilename(), saveFileDto.getType(), saveFileDto.getHandlerType()));
    }

    /**
     * 获取相同的file
     * @param file 比对的文件信息
     * @return 相同的文件信息，返回null 则不存在相同的文件
     */
    @Override
    public SysFilePO getSameFile(@NonNull SysFilePO file) {
        // 根据md5判断文件是否存在
        final List<SysFilePO> md5FileList = this.list(
                new QueryWrapper<SysFilePO>().lambda()
                        .eq(SysFilePO :: getMd5, file.getMd5())
                        .eq(SysFilePO :: getFileName, file.getFileName())
                        .eq(SysFilePO :: getFileSize, file.getFileSize())
        );
        if (md5FileList.isEmpty()) {
            return null;
        }
        return md5FileList.iterator().next();
    }

    /**
     * 保存文件
     * @param file 文件对象
     * @return 保存的文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public @NonNull SysFilePO saveFile(@NonNull SysFileBO file) {
        // 判断是否存在相同文件
        SysFilePO sameFile = this.getSameFile(file.getFile());
        if (Objects.nonNull(sameFile)) {
            return sameFile;
        }
        // 保存文件
        file.getFile().setDbId(this.saveActualFile(file));
        try {
            if (StringUtils.isEmpty(file.getFile().getType())) {
                file.getFile().setType(FileTypeConstants.TEMP.name());
            }
            this.saveWithUser(file.getFile(), AuthUtils.getCurrentUserId());
            return file.getFile();
        } catch (Exception e) {
            log.error("保存文件信息到数据库发生错误，删除保存的文件");
            this.getActualFileService(file.getFile().getHandlerType()).delete(file.getFile().getDbId());
            throw new BaseException(e);
        }
    }

    /**
     * 保存文件
     * @param multipartFile 文件信息
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type, String handlerType) {
        final SysFilePO file = new SysFilePO();
        file.setType(type);
        try {
            return this.saveFile(new SysFileBO(multipartFile, null, type, handlerType));
        } catch (Exception e) {
            throw new BaseException("系统发生未知异常", e);
        }
    }

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @return 文件实体信息
     */
    @Override
    public SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type) {
        return this.saveFile(multipartFile, type, this.fileProperties.getDefaultHandler());
    }

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public @Nullable SysFilePO deleteFile(@NonNull Long fileId) throws IOException {
        final SysFilePO file = this.getById(fileId);
        if (ObjectUtils.isNotEmpty(file)) {
            // 删除文件信息
            this.removeById(fileId);
            // 删除实际文件
            if (ObjectUtils.isEmpty(file.getDbId())) {
                throw new BaseException("实际文件ID未空，删除失败");
            }
            this.getActualFileService(file.getHandlerType()).delete(file.getDbId());
        }
        return file;
    }

    /**
     * 批量删除文件
     * @param fileIds 文件id列表
     * @return 删除是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteFile(@NonNull Collection<Serializable> fileIds) {
        if (!fileIds.isEmpty()) {
            final List<SysFilePO> fileList = this.listByIds(fileIds);
            if (CollectionUtils.isNotEmpty(fileList)) {
                this.removeByIds(fileIds);
                // 删除实际文件
                fileList.stream()
                        .collect(Collectors.groupingBy(SysFilePO :: getHandlerType))
                        .forEach((key, value) -> this.getActualFileService(key).batchDelete(
                                value.stream()
                                        .map(SysFilePO :: getDbId)
                                        .filter(StringUtils :: isNotEmpty)
                                        .collect(Collectors.toList())
                        ));
            }
            return true;
        }
        return false;
    }

    /**
     * 下载文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    @Nullable
    public SysFileBO download(@NonNull Long fileId) {
        final SysFilePO file = this.getById(fileId);
        if (ObjectUtils.isNotEmpty(file)) {
            return this.download(file);
        }
        return null;
    }


    /**
     * 获取文件的绝对路径
     * @param fileId 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull Long fileId) {
        final SysFilePO file = this.getById(fileId);
        Assert.notNull(file.getDbId(), "获取文件信息发生错误，未找到文件实体ID");
        return this.getActualFileService(file.getHandlerType()).getAbsolutePath(file.getDbId());
    }

    /**
     * 下载文件
     * @param file 文件实体类信息
     * @return 文件信息
     */
    @Override
    @NonNull
    public SysFileBO download(@NonNull SysFilePO file) {
        Assert.notNull(file.getDbId(), "实际文件ID未空，删除失败");
        try {
            return new SysFileBO(file, this.getActualFileService(file.getHandlerType()).download(file.getDbId()));
        } catch (FileNotFoundException e) {
            throw new BaseException("文件未找到", e);
        }
    }

    /**
     * 创建 SysFileBO
     * @param file 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @param handlerType 执行器类型
     * @return SysFileBO
     */
    private SysFileBO createSysFileBo(MultipartFile file, String fileName, String type, String handlerType) {
        String handler = StringUtils.isBlank(handlerType) ? this.fileProperties.getDefaultHandler() : handlerType;
        return new SysFileBO(file, fileName, type, handler);
    }

    /**
     * 保存实际文件
     * @param file 文件信息
     * @return 文件ID
     */
    private String saveActualFile(SysFileBO file) {
        return this.getActualFileService(file.getFile().getHandlerType()).save(file.getInputStream(), file.getFile().getFileName());
    }
}
