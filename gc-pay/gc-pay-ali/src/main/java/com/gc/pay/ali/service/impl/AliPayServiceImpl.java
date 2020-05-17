package com.gc.pay.ali.service.impl;

import com.gc.common.base.utils.BeanUtils;
import com.gc.common.base.utils.JsonUtils;
import com.gc.pay.ali.core.parameter.AliPayCommonParameter;
import com.gc.pay.ali.core.parameter.WapPayParameter;
import com.gc.pay.ali.service.AliPayService;
import com.google.common.collect.Maps;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.RsaKit;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author shizhongming
 * 2020/5/16 7:09 下午
 */
@Service
public class AliPayServiceImpl implements AliPayService {

    private static final String SIGN_KEY = "sign";

    /**
     * 创建wapPay URL
     * @param commonParameter
     * @param wapPayParameter
     * @param extraParameter
     * @return
     */
    @SneakyThrows
    @Override
    public String wapPay(@NonNull AliPayCommonParameter commonParameter, @NonNull WapPayParameter wapPayParameter, Map<String, String> extraParameter) {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfigKit.getAliPayApiConfig();
        Map<String, Object> payParameter = BeanUtils.beanToMap(wapPayParameter);
        if (!Objects.isNull(extraParameter)) {
            payParameter.putAll(extraParameter);
        }
        commonParameter.setBizContent(JsonUtils.toJsonString(payParameter));
        // 将参数转为map
        Map<String, String> parameter = Maps.newHashMap();
        BeanUtils.beanToMap(commonParameter).forEach((key, value) -> {
            if (!Objects.isNull(value)) {
                parameter.put(key, value.toString());
            }
        });
        // 参数转换
        String content = PayKit.createLinkString(parameter);
        // 生成签名
        String encrypt = RsaKit.encryptByPrivateKey(content, aliPayApiConfig.getPrivateKey());
        parameter.put(SIGN_KEY, encrypt);
        return aliPayApiConfig.getServiceUrl() + "?" + PayKit.createLinkString(parameter, true);
    }
}
