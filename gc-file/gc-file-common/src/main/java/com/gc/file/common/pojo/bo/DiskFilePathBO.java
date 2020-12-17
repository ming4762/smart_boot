package com.gc.file.common.pojo.bo;

import com.gc.common.base.utils.Base64Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 文件路径业务类
 * @author shizhongming
 * 2020/1/15 8:36 下午
 */
@NoArgsConstructor
@ToString
public class DiskFilePathBO {

    public static final String ID_CUT = "_";

    private static final int IDS_LENGTH = 2;

    private static final String FILE_SEPARATOR = "/";

    private static final String SEPARATOR_REPLACE = "@@";

    /**
     * 时间格式化工具
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            .withZone(ZoneId.systemDefault());

    private String basePath;

    private String datePath;

    private String md5;

    @Getter
    private String filename;

    public DiskFilePathBO(String basePath, String md5, String filename) {
        this.basePath = basePath;
        this.md5 = md5;
        this.filename = Base64Util.encoder(filename);
        // 如果文件名中存在路径分隔符，则进行替换
        if (StringUtils.contains(this.filename, FILE_SEPARATOR)) {
            this.filename = StringUtils.replace(this.filename, FILE_SEPARATOR, SEPARATOR_REPLACE);
        }
        this.datePath = FORMATTER.format(Instant.now());
    }

    /**
     * 获取文件夹路径
     * @return 文件夹路径
     */
    public String getFolderPath() {
        return this.basePath + FILE_SEPARATOR + this.datePath;
    }

    /**
     * 获取文件路径
     * @return 文件路径
     */
    public String getFilePath() {
        return this.getFolderPath() + FILE_SEPARATOR + this.getDiskFilename();
    }

    /**
     * 获取存储文件名
     * @return 文件名
     */
    public String getDiskFilename() {
        String path = md5;
        if (!StringUtils.isBlank(this.filename)) {
            path = path + ID_CUT + this.filename;
        }
        return path;
    }

    /**
     * 获取实际文件名
     * @return 实际文件名
     */
    public String getActualFilename() {
        String name = this.filename;
        if (StringUtils.contains(this.filename, SEPARATOR_REPLACE)) {
            name = StringUtils.replace(this.filename, SEPARATOR_REPLACE, FILE_SEPARATOR);
        }
        return Base64Util.decoder(name);
    }

    /**
     * 获取文件ID
     * @return 文件ID
     */
    public String getFileId() {
        String fileId = this.datePath + ID_CUT + this.md5;
        if (!StringUtils.isBlank(this.filename)) {
            fileId = fileId + ID_CUT + this.filename;
        }
        return fileId;
    }

    /**
     * 通过ID创建
     * @param id 文件ID
     * @param basePath 基础路径
     * @return 本地文件信息
     */
    @NonNull
    public static DiskFilePathBO createById(@NonNull String id, @NonNull String basePath) {
        final String[] ids = id.split(DiskFilePathBO.ID_CUT);
        DiskFilePathBO diskFilePath = new DiskFilePathBO();
        diskFilePath.basePath = basePath;
        diskFilePath.datePath = ids[0];
        diskFilePath.md5 = ids[1];
        if (ids.length > IDS_LENGTH) {
            diskFilePath.filename = ids[2];
        }
        return diskFilePath;
    }
}
