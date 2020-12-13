package com.gc.file.common.pojo.bo;

import com.gc.common.base.utils.Base64Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.File;
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

    /**
     * 时间格式化工具
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
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
        this.datePath = FORMATTER.format(Instant.now());
    }

    /**
     * 获取文件夹路径
     * @return 文件夹路径
     */
    public String getFolderPath() {
        return this.basePath + File.separator + this.datePath;
    }

    /**
     * 获取文件路径
     * @return 文件路径
     */
    public String getFilePath() {
        return this.getFolderPath() + File.separator + this.getActualFilename();
    }

    /**
     * 获取文件名
     * @return 文件名
     */
    public String getActualFilename() {
        String path = md5;
        if (!StringUtils.isEmpty(this.filename)) {
            path = path + ID_CUT + this.filename;
        }
        return path;
    }

    /**
     * 获取文件ID
     * @return 文件ID
     */
    public String getFileId() {
        String fileId = this.datePath + ID_CUT + this.md5;
        if (!StringUtils.isEmpty(this.filename)) {
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
