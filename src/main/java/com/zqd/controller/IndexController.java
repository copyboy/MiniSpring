package com.zqd.controller;

import com.zqd.annotation.MiniController;
import com.zqd.annotation.MiniRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-31-15:45
 */
@MiniController
public class IndexController {

    @MiniRequestMapping("/")
    public void index(HttpServletRequest req, HttpServletResponse resp) {
        try {

            // 想要跳转根路径主页，但是不生效，后续需要做静态资源的直接映射
            req.getRequestDispatcher( "index.jsp").forward(req, resp);;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
