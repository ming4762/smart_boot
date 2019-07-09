package com.smart.system.config

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
 * 系统模块数据库事物
 * @author ming
 * 2019/7/5 上午10:27
 */
@MapperScan("com.smart.system.mapper", sqlSessionTemplateRef = "systemSqlSessionTemplate")
@Configuration
class SystemDataSourceMybatisConfig {

    /**
     * 定时任务库数据源
     * @return 数据源
     */
    @Bean(name = ["systemDataSource"])
    @ConfigurationProperties("spring.datasource.system")
    fun dataSourceQuartz(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

    /**
     * 创建数据库事物
     * @param dataSource 系统数据源
     * @return 事物类
     */
    @Bean(name = ["systemTransactionManager"])
    fun quartzTransactionManager(@Qualifier("systemDataSource") dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean(name = ["systemSqlSessionFactory"])
    @Throws(Exception::class)
    fun sqlSessionFactory(@Qualifier("systemDataSource") dataSource: DataSource): SqlSessionFactory? {
        val sqlSessionFactoryBean = MybatisSqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setMapperLocations(matchMapperLocations())
        sqlSessionFactoryBean.setPlugins(arrayOf(this.createPageHelperPlugins()))
        return sqlSessionFactoryBean.getObject()
    }

    @Throws(IOException::class)
    private fun matchMapperLocations(): Array<Resource> {
        val resolver = PathMatchingResourcePatternResolver()
        return resolver.getResources("classpath*:mybatis/system/*.xml")
    }

    /**
     * 创建SqlSessionTemplate
     * @param sqlSessionFactory sqlSessionFactory
     * @return 系统库SqlSessionTemplate
     */
    @Bean(name = ["systemSqlSessionTemplate"])
    fun quartzSqlSessionTemplate(@Qualifier("systemSqlSessionFactory") sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
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