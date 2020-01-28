package com.gc.module.file.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jackson
 * 2020/1/28 2:48 下午
 */
@Getter
@Setter
public class SaveFileDTO implements Serializable {

    private static final long serialVersionUID = 8030356270886177531L;
    private String filename;

    private String type;
}
