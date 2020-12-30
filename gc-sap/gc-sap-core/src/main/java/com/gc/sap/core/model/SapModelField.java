package com.gc.sap.core.model;

import com.gc.sap.core.annotation.SapField;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * 描述SAP字段
 * TODO:待完善
 * 1、添加自定义转换器
 * @author ShiZhongMing
 * 2020/12/25 10:03
 * @since 1.0
 */
@Getter
@Setter
public class SapModelField {

    private Field field;

    private SapField sapField;

    private PropertyDescriptor propertyDescriptor;
}
