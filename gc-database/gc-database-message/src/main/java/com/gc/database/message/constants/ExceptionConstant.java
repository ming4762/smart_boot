package com.gc.database.message.constants;

import lombok.Getter;

/**
 * 异常信息
 * @author jackson
 */
@Getter
public enum ExceptionConstant {

    /**
     * 未找到驱动类
     */
    DIRVER_CLASS_NOT_FOUND("未找到驱动类，请检查是否引入驱动类：%s"),

    DIRVER_CLASS_INSTANCE("创建驱动类实体失败，驱动类：%s"),

    DATABASE_TYPE_NOT_FOUND("未找到数据库类型"),

    DATABASE_EXECUTER_NOT_FOUND("系统发送异常错误，未找到数据库对应的执行类，数据库类型：%s，数据库执行类：%s"),

    DATABASE_FILE_MAPPING_NOT_FOUND("未找到数据库字段与实体类的对应关系，请检查实体类是否添加DatabaseField注解，实体类：%s");

    private String value;

    ExceptionConstant(String value) {
        this.value = value;
    }
}
