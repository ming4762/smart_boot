package com.gc.database.generator.service.impl;

import com.gc.database.generator.engine.TemplateEngine;
import com.gc.database.generator.model.TemplateElement;
import com.gc.database.generator.pojo.bo.DatabaseTemplateModel;
import com.gc.database.generator.service.DatabaseGeneratorService;
import com.gc.database.message.executor.DatabaseExecutor;
import com.gc.database.message.pojo.bo.DatabaseConnectionBO;
import com.gc.database.message.pojo.bo.TableViewBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author shizhongming
 * 2020/7/3 9:36 上午
 */
@Service
@Slf4j
public class DatabaseGeneratorServiceImpl implements DatabaseGeneratorService {

    private static final String DEFAULT_DIC_TEMPLATE = "template/databaseDic.ftl";

    private final TemplateEngine templateEngine;

    public DatabaseGeneratorServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * 创建数据库字典
     * @param databaseConnection 数据库连接信息
     * @param outputStream 输出流
     */
    @Override
    public void createDatabaseDic(@NonNull DatabaseConnectionBO databaseConnection, @NonNull OutputStream outputStream) {
        // 获取默认的模板信息
        final TemplateElement defaultTemplateElement = new TemplateElement(DEFAULT_DIC_TEMPLATE, null);
        this.createDatabaseDic(databaseConnection, outputStream, defaultTemplateElement);
    }

    /**
     * 创建数据库字典
     * @param databaseConnection 数据库连接信息
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    @Override
    public void createDatabaseDic(@NonNull DatabaseConnectionBO databaseConnection, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement) {
        // 获取数据库执行器
        final DatabaseExecutor databaseExecutor = databaseConnection.getDatabaseExecutor();
        // 获取所有数据
        final List<TableViewBO> tableList = databaseExecutor.listTable(databaseConnection, null);

        final DatabaseTemplateModel model = DatabaseTemplateModel.builder()
                .tableList(tableList)
                .currentDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .build();
        this.templateEngine.processToOutputStream(model, outputStream, templateElement);
    }
}
