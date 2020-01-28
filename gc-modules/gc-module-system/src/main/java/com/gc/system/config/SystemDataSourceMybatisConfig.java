package com.gc.system.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * 系统模块数据源配置
 * @author jackson
 * 2020/1/22 9:32 上午
 */
@MapperScan(value = "com.gc.system.mapper", sqlSessionTemplateRef = "systemSqlSessionTemplate")
@Configuration
public class SystemDataSourceMybatisConfig {

    @Bean(name = "systemDataSource")
    @ConfigurationProperties("spring.datasource.system")
    public DataSource systemDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("systemSqlSessionFactory")
    public SqlSessionFactory systemSqlSessionFactory(@Qualifier("systemDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        mybatisSqlSessionFactoryBean.setMapperLocations(matchMapperLocations());
        mybatisSqlSessionFactoryBean.setPlugins(this.createPageHelperPlugins());
        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Bean("systemSqlSessionTemplate")
    public SqlSessionTemplate systemSqlSessionTemplate(@Qualifier("systemSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "systemTransactionManager")
    public DataSourceTransactionManager systemTransactionManager(@Qualifier("systemDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private Resource[] matchMapperLocations() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/system/*.xml");
    }

    private Interceptor createPageHelperPlugins() {
        final PageInterceptor interceptor = new PageInterceptor();
        final Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        interceptor.setProperties(properties);
        return interceptor;
    }

}
