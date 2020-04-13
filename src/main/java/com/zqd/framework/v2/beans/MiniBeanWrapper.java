package com.zqd.framework.v2.beans;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-4-13-23:51
 */
public class MiniBeanWrapper {

    private Object wrapperInstance;
    private Class<?> wrappedClass;

    public MiniBeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrappedClass = instance.getClass();
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Class<?> getWrappedClass() {
        return wrappedClass;
    }

    public void setWrappedClass(Class<?> wrappedClass) {
        this.wrappedClass = wrappedClass;
    }
}
