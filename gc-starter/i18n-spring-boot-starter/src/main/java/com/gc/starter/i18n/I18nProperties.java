package com.gc.starter.i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.convert.DurationUnit;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * @author shizhongming
 * 2020/5/12 5:56 下午
 */
@Getter
@Setter
public class I18nProperties {

    /**
     * 默认的Locale
     */
    private Locale defaultLocale;

    private String basename;

    private Charset encoding = StandardCharsets.UTF_8;

    private Cache cache = new Cache();

    @Getter
    @Setter
    public static class Cache {

        @DurationUnit(ChronoUnit.SECONDS)
        private Duration duration = Duration.ofSeconds(3600);
    }
}
