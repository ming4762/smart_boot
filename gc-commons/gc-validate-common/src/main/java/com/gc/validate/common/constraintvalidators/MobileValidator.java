package com.gc.validate.common.constraintvalidators;

import com.gc.validate.common.constraints.Mobile;
import com.gc.validate.common.utils.ValidateorUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号校验器
 * TODO 扩展：支持自定义正则表达式校验
 * @author shizhongming
 * 2020/6/2 1:53 下午
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {


    @Override
    public void initialize(Mobile constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return ValidateorUtils.checkMobile(value);
    }
}
