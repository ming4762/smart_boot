package com.gc.system.pojo.dto.menu;

import com.gc.starter.crud.query.PageQueryParameter;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/2/24 17:42
 * @since 1.0
 */
public class FunctionListLangDTO extends PageQueryParameter<String, Object> {

    private static final long serialVersionUID = -6650115548575592473L;

    private List<String> langs;
}
