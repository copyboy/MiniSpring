package com.zqd.framework.v2.annotation;

import java.lang.annotation.*;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiniComponent {
    String value() default "";
}
