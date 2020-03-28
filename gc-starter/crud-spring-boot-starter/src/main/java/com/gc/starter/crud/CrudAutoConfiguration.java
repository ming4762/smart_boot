package com.gc.starter.crud;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author shizhongming
 * 2020/1/12 9:05 下午
 */
@Configuration
@ComponentScan
@Slf4j
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class CrudAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("=========== crud 自动配置生效了 ===========");
    }

}
