package com.gc.starter.crud;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.gc.starter.crud.utils.SnowFlake53BitGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MybatisPlusProperties properties;

    @PostConstruct
    public void init() {
        log.info("=========== crud 自动配置生效了 ===========");
    }

    /**
     * 初始化mybatis 配置
     * @author zhongming
     */
    private void initMybatisPlusConfig() {
        // TODO: 配置文件设置workerId
        this.properties.getGlobalConfig().setIdentifierGenerator(new SnowFlake53BitGenerator());
    }

}
