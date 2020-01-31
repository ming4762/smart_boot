package com.gc.common.auth.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * auth配置参数
 * @author jackson
 * 2020/1/23 9:25 上午
 */
@ConfigurationProperties(prefix = "gc.auth")
@Getter
@Setter
public class AuthProperties {

    private Session session = new Session();

    private IgnoreConfig ignores = new IgnoreConfig();

    /**
     * 是否是开发模式
     */
    private Boolean development = Boolean.FALSE;

    @Getter
    @Setter
    public static class Session {
        private Timeout timeout = new Timeout();
    }

    @Getter
    @Setter
    public static class Timeout {
        // 默认 30分钟
        private Integer global = 1800;
        // 默认30天
        private Integer mobile = 2592000;
    }

    /**
     * 忽略的请求设置
     */
    @Getter
    @Setter
    public static class IgnoreConfig {

        /**
         * 需要忽略的 URL 格式，不考虑请求方法
         */
        private List<String> pattern = Lists.newArrayList("/public/**");

        /**
         * 需要忽略的 GET 请求
         */
        private List<String> get = Lists.newArrayList();

        /**
         * 需要忽略的 POST 请求
         */
        private List<String> post = Lists.newArrayList();

        /**
         * 需要忽略的 DELETE 请求
         */
        private List<String> delete = Lists.newArrayList();

        /**
         * 需要忽略的 PUT 请求
         */
        private List<String> put = Lists.newArrayList();

        /**
         * 需要忽略的 HEAD 请求
         */
        private List<String> head = Lists.newArrayList();

        /**
         * 需要忽略的 PATCH 请求
         */
        private List<String> patch = Lists.newArrayList();

        /**
         * 需要忽略的 OPTIONS 请求
         */
        private List<String> options = Lists.newArrayList();

        /**
         * 需要忽略的 TRACE 请求
         */
        private List<String> trace = Lists.newArrayList();
    }
}
