package com.zqd.annotation;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-31-11:23
 */
public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private RequestMethod() {
    }
}
