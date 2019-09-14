package com.smart.starter.crud

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

/**
 * 自动配置类
 * @author ming
 * 2019/9/14 下午6:22
 */
@Configuration
@ComponentScan
class CrudAutoConfiguration {

    @PostConstruct
    fun test () {
        println("=========== crud 自动配置生效了 ===========")
    }
}