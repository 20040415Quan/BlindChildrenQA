package com.example.androidmvvmtest.network.bean.response;

import java.util.List;

public class ChatRecordsResponse {

    private Integer code;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Integer total;
        private Integer lastPage;
        private Integer current;
        private Integer size;
        private List<Data.ChatBean> data;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getLastPage() {
            return lastPage;
        }

        public void setLastPage(Integer lastPage) {
            this.lastPage = lastPage;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public List<Data.ChatBean> getData() {
            return data;
        }

        public void setData(List<Data.ChatBean> data) {
            this.data = data;
        }

        public static class ChatBean {
            private Integer id;
            private Integer uid;
            private String question;
            private String answer;
            private String time;
            private String audioPath;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getUid() {
                return uid;
            }

            public void setUid(Integer uid) {
                this.uid = uid;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getAudioPath() {
                return audioPath;
            }

            public void setAudioPath(String audioPath) {
                this.audioPath = audioPath;
            }
        }
    }
}