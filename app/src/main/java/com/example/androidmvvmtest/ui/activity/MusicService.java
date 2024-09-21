package com.example.androidmvvmtest.ui.activity;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.androidmvvmtest.base.BaseApplication;
import com.example.androidmvvmtest.manager.MusicManager;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wuleizhenshang
 * 音乐播放服务类
 * 所有音乐相关资源和进度等信息应该由服务器持有
 */
public class MusicService extends Service {

    //多媒体播放器对象
    private MediaPlayer player;
    //音乐进度变化接口
    private OnMusicPlayListener mMusicPlayListener;
    //播放列表
    private List<Integer> audioResIds = new ArrayList<>();
    //当前播放的歌曲在list中的下标
    private int currentAudioIndex = 0;
    //时间任务
    private Timer timer;
    //音乐播放全局通知接口
    private MusicManager musicManager = MusicManager.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        //创建音乐播放器对象
        player = new MediaPlayer();
        //异步加载完就播放
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //播放并添加计时器
                mp.start();
                addTimer();
                musicManager.notifyItemChange(currentAudioIndex);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 添加计时器用于设置音乐播放器中的播放进度条
     */
    public void addTimer() {
        //创建计时器对象
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (player == null) {
                    return;
                }

                //回调外部
                if (mMusicPlayListener != null) {
                    //是否正在播放
                    mMusicPlayListener.onMusicPlayingChange(player.isPlaying());
                    //播放进度变化
                    mMusicPlayListener.onMusicProgressChange(player.getCurrentPosition(), player.getDuration());
                }
            }
        };
        //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒（0.5s）执行一次
        timer.schedule(task, 5, 500);
    }

    /**
     * 音乐播放变化的接口
     */
    public interface OnMusicPlayListener {
        /**
         * @param current 当前进度
         * @param total   总共时长
         */
        void onMusicProgressChange(int current, int total);

        /**
         * @param isPlaying 是否正在
         */
        void onMusicPlayingChange(boolean isPlaying);
    }

    /**
     * 让外部绑定服务获取到的Binder对象
     */
    class MusicControl extends Binder {

        /**
         * 设置音乐变化监听
         */
        public void setOnMusicPlayListener(OnMusicPlayListener onMusicPlayListener) {
            mMusicPlayListener = onMusicPlayListener;
        }

        /**
         * 播放 播放列表中的内容，必须先update列表
         */
        public void play(int position) {
            if (player == null) {
                return;
            }
            if (audioResIds.get(position)!=0){
                Uri uri = Uri.parse("android.resource://" + BaseApplication.sContext.getPackageName() + "/" + audioResIds.get(position));
                KLog.i("MusicControl", " 准备uri " + uri.toString());
                player.reset();
                try {
                    //记录播放下标
                    currentAudioIndex = position;
                    player.setDataSource(BaseApplication.sContext, uri);
                    player.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 暂停
         */
        public void pausePlay() {
            if (player == null) {
                return;
            }
            player.pause();
        }

        /**
         * 继续播放
         */
        public void continuePlay() {
            if (player == null) {
                return;
            }
            player.start();
        }

        /**
         * 设置播放进度
         *
         * @param progress 进度
         */
        public void seekTo(int progress) {
            if (player == null) {
                return;
            }
            player.seekTo(progress);
        }

        /**
         * 更新播放列表
         */
        public void updatePlayList(List<Integer> list) {
            //更新播放列表需要重设相关资源
            audioResIds.clear();
            audioResIds.addAll(list);

            //重设相关服务
            if (!audioResIds.isEmpty()) {
                play(0);
            }
        }

        /**
         * 播放上一首
         */
        public void playLast() {
            if (audioResIds.size() > currentAudioIndex - 1 && currentAudioIndex - 1 >= 0) {
                play(currentAudioIndex - 1);
            }
        }

        /**
         * 播放下一首
         */
        public void playNext() {
            if (audioResIds.size() > currentAudioIndex + 1) {
                play(currentAudioIndex + 1);
            }
        }

        /**
         * 是否正在播放
         * @return
         */
        public boolean isPlaying(){
            return player!=null && player.isPlaying();
        }
    }
}