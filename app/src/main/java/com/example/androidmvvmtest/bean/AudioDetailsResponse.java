package com.example.androidmvvmtest.bean;

public class AudioDetailsResponse {
    private int code;
    private String msg;
    private Data data;

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
        private int find;
        private String words;
        private AudioDetails audio;

        // Getters and Setters
        public int getFind() {
            return find;
        }

        public void setFind(int find) {
            this.find = find;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public AudioDetails getAudio() {
            return audio;
        }

        public void setAudio(AudioDetails audio) {
            this.audio = audio;
        }
    }
}