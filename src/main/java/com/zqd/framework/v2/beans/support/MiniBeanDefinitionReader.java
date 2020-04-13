package com.zqd.framework.v2.beans.support;

import com.zqd.framework.v2.annotation.MiniComponent;
import com.zqd.framework.v2.annotation.MiniService;
import com.zqd.framework.v2.beans.config.MiniBeanDefinition;
import sun.reflect.annotation.AnnotationType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-4-13-22:56
 */
public class MiniBeanDefinitionReader {

    private Properties contextConfig = new Properties();
    private List<String> registerBeanClasses = new ArrayList<>();

    public MiniBeanDefinitionReader(String... configLocations) {
        doLoadConfig(configLocations[0]);
        // 扫描配置文件相关类
        doScanner(contextConfig.getProperty("scanPackage"));

    }

    public List<MiniBeanDefinition> loadBeanDefinitions() {
        List<MiniBeanDefinition> beanDefinitions = new ArrayList<>();
        try {
            for (String registerBeanClass : registerBeanClasses) {
                Class<?> beanClass = Class.forName(registerBeanClass);

                // beanName
                String beanName = null;
                String beanClassName = beanClass.getName();

                // 非MiniComponent注解的类，不加入到IOC容器中
                if (!beanClass.isAnnotationPresent(MiniComponent.class)){continue;}
                // 保存类的全类名
                Annotation[] annotations =  beanClass.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof MiniComponent) {
                        // 1. 自定义的Component组件value值, TODO， 这里可能有问题
                        beanName = ((MiniComponent) annotation).value();
                    }
                }
                // 2. 默认类名首字母小写，
                if ("".equals(beanName)) {
                    beanName = toLowFirstCase(beanClass.getSimpleName());
                }
                beanDefinitions.add(doCreateBeanDefinition(beanName, beanClassName));
                // 3. 接口注入
                for (Class<?> anInterface : beanClass.getInterfaces()) {
                    beanDefinitions.add(doCreateBeanDefinition(anInterface.getName(), beanClass.getName()));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    private MiniBeanDefinition doCreateBeanDefinition(String beanName, String beanClassName) {
        MiniBeanDefinition beanDefinition = new MiniBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(beanName);
        return beanDefinition;
    }

    private void doLoadConfig(String contextPath) {

        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(contextPath.replaceAll("classpath:", ""));

        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {

                if (!file.getName().endsWith(".class")) {
                    continue;
                }

                String className = scanPackage + "." + file.getName().replace(".class", "");
                registerBeanClasses.add(className);
            }
        }
    }

    private String toLowFirstCase(String simpleName) {

        char[] name = simpleName.toCharArray();
        if (name[0] >= 'A' && name[0] <= 'Z') {
            name[0] += 32;
        }
        return new String(name);
    }
}
