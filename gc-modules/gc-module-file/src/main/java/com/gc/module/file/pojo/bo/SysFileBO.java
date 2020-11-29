package com.gc.module.file.pojo.bo;

import com.gc.common.base.exception.IORuntimeException;
import com.gc.common.base.utils.security.Md5Utils;
import com.gc.module.file.constants.FileTypeConstants;
import com.gc.module.file.model.SysFilePO;
import com.gc.starter.crud.utils.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
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

    public SysFileBO(@NonNull MultipartFile multipartFile, String filename, String type, String handlerType) {
        try {
            this.file = SysFilePO.builder()
                    .fileId(IdGenerator.nextId())
                    .fileName(StringUtils.isEmpty(filename) ? multipartFile.getOriginalFilename() : filename)
                    .type(StringUtils.isEmpty(type) ? FileTypeConstants.TEMP.name() : type)
                    .contentType(multipartFile.getContentType())
                    .handlerType(handlerType)
                    .md5(Md5Utils.md5(multipartFile.getInputStream()))
                    .fileSize(multipartFile.getSize())
                    .build();
            this.inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
