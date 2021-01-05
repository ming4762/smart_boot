package com.gc.auth.core.authentication;

import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/1/5 11:48
 * @since 1.0
 */
public abstract class AbstractUrlAuthenticationProvider implements UrlAuthenticationProvider {

    private String beanName;

    @Override
    @NonNull
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(@NonNull String s) {
        this.beanName = s;
    }
}
