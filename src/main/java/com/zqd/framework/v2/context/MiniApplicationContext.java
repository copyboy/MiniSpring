package com.zqd.framework.v2.context;

import com.zqd.framework.v2.beans.config.MiniBeanDefinition;
import com.zqd.framework.v2.beans.support.MiniBeanDefinitionReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-4-1-21:12
 */
public class MiniApplicationContext {

    private Map<String, MiniBeanDefinition> beanDefinitionMap = new HashMap<>();
    private MiniBeanDefinitionReader reader;

    public MiniApplicationContext(String...  configLocations) {

        // 1. 读取配置文件
        reader = new MiniBeanDefinitionReader(configLocations);
        // 2. 解析配置文件，封装成BeanDefinition
        List<MiniBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // 3. 保存BeanDefinition
        doRegisterBeanDefinition(beanDefinitions);
        // 4.
        doAutowired();
    }

    private void doAutowired() {
        // 调用getBean()
    }

    private void doRegisterBeanDefinition(List<MiniBeanDefinition> beanDefinitions) {

    }


    public Object getBean(String beanName) {

        return null;
    }

    public Object getBean(Class beanClass) {

        return getBean(beanClass.getName());

    }
}
