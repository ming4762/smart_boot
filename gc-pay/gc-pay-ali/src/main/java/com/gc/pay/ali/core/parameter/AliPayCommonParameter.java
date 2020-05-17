package com.gc.pay.ali.core.parameter;

import cn.hutool.core.date.DateUtil;
import com.gc.common.base.annotation.MapKey;
import com.gc.pay.ali.constants.AliPayMethodConstants;
import com.ijpay.alipay.AliPayApiConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付宝支付公共参数
 * @author shizhongming
 * 2020/5/16 7:20 下午
 */
@Getter
@Setter
public class AliPayCommonParameter implements Serializable {
    private static final long serialVersionUID = -5230205048421736860L;

    @MapKey("app_id")
    private String appId;

    private String method;

    private String charset;

    @MapKey("sign_type")
    private String signType;

    private String timestamp;

    private String version = "1.0";

    @MapKey("biz_content")
    private String bizContent;

    private String sign;

    /**
     * 通过配置创建通用参数
     * @param config
     * @param method
     * @return
     */
    public static AliPayCommonParameter createFromConfig(@NonNull AliPayApiConfig config, @NonNull AliPayMethodConstants method) {
        AliPayCommonParameter payCommonParameter = new AliPayCommonParameter();
        payCommonParameter.setAppId(config.getAppId());
        payCommonParameter.setMethod(method.getValue());
        payCommonParameter.setCharset(config.getCharset());
        payCommonParameter.setSignType(config.getSignType());
        payCommonParameter.setTimestamp(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return payCommonParameter;
    }

}
