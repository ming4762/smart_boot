package com.gc.pay.ali;

import com.ijpay.alipay.AliPayApiConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * 支付宝支付配置
 * @author shizhongming
 * 2020/5/15 5:32 下午
 */
@Getter
@Setter
@ConfigurationProperties("gc.pay.ali")
public class AliPayProperties {

    private String appId;
    private String privateKey;
    private String publicKey;
    private String appCertPath;
    private String aliPayCertPath;
    private String aliPayRootCertPath;
    private String serverUrl;
    private String domain;

    /**
     * 加密类型
     */
    private String signType;

    /**
     * 是否使用公钥证书
     */
    private Boolean userCsr;

    @SneakyThrows
    public AliPayApiConfig createAliPayApiConfig() {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(this.getAppId())
                .setAliPayPublicKey(this.getPublicKey())
                .setAppCertPath(this.getAppCertPath())
                .setSignType(this.signType)
                .setAliPayCertPath(this.getAliPayCertPath())
                .setAliPayRootCertPath(this.getAliPayRootCertPath())
                .setCharset("UTF-8")
                .setPrivateKey(this.getPrivateKey())
                .setServiceUrl(this.getServerUrl());
        if (Objects.equals(this.userCsr, Boolean.TRUE)) {
            return aliPayApiConfig.buildByCert();
        } else {
            return aliPayApiConfig.build();
        }
    }
}
