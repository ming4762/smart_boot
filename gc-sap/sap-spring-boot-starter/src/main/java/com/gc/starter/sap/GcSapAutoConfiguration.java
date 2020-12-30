package com.gc.starter.sap;

import com.gc.starter.sap.jco.JcoProvider;
import com.gc.starter.sap.jco.RfcManager;
import com.gc.starter.sap.service.SapService;
import com.gc.starter.sap.service.SapServiceImpl;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * SAP启动类
 * @author ShiZhongMing
 * 2020/12/25 14:42
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(GcSapProperties.class)
@Slf4j
public class GcSapAutoConfiguration {

    /**
     * 创建RfcManager
     * @param sapProperties 参数
     * @return RfcManager
     */
    @Bean
    @ConditionalOnMissingBean(RfcManager.class)
    public RfcManager rfcManager(GcSapProperties sapProperties) {
        JcoProvider jcoProvider = new JcoProvider();
        // 创建链接参数
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, sapProperties.getJcoAshost());
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, sapProperties.getJcoSysnr());
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, sapProperties.getJcoClient());
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, sapProperties.getJcoUser());
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, sapProperties.getJcoPasswd());
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, sapProperties.getJcoLang());
        connectProperties.setProperty(DestinationDataProvider.JCO_SAPROUTER, sapProperties.getJcoSaprouter());
        RfcManager rfcManager = new RfcManager();
        rfcManager.setProvider(jcoProvider);
        rfcManager.loadProvider("ABAP_AS_POOL", connectProperties);
        log.debug("------------ Rfc Manager init ------------");
        return rfcManager;
    }

    @Bean
    @ConditionalOnMissingBean(SapService.class)
    public SapService sapService(RfcManager rfcManager) {
        return new SapServiceImpl(rfcManager);
    }
}
