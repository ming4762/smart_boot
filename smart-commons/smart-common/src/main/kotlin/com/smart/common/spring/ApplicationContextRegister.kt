package com.smart.common.spring

import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * 容器注册类
 * @author ming
 * 2019/7/6 下午8:24
 */
@ConditionalOnMissingBean(ApplicationContextRegister::class)
@Component
class ApplicationContextRegister : ApplicationContextAware {


    companion object {
        private val LOGGER = LoggerFactory.getLogger(ApplicationContextRegister::class.java)
        private lateinit var applicationContext: ApplicationContext

        /**
         * 通过名字获取bean
         * @param name
         * @return
         */
        fun getBean(name: String): Any? {
            return applicationContext.getBean(name)
        }

        fun <T> getBean(type: Class<T>): T? {
            return applicationContext.getBean(type)
        }

        fun getContext(): ApplicationContext {
            return applicationContext
        }
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        LOGGER.debug("ApplicationContext registed-->{}", applicationContext)
        Companion.applicationContext = applicationContext
    }


}