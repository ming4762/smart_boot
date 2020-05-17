package com.gc.pay.ali.controller;

import com.gc.common.base.message.Result;
import com.gc.pay.ali.constants.AliPayMethodConstants;
import com.gc.pay.ali.core.parameter.AliPayCommonParameter;
import com.gc.pay.ali.core.parameter.WapPayParameter;
import com.gc.pay.ali.pojo.dto.WapPayDTO;
import com.gc.pay.ali.service.AliPayService;
import com.google.common.collect.Maps;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author shizhongming
 * 2020/5/15 6:05 下午
 */
@RestController
@RequestMapping("aliPay")
public class AliPayController {

    private final AliPayService aliPayService;

    public AliPayController(AliPayService aliPayService) {
        this.aliPayService = aliPayService;
    }

    @PostMapping("wapPay")
    public Result<String> wapPay(@RequestBody @Valid WapPayDTO parameter, HttpServletResponse response) throws IOException {
        // 获取配置信息
        AliPayApiConfig aliPayApiConfig = AliPayApiConfigKit.getAliPayApiConfig();
        // 创建通用参数
        AliPayCommonParameter aliPayCommonParameter = AliPayCommonParameter.createFromConfig(aliPayApiConfig, AliPayMethodConstants.WAP_PAY);
        // 创建支付参数
        WapPayParameter wapPayParameter = WapPayParameter.builder()
                .totalAmount(parameter.getTotalAmount())
                .subject(parameter.getSubject())
                .outTradeNo(System.currentTimeMillis() + "")
                .build();
        // 创建额外参数
        Map<String, String> extraParameter = Maps.newHashMap();
        if (Objects.isNull(parameter.getBody())) {
            extraParameter.put("body", parameter.getBody());
        }
        String url = this.aliPayService.wapPay(aliPayCommonParameter, wapPayParameter, extraParameter);
//        response.sendRedirect(url);
        return Result.success(url);
    }
}
