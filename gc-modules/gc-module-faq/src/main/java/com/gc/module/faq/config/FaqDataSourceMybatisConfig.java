package com.gc.module.faq.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.gc.common.base.constants.TransactionManagerConstants;
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
 * @author shizhongming
 * 2020/4/11 11:39 上午
 */
@Configuration
@MapperScan(value = "com.gc.module.faq.mapper", sqlSessionTemplateRef = "faqSqlSessionTemplate")
public class FaqDataSourceMybatisConfig {


    @Bean(name = "faqDataSource")
    @ConfigurationProperties("spring.datasource.faq")
    public DataSource faqDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean("faqSqlSessionFactory")
    public SqlSessionFactory faqSqlSessionFactory(@Qualifier("faqDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        mybatisSqlSessionFactoryBean.setMapperLocations(matchMapperLocations());
        mybatisSqlSessionFactoryBean.setPlugins(this.createPageHelperPlugins());
        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Bean("faqSqlSessionTemplate")
    public SqlSessionTemplate faqSqlSessionTemplate(@Qualifier("faqSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = TransactionManagerConstants.FAQ_MANAGER)
    public DataSourceTransactionManager faqTransactionManager(@Qualifier("faqDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private Resource[] matchMapperLocations() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/faq/*.xml");
    }

    private Interceptor createPageHelperPlugins() {
        final PageInterceptor interceptor = new PageInterceptor();
        final Properties properties = new Properties();
        interceptor.setProperties(properties);
        return interceptor;
    }
}
