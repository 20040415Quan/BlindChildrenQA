package com.example.androidmvvmtest.network.bean.request;

import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("email")
    private String email;

    public Request(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}