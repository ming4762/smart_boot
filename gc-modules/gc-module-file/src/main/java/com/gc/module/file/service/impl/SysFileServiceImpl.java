package com.gc.module.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.base.exception.ServiceException;
import com.gc.module.file.constants.FileDatabaseConstants;
import com.gc.module.file.constants.FileTypeConstants;
import com.gc.module.file.mapper.SysFileMapper;
import com.gc.module.file.model.SysFilePO;
import com.gc.module.file.pojo.bo.SysFileBO;
import com.gc.module.file.pojo.dto.SaveFileDTO;
import com.gc.module.file.service.SysFileService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.starter.file.serice.ActualFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/27 7:50 下午
 */
@Service
@Slf4j
public class SysFileServiceImpl extends BaseServiceImpl<SysFileMapper, SysFilePO> implements SysFileService {

    @Autowired
    private ActualFileService actualFileService;

    /**
     * 保存文件
     * @param multipartFile
     * @param saveFileDto
     * @return
     */
    @Override
    @Transactional(value = FileDatabaseConstants.TRANSACTION_MANAGER, rollbackFor = Exception.class)
    public @NotNull SysFilePO saveFile(@NotNull MultipartFile multipartFile, @NotNull SaveFileDTO saveFileDto) throws IOException {
        return this.saveFile(new SysFileBO(multipartFile, saveFileDto.getFilename(), saveFileDto.getType()));
    }

    /**
     * 保存文件
     * @param file
     * @return
     */
    @Override
    @Transactional(value = FileDatabaseConstants.TRANSACTION_MANAGER, rollbackFor = Exception.class)
    public @NotNull SysFilePO saveFile(@NotNull SysFileBO file) throws IOException {
        // 根据md5判断文件是否存在
        final List<SysFilePO> md5FileList = this.list(
                new QueryWrapper<SysFilePO>().lambda().eq(SysFilePO :: getMd5, file.getFile().getMd5())
        );
        if (md5FileList.isEmpty()) {
            // 保存文件
            file.getFile().setDbId(this.saveActualFile(file));
            try {
                if (StringUtils.isEmpty(file.getFile().getType())) {
                    file.getFile().setType(FileTypeConstants.TEMP.name());
                }
                this.save(file.getFile());
                return file.getFile();
            } catch (Exception e) {
                log.error("保存文件信息到数据库发生错误，删除保存的文件");
                this.actualFileService.delete(file.getFile().getDbId());
                throw e;
            }
        } else {
            return md5FileList.iterator().next();
        }
    }

    /**
     * 保存文件
     * @param multipartFile
     * @param type
     * @return
     * @throws IOException
     */
    @Override
    public SysFilePO saveFile(@NotNull MultipartFile multipartFile, String type) throws IOException {
        final SysFilePO file = new SysFilePO();
        file.setType(type);
        return this.saveFile(new SysFileBO(multipartFile, null, type));
    }

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    public @Nullable SysFilePO deleteFile(@NotNull Long fileId) {
        final SysFilePO file = this.getById(fileId);
        if (ObjectUtils.isNotEmpty(file)) {
            // 删除文件信息
            this.removeById(fileId);
            // 删除实际文件
            if (ObjectUtils.isEmpty(file.getDbId())) {
                throw new ServiceException("实际文件ID未空，删除失败");
            }
            this.actualFileService.delete(file.getDbId());
        }
        return file;
    }

    /**
     * 批量删除文件
     * @param fileIds 文件id列表
     * @return 删除是否成功
     */
    @Override
    @NotNull
    public Boolean batchDeleteFile(@NotNull Collection<Long> fileIds) {
        if (!fileIds.isEmpty()) {
            final List<SysFilePO> fileList = this.listByIds(fileIds);
            this.removeByIds(fileIds);
            // 删除时间文件
            List<String> dbIdList = fileList.stream()
                    .map(SysFilePO :: getDbId)
                    .filter(StringUtils :: isNotEmpty)
                    .collect(Collectors.toList());
            this.actualFileService.batchDelete(dbIdList);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 下载文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    @Nullable
    public SysFileBO downLoad(@NotNull Long fileId) throws FileNotFoundException {
        final SysFilePO file = this.getById(fileId);
        if (ObjectUtils.isNotEmpty(file)) {
            return this.download(file);
        }
        return null;
    }

    /**
     * 下载文件
     * @param file 文件实体类信息
     * @return 文件信息
     */
    @Override
    @NotNull
    public SysFileBO download(@NotNull SysFilePO file) throws FileNotFoundException {
        Assert.notNull(file.getDbId(), "实际文件ID未空，删除失败");
        return new SysFileBO(file, this.actualFileService.download(file.getDbId()));
    }

    /**
     * 保存实际文件
     * @param file
     * @return
     */
    private String saveActualFile(SysFileBO file) throws IOException {
        return this.actualFileService.save(file.getInputStream(), file.getFile().getFileName());
    }
}
