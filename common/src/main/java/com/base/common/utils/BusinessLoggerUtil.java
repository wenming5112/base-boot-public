package com.base.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ming
 * @version 1.0.0
 * @since 2020/12/30 23:47
 **/

public class BusinessLoggerUtil {
    private static String prefix = "prefix --- >";

    private Logger logger = null;

    private static Map<Object, Logger> loggerMap = new HashMap<>();

    private static class BusinessLogger {
        private static BusinessLoggerUtil instance = new BusinessLoggerUtil();
    }

    /**
     * 获取服务实例的静态方法
     *
     * @param clazz 传入调用此方法的类型
     * @return init
     */
    public static <T> BusinessLoggerUtil newInstance(Class<T> clazz) {
        BusinessLoggerUtil businessLogger = BusinessLogger.instance;

        businessLogger.logger = loggerMap.get(clazz);

        if (null == businessLogger.logger) {
            businessLogger.logger = LoggerFactory.getLogger(clazz);
            loggerMap.put(clazz, businessLogger.logger);
        }

        return businessLogger;
    }

    public void trace(String message) {
        this.logger.trace(prefix + message);
    }

    public void debug(String message) {
        this.logger.debug(prefix + message);
    }

    public void info(String message) {
        this.logger.info(prefix + message);
    }

    public void warn(String message) {
        this.logger.warn(prefix + message);
    }

    public void error(String message) {
        this.logger.error(prefix + message);
    }

}
