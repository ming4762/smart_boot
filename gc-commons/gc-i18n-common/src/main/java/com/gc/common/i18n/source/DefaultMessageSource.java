package com.gc.common.i18n.source;

import com.gc.common.i18n.cache.ResourceCache;
import com.gc.common.i18n.format.MessageFormat;
import com.gc.common.i18n.reader.ResourceReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Locale;
import java.util.Map;

/**
 * @author shizhongming
 * 2021/2/1 11:24 下午
 */
public class DefaultMessageSource implements MapArgsMessageSource {

    private ResourceCache resourceCache;

    private MessageFormat messageFormat;

    private ResourceReader resourceReader;

    @Override
    @NonNull
    public String getMessage(String code, Map<String, Object> args, String defaultMessage, Locale locale) {
        final String message = this.doGetMessage(code, locale, defaultMessage);
        return messageFormat.format(message, args);
    }

    @Override
    @NonNull
    public String getMessage(@NonNull String code, Object[] args, String defaultMessage, @Nullable Locale locale) {
        final String message = this.doGetMessage(code, locale, defaultMessage);
        return messageFormat.format(message, args);
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

    private String doGetMessage(String code, Locale locale, String defaultMessage) {
        // 从缓存获取
        String message = this.resourceCache.get(locale, code);
        if (StringUtils.isNotBlank(message)) {
            return message;
        }
        // 缓存没有读到 从资源库读
        final Map<String, String> messages = this.resourceReader.read(locale);
        if (!this.resourceCache.contain(locale)) {
            // 设置缓存
            this.resourceCache.putAll(locale, messages);
        }
        message = messages.get(code);
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
}
