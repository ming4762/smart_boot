package com.gc.starter.i18n;

import com.gc.common.i18n.cache.MemoryResourceCache;
import com.gc.common.i18n.cache.ResourceCache;
import com.gc.common.i18n.format.DefaultMessageFormat;
import com.gc.common.i18n.format.MessageFormat;
import com.gc.common.i18n.reader.ResourceBundleResourceReader;
import com.gc.common.i18n.reader.ResourceReader;
import com.gc.common.i18n.source.AutoReloadMessageSource;
import com.gc.common.i18n.utils.I18nUtils;
import com.gc.starter.i18n.resolver.AcceptHeaderAndUserLocaleResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.PostConstruct;

/**
 * @author shizhongming
 * 2020/5/12 5:57 下午
 */
@Configuration
@EnableConfigurationProperties
public class I18nAutoConfiguration {


    @Bean
    @ConfigurationProperties("gc.i18n")
    public I18nProperties i18nProperties() {
        return new I18nProperties();
    }


    @PostConstruct
    public void init() {
        I18nUtils.setMessageSource(messageSource(i18nProperties()));
    }

    /**
     * 创建 localeResolver
     * @param properties 参数
     * @return localeResolver
     */
    @Bean
    @ConditionalOnMissingBean(LocaleResolver.class)
    public LocaleResolver localeResolver(I18nProperties properties) {
        final AcceptHeaderAndUserLocaleResolver localeResolver =  new AcceptHeaderAndUserLocaleResolver();
        localeResolver.setDefaultLocale(properties.getDefaultLocale());
        return localeResolver;
    }

    /**
     * 创建 ResourceCache
     * @return MemoryResourceCache
     */
    @Bean
    @ConditionalOnMissingBean(ResourceCache.class)
    public ResourceCache memoryResourceCache() {
        return new MemoryResourceCache();
    }

    /**
     * 创建messageFormat
     * @return MessageFormat
     */
    @Bean
    @ConditionalOnMissingBean(MessageFormat.class)
    public MessageFormat messageFormat() {
        return new DefaultMessageFormat();
    }

    /**
     * 创建 ResourceReader
     * @param properties 参数
     * @return ResourceReader
     */
    @Bean
    @ConditionalOnMissingBean(ResourceReader.class)
    public ResourceReader resourceReader(I18nProperties properties) {
        final ResourceBundleResourceReader resourceReader = new ResourceBundleResourceReader();
        resourceReader.setEncoding(properties.getEncoding());
        if (!StringUtils.isEmpty(properties.getBasename())) {
            resourceReader.addBasename(StringUtils.commaDelimitedListToSet(StringUtils.trimAllWhitespace(properties.getBasename())));
        }
        return resourceReader;
    }


    /**
     * 创建message Source
     * @param properties 参数
     * @return MessageSource
     */
    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource(I18nProperties properties) {
        AutoReloadMessageSource autoReloadMessageSource =  new AutoReloadMessageSource();
        autoReloadMessageSource.setDuration(properties.getCache().getDuration());
        return autoReloadMessageSource;
    }
}
