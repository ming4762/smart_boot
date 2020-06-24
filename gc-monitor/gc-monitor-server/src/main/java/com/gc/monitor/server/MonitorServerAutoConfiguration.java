package com.gc.monitor.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 监控服务启动类
 * @author shizhongming
 * 2020/5/7 9:35 上午
 */
@Configuration
@EnableConfigurationProperties(MonitorServerProperties.class)
@EnableAdminServer
public class MonitorServerAutoConfiguration {
}
