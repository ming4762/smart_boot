package com.gc.pay.ali.constants;

import lombok.Getter;

/**
 * @author shizhongming
 * 2020/5/16 6:59 下午
 */
public enum  AliPayMethodConstants {

    /**
     * WEB支付
     */
    WAP_PAY("alipay.trade.wap.pay");

    @Getter
    private final String value;

    AliPayMethodConstants(String value) {
        this.value = value;
    }
}
