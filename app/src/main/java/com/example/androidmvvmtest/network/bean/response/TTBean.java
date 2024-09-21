package com.example.androidmvvmtest.network.bean.response;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe:
 */
public class TTBean {
    public Integer code;
    public String msg;
    public String update;

    public Data data;

    public static class Data{
        public String title;
        public String content;
        public String videolink;
    }
}
