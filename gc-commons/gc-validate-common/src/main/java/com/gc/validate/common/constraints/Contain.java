package com.gc.validate.common.constraints;

import com.gc.validate.common.constraintvalidators.ContainValidator;
import com.gc.validate.common.enums.IEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 判断是否包含验证器
 * @author shizhongming
 * 2020/6/2 4:39 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = ContainValidator.class)
public @interface Contain {

    String message() default "";

    String[] allow() default {};

    Class<? extends IEnum> allowClass() default IEnum.class;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
