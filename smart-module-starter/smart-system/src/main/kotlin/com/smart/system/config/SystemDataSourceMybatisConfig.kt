package com.smart.system.config

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean
import com.github.pagehelper.PageInterceptor
import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.IOException
import java.util.*
import javax.sql.DataSource

/**
 * 系统模块数据库事物
 * @author ming
 * 2019/7/5 上午10:27
 */
@MapperScan("com.smart.system.mapper", sqlSessionFactoryRef = "systemSqlSessionFactory")
@Configuration
class SystemDataSourceMybatisConfig {

    /**
     * 系统模块数据库数据源
     * @return 数据源
     */
    @Bean(name = ["systemDataSource"])
    @ConfigurationProperties("spring.datasource.system")
    fun dataSourceSystem(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

    @Bean(name = ["systemSqlSessionFactory"])
    @Throws(Exception::class)
    fun sqlSessionFactory(@Qualifier("systemDataSource") dataSource: DataSource): SqlSessionFactory? {
        val sqlSessionFactoryBean = MybatisSqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setMapperLocations(*matchMapperLocations())
        sqlSessionFactoryBean.setPlugins(this.createPageHelperPlugins())
        return sqlSessionFactoryBean.getObject()
    }

    @Throws(IOException::class)
    private fun matchMapperLocations(): Array<Resource> {
        val resolver = PathMatchingResourcePatternResolver()
        return resolver.getResources("classpath*:mybatis/system/*.xml")
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