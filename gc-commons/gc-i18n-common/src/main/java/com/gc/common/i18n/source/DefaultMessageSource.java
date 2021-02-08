package com.gc.common.i18n.source;

import com.gc.common.i18n.cache.ResourceCache;
import com.gc.common.i18n.format.MessageFormat;
import com.gc.common.i18n.reader.ResourceReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author shizhongming
 * 2021/2/1 11:24 下午
 */
@Slf4j
public class DefaultMessageSource implements MapArgsMessageSource, ReloadableMessageSource {

    private ResourceCache resourceCache;

    private MessageFormat messageFormat;

    private ResourceReader resourceReader;

    @Override
    public String getMessage(String code, Map<String, Object> args, String defaultMessage, Locale locale) {
        try {
            final String message = this.doGetMessage(code, locale, defaultMessage);
            return messageFormat.format(message, args);
        } catch (IOException e) {
            log.warn("读取资源失败", e);
            return null;
        }
    }

    @Override
    public String getMessage(@NonNull String code, Object[] args, String defaultMessage, @Nullable Locale locale) {
        try {
            final String message = this.doGetMessage(code, locale, defaultMessage);
            return messageFormat.format(message, args);
        } catch (IOException e) {
            log.warn("读取资源失败", e);
            return null;
        }
    }

    @NonNull
    @Override
    public String getMessage(@NonNull String code, Object[] args, @Nullable Locale locale) throws NoSuchMessageException {
        return this.getMessage(code, args, null, locale);
    }

    @NonNull
    @Override
    public String getMessage(@NonNull MessageSourceResolvable resolvable, @Nullable Locale locale) throws NoSuchMessageException {
        throw new NoSuchMessageException("no support", locale);
    }

    /**
     * 缓存是否有效
     * @param locale Locale
     * @return 缓存是否有效
     */
    protected boolean cacheValid(Locale locale) {
        return this.resourceCache.contain(locale);
    }

    /**
     * 读取资源
     * @param locale Locale
     * @return 读取的资源信息
     * @throws IOException IOException
     */
    protected Map<String, String> readMessage(Locale locale) throws IOException {
        return this.resourceReader.read(locale);
    }


    /**
     * 获取信息
     * @param code 编码
     * @param locale Locale
     * @param defaultMessage 默认信息
     * @return I18N 信息
     * @throws IOException IOException
     */
    protected String doGetMessage(String code, Locale locale, String defaultMessage) throws IOException {
        // 从缓存获取
        if (this.cacheValid(locale)) {
            return this.resourceCache.get(locale, code);
        }
        // 缓存没有读到 从资源库读
        final Map<String, String> messages = this.readMessage(locale);
        // 设置缓存
        this.resourceCache.putAll(locale, messages);

        String message = messages.get(code);
        if (StringUtils.isNotBlank(message)) {
            return message;
        }
        if (StringUtils.isBlank(defaultMessage)) {
            throw new NoSuchMessageException(code, locale);
        }
        return defaultMessage;
     }

    @Autowired
    public void setResourceCache(ResourceCache resourceCache) {
        this.resourceCache = resourceCache;
    }

    @Autowired
    public void setMessageFormat(MessageFormat messageFormat) {
        this.messageFormat = messageFormat;
    }

    @Autowired
    public void setResourceReader(ResourceReader resourceReader) {
        this.resourceReader = resourceReader;
    }

    @Override
    public void reload(Locale locale) {
        this.resourceCache.remove(locale);
    }

    @Override
    public void reload() {
        this.resourceCache.clear();
    }
}
