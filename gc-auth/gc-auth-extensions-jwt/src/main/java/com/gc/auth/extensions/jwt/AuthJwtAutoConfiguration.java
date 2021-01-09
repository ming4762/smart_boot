package com.gc.auth.extensions.jwt;

import com.gc.auth.extensions.jwt.config.AuthJwtBeanConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ShiZhongMing
 * 2021/1/8 15:58
 * @since 1.0
 */
@Configuration
@Import(AuthJwtBeanConfig.class)
public class AuthJwtAutoConfiguration {
}
