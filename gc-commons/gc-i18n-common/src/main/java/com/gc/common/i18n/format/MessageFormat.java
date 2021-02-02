package com.gc.common.i18n.format;

import java.util.Map;

/**
 * i18n格式化
 * @author shizhongming
 * 2021/2/1 11:16 下午
 */
public interface MessageFormat {

    /**
     * 格式化I18N message
     * @param formValue  formValue
     * @param args args
     * @return I18N message
     */
    String format(String formValue, Map<String, Object> args);

    /**
     * 格式化I18N message
     * @param formValue  formValue
     * @param args args
     * @return I18N message
     */
    String format(String formValue, Object[] args);
}
