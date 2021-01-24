package com.gc.common.i18n.exception;

import com.gc.common.base.exception.BaseException;
import com.gc.common.base.message.I18nMessage;
import lombok.Getter;

/**
 * 支持I18n 的异常信息
 * @author shizhongming
 * 2021/1/24 10:39 上午
 */
@Getter
public class I18nException extends BaseException {
    private static final long serialVersionUID = 7336029237493468850L;

    private I18nMessage i18nMessage;

    public I18nException(Throwable e) {
        super(e);
    }

    public I18nException(I18nMessage i18nMessage, Throwable e) {
        super(e);
        this.i18nMessage = i18nMessage;
    }

    public I18nException(Integer code, I18nMessage i18nMessage, Throwable e) {
        super(code, null, e);
        this.i18nMessage = i18nMessage;
    }
}
