package com.base.common.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author wzm
 * @date 2020/04/16
 */
// 注解会在class中存在，运行时可通过反射获取
@Retention(RetentionPolicy.RUNTIME)
// 目标是方法
@Target({ElementType.METHOD, ElementType.PARAMETER})
// 表示是否将注解信息添加在java文档中
@Documented
public @interface SysLog {

    // 这个用户所做的是什么操作
    String value() default "未标注操作";

    /**
     * 1 2 3 4 / 增 删 改 查
     * 0 未设置
     *
     * @return int
     */
    int type() default 0;

}
