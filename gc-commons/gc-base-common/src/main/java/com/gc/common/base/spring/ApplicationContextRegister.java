package com.gc.common.base.spring;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 容器注册类
 * @author jackson
 */
@Component
@Log4j2
public class ApplicationContextRegister implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 通过名字获取bean
     * @param name
     * @return
     */
    @Nullable
    public static Object getBean(@NotNull String name) {
        try {
            return applicationContext.getBean(name);
        } catch (Exception e) {
            log.error("获取bean失败", e);
            return null;
        }
    }

    /**
     * 通过类型获取bean
     * @param clazz
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T getBean(@NotNull Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.error("获取bean失败", e);
            return null;
        }
    }

    /**
     * 获取spring上下文
     * @return
     */
    @NotNull
    public static ApplicationContext getContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        log.debug("ApplicationContext registed-->{}", applicationContext);
        ApplicationContextRegister.applicationContext = applicationContext;
    }
}
