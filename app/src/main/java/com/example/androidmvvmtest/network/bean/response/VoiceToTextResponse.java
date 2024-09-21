package com.example.androidmvvmtest.network.bean.response;

public class VoiceToTextResponse {
    private int code;
    private String msg;
    private String data;

    // Getters
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

    // Setters
    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(String data) {
        this.data = data;
    }
}