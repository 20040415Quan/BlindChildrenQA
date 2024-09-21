package com.example.androidmvvmtest.network.bean.response;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/4/15 16:00
 * @Description: TODO
 */
public class DailyContentResponseBean {
    private Integer code;
    private String msg;
    private Result result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private Integer oneid;
        private String word;
        private String wordfrom;
        private String imgurl;
        private String imgauthor;
        private String date;

        public Integer getOneid() {
            return oneid;
        }

        public void setOneid(Integer oneid) {
            this.oneid = oneid;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getWordfrom() {
            return wordfrom;
        }

        public void setWordfrom(String wordfrom) {
            this.wordfrom = wordfrom;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getImgauthor() {
            return imgauthor;
        }

        public void setImgauthor(String imgauthor) {
            this.imgauthor = imgauthor;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
