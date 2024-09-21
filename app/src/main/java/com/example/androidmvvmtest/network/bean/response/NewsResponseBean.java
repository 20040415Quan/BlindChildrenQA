package com.example.androidmvvmtest.network.bean.response;

import java.util.List;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe:
 */
public class NewsResponseBean {

    private List<NewsBean> list;

    public List<NewsBean> getList() {
        return list;
    }

    public static class NewsBean{
        private String title;//新闻标题
        private String time;//新闻时间
        private String src;//新闻来源
        private String category;//新闻分类
        private String pic;//新闻图片
        private String url;//原文网址
        private String weburl;//原文PC网址
        private String content;//新闻内容

        public NewsBean() {
        }

        public NewsBean(String title, String time, String src, String category, String pic, String url, String weburl, String content) {
            this.title = title;
            this.time = time;
            this.src = src;
            this.category = category;
            this.pic = pic;
            this.url = url;
            this.weburl = weburl;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
