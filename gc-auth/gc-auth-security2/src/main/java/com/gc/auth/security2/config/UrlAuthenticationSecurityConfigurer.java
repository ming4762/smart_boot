package com.gc.auth.security2.config;

import com.gc.auth.core.authentication.UrlAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.util.Assert;

/**
 * 动态URL校验
 * @author ShiZhongMing
 * 2021/1/4 16:20
 * @since 1.0
 */
public class UrlAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private UrlAuthenticationSecurityConfigurer() {}

    public static UrlAuthenticationSecurityConfigurer urlAuth() {
        return new UrlAuthenticationSecurityConfigurer();
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        Assert.notNull(this.serviceProvider.authenticationProvider, "authenticationProvider is null, please init it");
        String access = String.format("@%s.hasPermission(request, authentication)", this.serviceProvider.authenticationProvider.getBeanName());
        builder.authorizeRequests()
                .anyRequest()
                .access(access);
    }

    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    public class ServiceProvider {
        private UrlAuthenticationProvider authenticationProvider;

        public ServiceProvider authenticationProvider(UrlAuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;
            return this;
        }

        public UrlAuthenticationSecurityConfigurer and() {
            return UrlAuthenticationSecurityConfigurer.this;
        }
    }
}
