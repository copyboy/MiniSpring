package com.zqd.framework.v2.webmvc.servlet;

import com.zqd.framework.v2.annotation.*;
import com.zqd.framework.v2.context.MiniApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-29-21:20
 */
public class MiniDispatcherServlet extends HttpServlet {



    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    private MiniApplicationContext applicationContext;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 6. 委派
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception, detail :" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }

    }


    @Override
    public void init(ServletConfig config) throws ServletException {

        applicationContext = new MiniApplicationContext(config.getInitParameter("contextPath"));

//        // 1. 加载配置文件
//        doLoadConfig(config.getInitParameter("contextPath"));
//
//        // 2. 扫描相关的类
//        doScanner(contextConfig.getProperty("scanPackage"));
//
//        // ==============IOC====================
//        // 3. IOC 容器初始化，并实例化保存
//        doInstance();
//
//        // ==============AOP====================
//
//        // ==============DI====================
//        // 4. 依赖注入
//        doAutowired();

        // ==============MVC====================
        // 5. 初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("Mini Spring framework is init.");
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 这里看视频，有点问题，我们只要取值uri就可以，不需要replaceAll 相对路径，
        // uri 就已经是相对路径了
        String uri = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        uri = uri.replace(contextPath, "").replaceAll("/+", "");

        if (!handlerMapping.containsKey(uri)) {
            resp.getWriter().write("404 Not Found .");
            return;
        }

        Method method = this.handlerMapping.get(uri);
        // 所有参数
        Map<String, String[]> params = req.getParameterMap();

        // 获取形参列表
        Class<?>[] paramTypes = method.getParameterTypes();
        // 赋值实参
        Object[] paramValue = new Object[paramTypes.length];
        Annotation[][] annos = method.getParameterAnnotations();

        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];

            if (paramType == HttpServletRequest.class) {
                paramValue[i] = req;
            } else if (paramType == HttpServletResponse.class) {
                paramValue[i] = resp;
            } else if (paramType == String.class) {

                // 这里听课的时候，有点懵，主要是因为method.getParameterAnnotations()
                // 这个取值对象是所有参数，结果是一个二维数组
                // 比如 hello方法，有三个参数， req,resp,String
                // 这里的二维数组就是 anno[0]为空
                // anno[1] 为空
                // anno[2] 值为1， = [MiniRequestParam]，如果是多个，比如我们常用的 Validate注解，
                // 就会得到一个 anno[2] = [MiniRequestParam, Validate],
                // 我们遍历对象，应该是anno[2]，所以这里的i.j下标是i，

                for (Annotation an: annos[i]) {
                    if (an instanceof MiniRequestParam) {
                        String paramName = ((MiniRequestParam) an).value();
                        if (!"".equals(paramName.trim())) {
                            String value = Arrays.toString(params.get(paramName))
                                    .replaceAll("\\[|\\]","")
                                    .replaceAll("\\s+","");
                            paramValue[i] = value;
                        }
                    }
                }
            } else if (paramType == Integer.class) {
                for (Annotation an: annos[i]) {
                    if (an instanceof MiniRequestParam) {
                        String paramName = ((MiniRequestParam) an).value();
                        if (!"".equals(paramName.trim())) {
                            String value = Arrays.toString(params.get(paramName))
                                    .replaceAll("\\[|\\]","")
                                    .replaceAll("\\s+","");
                            paramValue[i] = Integer.valueOf(value);
                        }
                    }
                }
            }

        }
        String beanName = toLowFirstCase(method.getDeclaringClass().getSimpleName());
        // 这里Controller 是规范的，所以直接是类名首字母小写
        method.invoke(ioc.get(beanName), paramValue);


    }

    private void doInitHandlerMapping() {
        if (ioc.isEmpty()) {return;}

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            Class<?> clz = entry.getValue().getClass();
            if (!clz.isAnnotationPresent(MiniController.class)){continue;}

            String baseUri = "";
            if (clz.isAnnotationPresent(MiniRequestMapping.class)) {
                baseUri = clz.getAnnotation(MiniRequestMapping.class).value();
            }

            // 只取public方法
            for (Method method : clz.getMethods()) {
                if (!method.isAnnotationPresent(MiniRequestMapping.class)) {continue;}
                MiniRequestMapping mapping = method.getAnnotation(MiniRequestMapping.class);
                String uri = ("/" + baseUri + "/" + mapping.value()).replaceAll("/+", "/");
                handlerMapping.put(uri, method);

                System.out.println("Mapped [uri: " + uri + "-> Method: "+method +"]");
            }

        }

    }


    private String toLowFirstCase(String simpleName) {

        char[] name = simpleName.toCharArray();
        if (name[0] >= 'A' && name[0] <='Z') {
            name[0] += 32;
        }
        return new String(name);
    }



}
