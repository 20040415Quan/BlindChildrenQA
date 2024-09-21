package com.example.androidmvvmtest.bean;

public class AudioDetails {
    private int aid;
    private String title;
    private String url;
    private String overview;
    private String author;
    private int playNums;
    private int favourNums;
    private int thumbNums;
    private String content;
    private String picture;
    private String chapter;

    // 构造函数
    public AudioDetails(int aid, String title, String url, String overview, String author, int playNums, int favourNums, int thumbNums, String content, String picture, String chapter) {
        this.aid = aid;
        this.title = title;
        this.url = url;
        this.overview = overview;
        this.author = author;
        this.playNums = playNums;
        this.favourNums = favourNums;
        this.thumbNums = thumbNums;
        this.content = content;
        this.picture = picture;
        this.chapter = chapter;
    }

    // Getter 和 Setter 方法
    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPlayNums() {
        return playNums;
    }

    public void setPlayNums(int playNums) {
        this.playNums = playNums;
    }

    public int getFavourNums() {
        return favourNums;
    }

    public void setFavourNums(int favourNums) {
        this.favourNums = favourNums;
    }

    public int getThumbNums() {
        return thumbNums;
    }

    public void setThumbNums(int thumbNums) {
        this.thumbNums = thumbNums;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}