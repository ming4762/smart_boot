package com.gc.common.base.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
    public static Object getBean(@NonNull String name) {
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
    public static <T> T getBean(@NonNull Class<T> clazz) {
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
    @NonNull
    public static ApplicationContext getContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        log.debug("ApplicationContext registed-->{}", applicationContext);
        ApplicationContextRegister.applicationContext = applicationContext;
    }
}
