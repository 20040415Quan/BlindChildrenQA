package com.example.androidmvvmtest.network.bean.request;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/4/19 10:45
 * @Description: TODO
 */
public class WanAndroidLoginRequestBean {
    private String username;
    private String password;

    public WanAndroidLoginRequestBean() {
    }

    public WanAndroidLoginRequestBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
