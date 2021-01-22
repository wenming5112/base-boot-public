package com.base.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Spring util for get class or env values
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/12/30 23:05
 **/
@Component
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (!ObjectUtils.isEmpty(context)) {
            SpringContextUtil.context = context;
        }
    }

    /**
     * Get applicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * Get Bean by name
     *
     * @return ApplicationContext
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * Get Bean by clazz
     *
     * @return ApplicationContext
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * Get Bean by name & clazz
     *
     * @return ApplicationContext
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    /**
     * Get env value
     *
     * @return ApplicationContext
     */
    public static String getEnvironment(String key) {
        return context.getEnvironment().resolvePlaceholders(key);
    }
}
