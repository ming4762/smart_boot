package com.gc.database.generator.service;

import com.gc.database.generator.model.TemplateElement;
import com.gc.database.message.pojo.bo.DatabaseConnectionBO;
import org.springframework.lang.NonNull;

import java.io.OutputStream;

/**
 * @author shizhongming
 * 2020/7/3 9:32 上午
 */
public interface DatabaseGeneratorService {

    /**
     * 创建数据库字典
     * @param databaseConnection 数据库连接信息
     * @param outputStream 输出流
     */
    void createDatabaseDic(@NonNull DatabaseConnectionBO databaseConnection, @NonNull OutputStream outputStream);


    /**
     * 创建数据库字典
     * @param databaseConnection 数据库连接信息
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    void createDatabaseDic(@NonNull DatabaseConnectionBO databaseConnection, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement);
}
