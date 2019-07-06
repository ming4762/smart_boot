package com.smart.quartz.config

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean
import com.github.pagehelper.PageInterceptor
import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionTemplate
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import java.io.IOException
import java.util.*
import javax.sql.DataSource

/**
 * 数据源、mybatis配置
 * @author ming
 * 2019/7/4 下午4:38
 */
@Configuration
@MapperScan("com.smart.quartz.mapper", sqlSessionTemplateRef = "quartzSqlSessionTemplate")
class QuartzDataSourceMybatisConfig {

    /**
     * 定时任务库数据源
     * @return 数据源
     */
    @Bean(name = ["quartzDataSource"])
    @ConfigurationProperties("spring.datasource.quartz")
    fun dataSourceQuartz(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

    /**
     * 创建数据库事物
     * @param dataSource 系统数据源
     * @return 事物类
     */
    @Bean(name = ["quartzTransactionManager"])
    fun quartzTransactionManager(@Qualifier("quartzDataSource") dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean(name = ["quartzSqlSessionFactory"])
    @Throws(Exception::class)
    fun sqlSessionFactory(@Qualifier("quartzDataSource") dataSource: DataSource): SqlSessionFactory? {
        val sqlSessionFactoryBean = MybatisSqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setMapperLocations(matchMapperLocations())
        sqlSessionFactoryBean.setPlugins(arrayOf(this.createPageHelperPlugins()))
        return sqlSessionFactoryBean.getObject()
    }

    @Throws(IOException::class)
    private fun matchMapperLocations(): Array<Resource> {
        val resolver = PathMatchingResourcePatternResolver()
        return resolver.getResources("classpath*:mybatis/quartz/*.xml")
    }

    /**
     * 创建SqlSessionTemplate
     * @param sqlSessionFactory sqlSessionFactory
     * @return 系统库SqlSessionTemplate
     */
    @Bean(name = ["quartzSqlSessionTemplate"])
    fun quartzSqlSessionTemplate(@Qualifier("quartzSqlSessionFactory") sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(sqlSessionFactory)
    }

    /**
     * 创建分页插件
     */
    private fun createPageHelperPlugins(): Interceptor {
        val interceptor = PageInterceptor()
        // 设置参数
        val properties = Properties()
        properties.setProperty("helperDialect", "mysql")
        interceptor.setProperties(properties)
        return interceptor
    }
}