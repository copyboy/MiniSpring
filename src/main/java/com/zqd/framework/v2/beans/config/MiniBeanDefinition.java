package com.zqd.framework.v2.beans.config;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-4-13-22:53
 */
public class MiniBeanDefinition {

    private String factoryBeanName;

    private String beanClassName;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}
