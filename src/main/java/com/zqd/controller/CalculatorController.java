package com.zqd.controller;

import com.zqd.framework.v2.annotation.MiniAutowired;
import com.zqd.framework.v2.annotation.MiniController;
import com.zqd.framework.v2.annotation.MiniRequestMapping;
import com.zqd.framework.v2.annotation.MiniRequestParam;
import com.zqd.service.CalculatorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:20
 */
@MiniController
@MiniRequestMapping(value = "/calc")
public class CalculatorController {

    @MiniAutowired
    private CalculatorService service;

    @MiniRequestMapping(value = "/add")
    public void add(HttpServletRequest req, HttpServletResponse resp,
                   @MiniRequestParam(value = "a") Integer a,
                   @MiniRequestParam(value = "b") Integer b){

        try {
            int c = a + b;

            PrintWriter print = resp.getWriter();
            print.write("<h1>");
            print.write("a="+a +",b="+b+",a+b="+c );
            print.write("</h1>");

            print.flush();
            print.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MiniRequestMapping(value = "/sub")
    public void sub(HttpServletRequest req, HttpServletResponse resp,
                   @MiniRequestParam(value = "a") Integer a,
                   @MiniRequestParam(value = "b") Integer b){

        try {

            int c = a - b;
            PrintWriter print = resp.getWriter();
            print.write("<h1>");
            print.write("a="+a +",b="+b+",a-b="+c);
            print.write("</h1>");

            print.flush();
            print.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @MiniRequestMapping(value = "/hello")
    public void hello(HttpServletRequest req, HttpServletResponse resp,
                        @MiniRequestParam(value = "name") String name) {

//        String retMsg = "My name is "+name + ", this is controller";
        String retMsg = service.hello(name);
        try {
            PrintWriter print = resp.getWriter();
            print.write("<h1>");
            print.write(retMsg);
            print.write("</h1>");

            print.flush();
            print.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
