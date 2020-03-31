package com.zqd.annotation;

import java.lang.annotation.*;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:24
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiniRequestParam {

    String value() default "";

}
