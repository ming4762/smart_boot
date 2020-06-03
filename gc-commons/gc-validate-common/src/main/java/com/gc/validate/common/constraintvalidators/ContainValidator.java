package com.gc.validate.common.constraintvalidators;

import com.gc.validate.common.constraints.Contain;
import com.gc.validate.common.enums.IEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 是否包含验证器
 * @author shizhongming
 * 2020/6/2 4:41 下午
 */
public class ContainValidator implements ConstraintValidator<Contain, Object> {

    private String[] allow;

    private Class<? extends IEnum> allowClass;

    @Override
    public void initialize(Contain constraintAnnotation) {
        this.allow = constraintAnnotation.allow();
        this.allowClass = constraintAnnotation.allowClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return this.checkAllowClass(value) && this.checkAllow(value);
    }

    /**
     * 判断是否在allow里
     * @param value
     * @return
     */
    private boolean checkAllow(Object value) {
        final List<String> allowList = Arrays.asList(allow);
        return allowList.isEmpty() || allowList.contains(value.toString());
    }

    /**
     * 检查是否在包含的枚举内
     * @param value
     * @return
     */
    private boolean checkAllowClass(Object value) {
        if (Objects.equals(this.allowClass, IEnum.class)) {
            return true;
        }
        IEnum[] enums = this.allowClass.getEnumConstants();
        return Arrays.stream(enums).anyMatch(item -> Objects.equals(item.getValue(), value));
    }
}
