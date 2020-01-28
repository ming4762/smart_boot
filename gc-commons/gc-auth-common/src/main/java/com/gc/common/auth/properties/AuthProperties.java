package com.gc.common.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

    private String permitAll;

    /**
     * 是否是开发模式
     */
    private Boolean development = Boolean.FALSE;

    @Getter
    @Setter
    public class Session {
        private Timeout timeout = new Timeout();
    }

    @Getter
    @Setter
    public class Timeout {
        // 默认 30分钟
        private Integer global = 1800;
        // 默认30天
        private Integer mobile = 2592000;
    }
}
