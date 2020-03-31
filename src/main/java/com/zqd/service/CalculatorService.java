package com.zqd.service;

import com.zqd.annotation.MiniService;

/**
 * @author qingdong.zhang
 * @version 1.0
 * @since 2020-3-30-23:33
 */
@MiniService
public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public String hello(String name) {
        return "My name is "+name + ", this is service";
    }

}
