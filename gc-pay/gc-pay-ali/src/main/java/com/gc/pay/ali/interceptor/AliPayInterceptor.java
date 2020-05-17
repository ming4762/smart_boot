package com.gc.pay.ali.interceptor;

import com.gc.pay.ali.AliPayProperties;
import com.ijpay.alipay.AliPayApiConfigKit;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shizhongming
 * 2020/5/15 5:59 下午
 */
public class AliPayInterceptor implements HandlerInterceptor {

    private final AliPayProperties payProperties;

    public AliPayInterceptor(AliPayProperties payProperties) {
        this.payProperties = payProperties;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(this.payProperties.createAliPayApiConfig());
        return true;
    }
}
