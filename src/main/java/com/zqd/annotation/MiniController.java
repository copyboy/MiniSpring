package com.zqd.annotation;

import java.lang.annotation.*;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:14
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MiniComponent
public @interface MiniController {

    String value() default "";
}
