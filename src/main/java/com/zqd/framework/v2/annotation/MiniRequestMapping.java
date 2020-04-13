package com.zqd.framework.v2.annotation;

import java.lang.annotation.*;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:21
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiniRequestMapping {

    String value() default "";

    RequestMethod[] method() default {};
}
