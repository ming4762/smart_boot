package com.gc.pay.ali.pojo.dto;

import com.gc.common.base.annotation.MapKey;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2020/5/16 7:11 下午
 */
@Getter
@Setter
@ToString
public class WapPayDTO implements Serializable {
    private static final long serialVersionUID = 584240723736540791L;

    @MapKey("total_amount")
    @NotNull(message = "请输入金额")
    private String totalAmount;

    private String body;

    private String subject;

    private String outTradeNo;
}
