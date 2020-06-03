package com.gc.validate.common.utils;

import java.util.regex.Pattern;

/**
 * 校验工具类
 * @author shizhongming
 * 2020/6/2 2:05 下午
 */
public final class ValidatorUtils {

    private ValidatorUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 校验手机号
     * @param mobile 手机号
     * @return 校验结果
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }

    /**
     * 验证固定电话号码
     * @param phone 电话号码
     * @return 校验结果
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }
}
