package com.zqd.framework.v2.context;

import com.zqd.framework.v2.annotation.MiniAutowired;
import com.zqd.framework.v2.annotation.MiniComponent;
import com.zqd.framework.v2.beans.MiniBeanWrapper;
import com.zqd.framework.v2.beans.config.MiniBeanDefinition;
import com.zqd.framework.v2.beans.support.MiniBeanDefinitionReader;

import java.lang.reflect.Field;
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
    private Map<String, MiniBeanWrapper> factoryBeanInstanceCache = new HashMap<>();
    private Map<String, Object> factoryObjectCache = new HashMap<>();

    public MiniApplicationContext(String...  configLocations) {

        try {
            // 1. 读取配置文件
            reader = new MiniBeanDefinitionReader(configLocations);
            // 2. 解析配置文件，封装成BeanDefinition
            List<MiniBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
            // 3. 保存BeanDefinition
            doRegisterBeanDefinition(beanDefinitions);
            // 4.
            doAutowired();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doAutowired() {
        // 调用getBean()
        for (Map.Entry<String, MiniBeanDefinition> beanDefinition : beanDefinitionMap.entrySet()) {

            String beanName = beanDefinition.getKey();
            getBean(beanName);

        }
    }

    private void doRegisterBeanDefinition(List<MiniBeanDefinition> beanDefinitions) throws Exception {
        for (MiniBeanDefinition beanDefinition : beanDefinitions) {

            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The" + beanDefinition.getBeanClassName() + "is exist.");
            }

            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }


    // Bean 的实例化
    public Object getBean(String beanName) {

        // 1. 获取BeanDefinition
        MiniBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        // 2. 实例化
        Object object = instanceObject(beanDefinition);
        // 3. 封装beanWrapper
        MiniBeanWrapper beanWrapper = new MiniBeanWrapper(object);
        // 4. 保存
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 5. 依赖注入
        populateBean(beanName, beanDefinition, beanWrapper);

        return beanWrapper.getWrapperInstance();
    }

    private void populateBean(String beanName, MiniBeanDefinition beanDefinition, MiniBeanWrapper beanWrapper) {

        // 循环依赖 TODO
        // 两个缓存
        // 第一次读取结果为空的BeanDefinition，存到缓存中
        // 第一次循环结束后，第二次循环再检查第一次的缓存，进行赋值

        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrappedClass();

        if (!clazz.isAnnotationPresent(MiniComponent.class)){return;}

        // 遍历所有的Field属性,包括public,protected, private ...
        for (Field field : clazz.getDeclaredFields()) {

            // 判断Field 是使用了Autowired注解的
            if (!field.isAnnotationPresent(MiniAutowired.class)) {
                continue;
            }

            MiniAutowired autowired = field.getAnnotation(MiniAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                if (field.getType().isInterface()) {
                    // 接口全名
                    autowiredBeanName = field.getType().getName();
                } else {
                    // 实现类类名，首字母小写
                    autowiredBeanName = toLowFirstCase(field.getType().getSimpleName());
                }
            }

            field.setAccessible(true);
            try {
                if (factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
                field.set(instance, factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }



    private Object instanceObject(MiniBeanDefinition beanDefinition) {

        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();
            factoryObjectCache.put(beanDefinition.getFactoryBeanName(), instance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    public Object getBean(Class beanClass) {

        return getBean(beanClass.getName());

    }

    private String toLowFirstCase(String simpleName) {

        char[] name = simpleName.toCharArray();
        if (name[0] >= 'A' && name[0] <='Z') {
            name[0] += 32;
        }
        return new String(name);
    }
}
