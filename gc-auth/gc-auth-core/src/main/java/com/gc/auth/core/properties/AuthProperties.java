package com.gc.auth.core.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

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

    private String jwtKey = "gc-it";

    private boolean jwt = false;

    private boolean urlCheck = false;

    private Session session = new Session();

    private Saml2 saml2 = new Saml2();

    private IgnoreConfig ignores = new IgnoreConfig();

    /**
     * 是否是开发模式
     */
    @NonNull
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
        private Long global = 1800L;
        // 默认30天
        private Long mobile = 2592000L;
        // 默认7天
        private Long remember = 604800L;
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

        public List<String> getPattern() {
            String publicStr = "/public/**";
            if (!pattern.contains(publicStr)) {
                pattern.add(publicStr);
            }
            return pattern;
        }

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

    /**
     * SAML配置
     */
    @Getter
    @Setter
    public static class Saml2 {

        private String entityId;

        private KeyStore key = new KeyStore();

        private Identity identity = new Identity();

        /**
         * 重试次数
         */
        private Integer retry = 5;

        /**
         * key 配置
         */
        @Getter
        @Setter
        public static class KeyStore {
            private String name;

            private String password;

            private String filePath;
        }

        @Getter
        @Setter
        public static class Identity {
            private Boolean discoveryEnabled = Boolean.TRUE;

            private String metadataFilePath;
        }
    }
}
