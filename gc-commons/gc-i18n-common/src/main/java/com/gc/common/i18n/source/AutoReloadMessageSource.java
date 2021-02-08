package com.gc.common.i18n.source;

import com.google.common.collect.Maps;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * 自动加载的MessageSource
 * @author ShiZhongMing
 * 2021/2/8 10:41
 * @since 1.0
 */
public class AutoReloadMessageSource extends DefaultMessageSource implements InitializingBean {

    @Setter
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration duration = Duration.ofSeconds(3600);

    /**
     * 资源读取时间存储
     */
    private static final ConcurrentMap<Locale, LocalDateTime> READ_TIME_CACHE = Maps.newConcurrentMap();

    @Override
    protected boolean cacheValid(Locale locale) {
        // 判断有没有缓存
        boolean isValid = super.cacheValid(locale);
        if (!isValid) {
            return false;
        }
        // 获取上次添加的时间
        final LocalDateTime readTime = READ_TIME_CACHE.get(locale);
        if (Objects.isNull(readTime)) {
            return false;
        }
        final LocalDateTime currentTime = LocalDateTime.now();
        // 计算时间差
        final Duration between = Duration.between(readTime, currentTime);
        return between.getSeconds() <= this.duration.getSeconds();
    }

    @Override
    protected Map<String, String> readMessage(Locale locale) throws IOException {
        Map<String, String> messages = super.readMessage(locale);
        // 设置读取时间
        READ_TIME_CACHE.put(locale, LocalDateTime.now());
        return messages;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.duration, "AutoReloadMessageSource must set duration");
    }
}
