package com.example.androidmvvmtest.manager;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {

    private static volatile MusicManager instance;

    private MusicManager(){

    }

    public static MusicManager getInstance() {
        if (instance==null){
            synchronized (MusicManager.class){
                if (instance==null){
                    instance = new MusicManager();
                }
            }
        }
        return instance;
    }

    public int lastPlayId = -1;

    public interface PlayListener{

        void itemChange(int last,int now);

        //播放器监听进行播放
        void playChange(int id);
    }

    private List<PlayListener>listeners = new ArrayList<>();

    public void setPlayListener(PlayListener playListener){
        listeners.add(playListener);
    }

    public void removePlayListener(PlayListener playListener){
        listeners.remove(playListener);
    }

    /**
     * 下一首或者上一首
     * @param id
     */
    public void notifyItemChange(int id){
        for (PlayListener listener:listeners) {
            listener.itemChange(lastPlayId,id);
        }
        lastPlayId = id;
    }

    /**
     * 让音乐播放器播放音乐
     * @param id
     */
    public void notifyPlay(int id){
        for (PlayListener listener:listeners) {
            listener.playChange(id);
        }
    }

}