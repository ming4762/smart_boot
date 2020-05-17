package com.gc.pay.ali.core.parameter;

import com.gc.common.base.annotation.MapKey;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shizhongming
 * 2020/5/16 8:53 下午
 */
@Getter
@Setter
@ToString
@Builder
public class WapPayParameter implements Serializable {
    private static final long serialVersionUID = -4649473866524895403L;

    @MapKey("total_amount")
    private String totalAmount;

    private String subject;

    @MapKey("out_trade_no")
    private String outTradeNo;
}
