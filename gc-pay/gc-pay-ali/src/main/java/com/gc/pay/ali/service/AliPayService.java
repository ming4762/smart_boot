package com.gc.pay.ali.service;

import com.gc.pay.ali.core.parameter.AliPayCommonParameter;
import com.gc.pay.ali.core.parameter.WapPayParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * @author shizhongming
 * 2020/5/16 7:09 下午
 */
public interface AliPayService {

    String wapPay(@NonNull AliPayCommonParameter commonParameter, @NonNull WapPayParameter wapPayParameter, @Nullable Map<String, String> extraParameter);
}
