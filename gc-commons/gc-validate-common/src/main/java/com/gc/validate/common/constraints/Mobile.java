package com.gc.validate.common.constraints;

import com.gc.validate.common.constraintvalidators.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义手机校验
 * @author shizhongming
 * 2020/6/2 1:50 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = MobileValidator.class)
public @interface Mobile {

    String message() default "手机号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
