package com.gc.module.file.pojo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author jackson
 * 2020/1/28 2:48 下午
 */
@Getter
@Setter
@Builder
@ToString
public class SaveFileDTO implements Serializable {

    private static final long serialVersionUID = 8030356270886177531L;
    private String filename;

    private String type;

    private String handlerType;
}
