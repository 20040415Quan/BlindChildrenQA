package com.example.androidmvvmtest.network.bean.request;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/4/19 10:46
 * @Description: TODO
 */
public class WanAndroidRegisterRequestBean {
    private String username;
    private String password;
    private String repassword;//再次输入密码

    public WanAndroidRegisterRequestBean() {
    }

    public WanAndroidRegisterRequestBean(String username, String password, String repassword) {
        this.username = username;
        this.password = password;
        this.repassword = repassword;
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

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
}
