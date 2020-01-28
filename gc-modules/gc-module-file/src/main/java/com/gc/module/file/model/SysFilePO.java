package com.gc.module.file.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelCreateUserTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jackson
 * 2020/1/27 7:48 下午
 */
@TableName("sys_file")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class SysFilePO extends BaseModelCreateUserTime {

    private static final long serialVersionUID = -9077274336204793728L;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 类型
     */
    private String type;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 数据库ID
     */
    private String dbId;

    /**
     * MD5
     */
    private String md5;

    /**
     * 序号
     */
    private Integer seq;
}