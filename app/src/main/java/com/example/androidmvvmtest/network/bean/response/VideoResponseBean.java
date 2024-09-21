package com.example.androidmvvmtest.network.bean.response;

import java.util.List;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/4/6 15:00
 * @Description: video网络请求返回数据解析格式
 */
public class VideoResponseBean {

    private String reason;
    private List<ResultDTO> result;
    private Integer error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultDTO> getResult() {
        return result;
    }

    public void setResult(List<ResultDTO> result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public static class ResultDTO {
        private String title;
        private String share_url;
        private String author;
        private String item_cover;
        private Integer hot_value;
        private String hot_words;
        private Integer play_count;
        private Integer digg_count;
        private Integer comment_count;

        public ResultDTO() {
        }

        public ResultDTO(String title, String share_url, String author, String item_cover, Integer hot_value, String hot_words, Integer play_count, Integer digg_count, Integer comment_count) {
            this.title = title;
            this.share_url = share_url;
            this.author = author;
            this.item_cover = item_cover;
            this.hot_value = hot_value;
            this.hot_words = hot_words;
            this.play_count = play_count;
            this.digg_count = digg_count;
            this.comment_count = comment_count;
        }

        public ResultDTO(String title, String share_url, String author, String item_cover, Integer hot_value, String hot_words) {
            this.title = title;
            this.share_url = share_url;
            this.author = author;
            this.item_cover = item_cover;
            this.hot_value = hot_value;
            this.hot_words = hot_words;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getItem_cover() {
            return item_cover;
        }

        public void setItem_cover(String item_cover) {
            this.item_cover = item_cover;
        }

        public Integer getHot_value() {
            return hot_value;
        }

        public void setHot_value(Integer hot_value) {
            this.hot_value = hot_value;
        }

        public String getHot_words() {
            return hot_words;
        }

        public void setHot_words(String hot_words) {
            this.hot_words = hot_words;
        }

        public Integer getPlay_count() {
            return play_count;
        }

        public void setPlay_count(Integer play_count) {
            this.play_count = play_count;
        }

        public Integer getDigg_count() {
            return digg_count;
        }

        public void setDigg_count(Integer digg_count) {
            this.digg_count = digg_count;
        }

        public Integer getComment_count() {
            return comment_count;
        }

        public void setComment_count(Integer comment_count) {
            this.comment_count = comment_count;
        }
    }
}
