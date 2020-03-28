package com.gc.module.file.pojo.bo;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gc.common.base.utils.security.Md5Utils;
import com.gc.module.file.constants.FileTypeConstants;
import com.gc.module.file.model.SysFilePO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jackson
 * 2020/1/27 7:52 下午
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class SysFileBO {
    private SysFilePO file;

    private InputStream inputStream;

    public SysFileBO(@NotNull MultipartFile multipartFile, String filename, String type) throws IOException {
        this.file = SysFilePO.builder()
                .fileId(IdWorker.getId())
                .fileName(StringUtils.isEmpty(filename) ? multipartFile.getOriginalFilename() : filename)
                .type(StringUtils.isEmpty(type) ? FileTypeConstants.TEMP.name() : type)
                .contentType(multipartFile.getContentType())
                .md5(Md5Utils.md5(multipartFile.getInputStream()))
                .size(multipartFile.getSize())
                .build();
        this.inputStream = multipartFile.getInputStream();
    }

}
