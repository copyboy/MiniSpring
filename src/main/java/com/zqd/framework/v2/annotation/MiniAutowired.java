package com.zqd.framework.v2.annotation;

import java.lang.annotation.*;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:31
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiniAutowired {
    String value() default "";
}
