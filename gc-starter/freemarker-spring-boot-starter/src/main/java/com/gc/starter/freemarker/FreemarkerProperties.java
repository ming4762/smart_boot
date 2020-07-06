package com.gc.starter.freemarker;

import com.google.common.collect.Lists;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author shizhongming
 * 2020/7/2 8:32 下午
 */
@ConfigurationProperties("gc.freemarker")
@Getter
@Setter
public class FreemarkerProperties {

    private Config config = new Config();

    @Getter
    @Setter
    public static final class Config {

        // 版本
        private Version version = Configuration.VERSION_2_3_29;

        private Charset encoding = StandardCharsets.UTF_8;
        // 加载器
        private List<TemplateLoader> loaders = Lists.newArrayList();
    }
}
