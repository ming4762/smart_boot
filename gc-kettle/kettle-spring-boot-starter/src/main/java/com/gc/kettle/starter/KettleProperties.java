package com.gc.kettle.starter;


import com.gc.kettle.actuator.properties.DatabaseMetaProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * kettle 配置参数
 * @author shizhongming
 * 2020/7/16 8:10 下午
 */
@ConfigurationProperties("gc.kettle")
@Getter
@Setter
public class KettleProperties {

    /**
     * 数据库资源库连接池
     */
    private DatabaseMetaProperties repository = new DatabaseMetaProperties();

}
