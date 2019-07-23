package com.smart.auth.config

import com.smart.auth.shiro.CustomAuthorizationAttributeSourceAdvisor
import com.smart.auth.shiro.OptionsAdoptFilter
import com.smart.auth.shiro.StatelessRealm
import com.smart.auth.shiro.TokenSessionManager
import com.smart.shiro.redis.cache.RedisCacheManager
import com.smart.shiro.redis.session.RedisSessionDAO
import com.smart.starter.redis.service.RedisService
import org.apache.shiro.cache.CacheManager
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.session.mgt.SessionManager
import org.apache.shiro.session.mgt.eis.SessionDAO
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.util.StringUtils
import javax.servlet.Filter

/**
 * shiro 配置类
 * @author ming
 * 2019/6/21 下午1:26
 */
@Configuration
class ShiroConfiguration {

    @Value("\${smart.auth.filterChainDefinitionList:}")
    private var filterChainDefinitionList: String? = null

    // 缓存类型
    @Value("\${smart.auth.cacheType:redis}")
    private lateinit var cacheType: String

    // 是否是开发模式
    @Value("\${smart.auth.development:false}")
    private var development: Boolean = false

    /**
     * 创建shiro拦截器
     */
    @Bean
    fun shiroFilter(securityManager: SecurityManager): ShiroFilterFactoryBean {
        val shiroFilterFactoryBean = ShiroFilterFactoryBean()
        shiroFilterFactoryBean.securityManager = securityManager
        // 设置自定义拦截器
        val filters = mutableMapOf<String, Filter>()
        filters["optionsAdoptFilter"] = OptionsAdoptFilter()
        shiroFilterFactoryBean.filters = filters
        // 设置拦截器链
        if (this.development) {
            // 开发模式不拦截请求
            shiroFilterFactoryBean.filterChainDefinitionMap = linkedMapOf("/**" to "anon")
        } else {
            shiroFilterFactoryBean.filterChainDefinitionMap = this.createFilterChainDefinitionMap()
        }
        return shiroFilterFactoryBean
    }

    /**
     * 配置StatelessRealm
     */
    @Bean
    fun statelessRealm() = StatelessRealm()


    /**
     * 配置核心事物管理器
     */
    @Bean
    fun securityManager(@Autowired @Lazy redisService: RedisService, @Autowired sessionDAO: SessionDAO): SecurityManager {
        val securityManager = DefaultWebSecurityManager()
        securityManager.setRealm(statelessRealm())
        // 使用自定义session 管理器
        securityManager.sessionManager = sessionManager(sessionDAO)
        securityManager.cacheManager = createRedisCacheManager(redisService)
        return securityManager
    }

    /**
     * 配置sessionmanager
     */
    fun sessionManager(sessionDAO: SessionDAO): SessionManager {
        val sessionManager = TokenSessionManager()
        sessionManager.sessionDAO = sessionDAO
        return sessionManager
    }

    @Bean
    fun sessionDAO(@Autowired @Lazy redisService: RedisService): SessionDAO {
        return RedisSessionDAO(redisService)
    }



    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    fun authorizationAttributeSourceAdvisor(securityManager: SecurityManager): AuthorizationAttributeSourceAdvisor {
        val advisor = CustomAuthorizationAttributeSourceAdvisor(this.development)
        advisor.securityManager = securityManager
//        val authorizationAttributeSourceAdvisor = AuthorizationAttributeSourceAdvisor()
//        authorizationAttributeSourceAdvisor.securityManager = securityManager
        return advisor
    }

    @Bean
    fun advisorAutoProxyCreator(): DefaultAdvisorAutoProxyCreator {
        val advisorAutoProxyCreator = DefaultAdvisorAutoProxyCreator()
        advisorAutoProxyCreator.isProxyTargetClass = true
        return advisorAutoProxyCreator
    }

    /**
     * 创建shrio拦截器链
     */
    private fun createFilterChainDefinitionMap(): Map<String, String> {
        if (StringUtils.isEmpty(this.filterChainDefinitionList)) return mapOf()

        return this.filterChainDefinitionList!!.split(",")
                .map { it.trim().split(":") }
                .map { it[0].trim() to it[1].trim() }
                .toMap()
    }

    /**
     * 创建缓存管理器
     */
    private fun createRedisCacheManager(redisService: RedisService): CacheManager {
        val cacheManager = RedisCacheManager()
        cacheManager.redisService = redisService
        return cacheManager
    }
}