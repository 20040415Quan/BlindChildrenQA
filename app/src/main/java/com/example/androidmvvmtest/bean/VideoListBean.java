package com.example.androidmvvmtest.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoListBean {

    @SerializedName("total")
    public Integer total;
    @SerializedName("lastPage")
    public Integer lastPage;
    @SerializedName("current")
    public Integer current;
    @SerializedName("size")
    public Integer size;
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        @SerializedName("aid")
        public Integer aid;
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
        @SerializedName("overview")
        public String overview;
        @SerializedName("author")
        public String author;
        @SerializedName("playNums")
        public Integer playNums;
        @SerializedName("favourNums")
        public Integer favourNums;
        @SerializedName("thumbNums")
        public Integer thumbNums;
        @SerializedName("content")
        public String content;
        @SerializedName("picture")
        public String picture;
        @SerializedName("chapter")
        public String chapter;
    }
}
