package com.gc.database.generator.engine;

import com.gc.database.generator.model.TemplateElement;
import org.springframework.lang.NonNull;

import java.io.OutputStream;

/**
 * 模板引擎接口
 * @author shizhongming
 * 2020/7/3 9:06 上午
 */
public interface TemplateEngine {

    /**
     * 执行引擎并将接口写入到输出流
     * @param model 模板数据
     * @param outputStream 输出流
     * @param templateElement 模板信息
     */
    void processToOutputStream(@NonNull Object model, @NonNull OutputStream outputStream, @NonNull TemplateElement templateElement);
}
