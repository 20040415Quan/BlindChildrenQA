package com.example.androidmvvmtest.ui.activity;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.bean.AudioDetails;
import com.example.androidmvvmtest.bean.AudioDetailsResponse;
import com.example.androidmvvmtest.bean.FavoriteResponse;
import com.example.androidmvvmtest.bean.ThumbResponse;
import com.example.androidmvvmtest.manager.MusicManager;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.Interface.XunFeiCallbackListener;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.ui.adapter.MusicVp2Adapter;
import com.example.androidmvvmtest.ui.fragment.MusicFragment;
import com.example.androidmvvmtest.utils.Constant;
import com.example.androidmvvmtest.utils.MVUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iflytek.cloud.RecognizerResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener, XunFeiCallbackListener {

    //region 一些额外参数
    //是否滑块在滑动
    private boolean isTrackingTouch = false;
    //是否ViewPager2在滑动
    private boolean isViewPager2Scroll = false;
    //endregion

    //region 控件相关对象
    private FrameLayout frameLayout;
    private ImageButton back;
    private ViewPager2 musicList;
    private LinearLayout pageIndicatorContainer;
    private ImageButton btnSpeech;
    private ConstraintLayout musicPlayer;
    private TextView songName;
    private ImageView imageView3;
    private ImageView imageView2;
    private ImageView ivMusic;
    private TextView tvContent;
    private LinearLayout linearLayout2;
    private ImageButton btnFavorite;
    private ImageButton btnComment;
    private SeekBar seekbar;
    private LinearLayout linearLayout;
    private ImageButton btnLast;
    private ImageButton btnPlayOrPause;
    private ImageButton btnNext;
    private BottomNavigationView bottomNavigation;
    private ImageButton btnLike;
    private TextView tvThumb;
    private TextView pageDot1;
    private TextView pageDot2;
    private TextView pageDot3;
    private TextView pageDot4;
    private ImageButton btnLeftArrow;
    private ImageButton btnRightArrow;
    private TextView tvPlayCount;


    //endregion

    //region 控件相关
    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        back = (ImageButton) findViewById(R.id.back);
        musicList = (ViewPager2) findViewById(R.id.music_list);
        pageIndicatorContainer = (LinearLayout) findViewById(R.id.page_indicator_container);
        btnSpeech = (ImageButton) findViewById(R.id.btn_speech);
        musicPlayer = (ConstraintLayout) findViewById(R.id.music_player);
        songName = (TextView) findViewById(R.id.song_name);
//        imageView3 = (ImageView) findViewById(R.id.imageView3);
//        imageView2 = (ImageView) findViewById(R.id.imageView2);
        ivMusic = (ImageView) findViewById(R.id.iv_music);
        tvContent = (TextView) findViewById(R.id.tv_content);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        btnFavorite = (ImageButton) findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);
        btnComment = (ImageButton) findViewById(R.id.btn_comment);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btnLast = (ImageButton) findViewById(R.id.btn_last);
        btnLast.setOnClickListener(this);
        btnPlayOrPause = (ImageButton) findViewById(R.id.btn_play_or_pause);
        btnPlayOrPause.setOnClickListener(this);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        btnLike = findViewById(R.id.btn_like);
        btnLike.setOnClickListener(this);
        //tvThumb = findViewById(R.id.tv_thumb);
        pageDot1 = findViewById(R.id.page_dot1);
        pageDot1.setOnClickListener(this);
        pageDot2 = findViewById(R.id.page_dot2);
        pageDot2.setOnClickListener(this);
        pageDot3 = findViewById(R.id.page_dot3);
        pageDot3.setOnClickListener(this);
        pageDot4 = findViewById(R.id.page_dot4);
        pageDot4.setOnClickListener(this);
        btnLeftArrow = (ImageButton) findViewById(R.id.btn_left_arrow);
        btnLeftArrow.setOnClickListener(this);
        btnRightArrow = (ImageButton) findViewById(R.id.btn_right_arrow);
        btnRightArrow.setOnClickListener(this);
        //tvPlayCount = (TextView) findViewById(R.id.tv_play_count);
    }

    //region vp2

    /**
     * 初始化VP2
     */
    private void initVp2() {
        //点击
        musicList.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updatePageIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    isViewPager2Scroll = false;
                } else {
                    isViewPager2Scroll = true;
                }
            }
        });

        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            fragments.add(MusicFragment.newInstance(i));
        }
        MusicVp2Adapter adapter = new MusicVp2Adapter(
                this, fragments);
        musicList.setAdapter(adapter);
        musicList.setOffscreenPageLimit(4);

        //更新第一个
        updatePageIndicator(0);
    }

    /**
     * 设置当前当前指示器图标
     *
     * @param currentPage
     */
    private void updatePageIndicator(int currentPage) {
        switch (currentPage) {
            case 0:
                pageDot1.setBackgroundResource(R.drawable.ic_round);
                pageDot2.setBackground(null);
                pageDot3.setBackground(null);
                pageDot4.setBackground(null);
                break;
            case 1:
                pageDot1.setBackground(null);
                pageDot2.setBackgroundResource(R.drawable.ic_round);
                pageDot3.setBackground(null);
                pageDot4.setBackground(null);
                break;
            case 2:
                pageDot1.setBackground(null);
                pageDot2.setBackground(null);
                pageDot3.setBackgroundResource(R.drawable.ic_round);
                pageDot4.setBackground(null);
                break;
            case 3:
                pageDot1.setBackground(null);
                pageDot2.setBackground(null);
                pageDot3.setBackground(null);
                pageDot4.setBackgroundResource(R.drawable.ic_round);
                break;
        }
    }
    //endregion

    /**
     * 初始化控件相关
     */
    private void initSeekBar() {
        //进度条
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress != 0 && seekBar.getMax() != 0 && progress >= seekBar.getMax()) {
                    if (musicControl != null) {
                        //播放下一首
                        musicControl.playNext();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //拖动的时候不受外部影响
                isTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int touchProgress = seekBar.getProgress();
                //停止拖动
                isTrackingTouch = false;
                if (musicControl != null) {
                    musicControl.seekTo(touchProgress);
                }
            }
        });
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (musicControl == null) return;
        if (v == btnLeftArrow) {
            if (musicList.getCurrentItem() - 1 >= 0) {
                musicList.setCurrentItem(musicList.getCurrentItem() - 1);
            }
        } else if (v == btnRightArrow) {
            if (musicList.getCurrentItem() + 1 < 4) {
                musicList.setCurrentItem(musicList.getCurrentItem() + 1);
            }
        } else if (v == pageDot1) {
            musicList.setCurrentItem(0);
        } else if (v == pageDot2) {
            musicList.setCurrentItem(1);
        } else if (v == pageDot3) {
            musicList.setCurrentItem(2);
        } else if (v == pageDot4) {
            musicList.setCurrentItem(3);
        } else if (v == btnLike) {

        } else if (v == btnFavorite) {

        } else if (v == btnLast) {
            musicControl.playLast();
        } else if (v == btnNext) {
            musicControl.playNext();
        } else if (v == btnPlayOrPause) {
            if (musicControl.isPlaying()) {
                musicControl.pausePlay();
            } else {
                musicControl.continuePlay();
            }
        }
    }

    //endregion

    //region 服务相关全局对象
    //连接服务，获取Binder对象，获取MusicControl
    private MyServiceConn conn;
    //控制音乐播放相关的Binder对象
    private MusicService.MusicControl musicControl;

    private MusicManager musicManager = MusicManager.getInstance();
    //endregion

    //region 服务相关

    /**
     * 监听是否更换歌曲
     */
    private void notifyPlaying() {
        musicManager.setPlayListener(new MusicManager.PlayListener() {
            @Override
            public void itemChange(int last, int now) {
                int current = now / 4;
                if (!isViewPager2Scroll && musicList.getCurrentItem() != current) {
                    musicList.setCurrentItem(current);
                }
            }

            @Override
            public void playChange(int id) {
                if (musicControl != null) {
                    musicControl.play(id);
                }
            }
        });
    }

    /**
     * 初始化播放服务
     */
    private void initMusicPlayService() {
        //启动服务
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        //绑定服务
        conn = new MyServiceConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    /**
     * 销毁服务相关，回收资源
     */
    private void destroyMusicPlayService() {

        //暂停播放，回收播放器
        musicControl.pausePlay();

        //取消绑定服务
        unbindService(conn);
        conn = null;

        //暂停服务
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
    }

    //用于实现连接服务，获取binder对象
    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
            if (musicControl != null) {
                //设置播放列表
                List<Integer> list = new ArrayList<>();
                list.add(R.raw.music1);
                list.add(R.raw.music2);
                list.add(R.raw.music3);
                list.add(R.raw.music4);
                list.add(R.raw.music5);
                list.add(R.raw.music6);
                list.add(R.raw.music7);
                list.add(R.raw.music8);
                list.add(R.raw.music9);
                list.add(R.raw.music10);
                list.add(R.raw.music11);
                list.add(R.raw.music12);
                list.add(R.raw.music13);
                list.add(R.raw.music14);
                list.add(R.raw.music15);
                list.add(R.raw.music16);
                musicControl.updatePlayList(list);

                //监听变化
                musicControl.setOnMusicPlayListener(new MusicService.OnMusicPlayListener() {
                    @Override
                    public void onMusicProgressChange(int current, int total) {
                        if (!isTrackingTouch) {
                            //修改进度
                            seekbar.setMax(total);
                            seekbar.setProgress(current);
                        }
                    }

                    @Override
                    public void onMusicPlayingChange(boolean isPlaying) {
                        //根据是否在播放修改播放暂停按钮
                        if (isPlaying) {
                            btnPlayOrPause.setBackgroundResource(R.drawable.star);
                        } else {
                            btnPlayOrPause.setBackgroundResource(R.drawable.stop);
                        }
                    }
                });

                //初始化进度条
                initSeekBar();

                //关注切换歌曲
                notifyPlaying();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    //endregion

    //region 生命周期相关

    /**
     * 创建
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        //绑定控件和注册点击事件
        initView();

        //初始化vp2
        initVp2();


        //开启服务
        initMusicPlayService();


        // 获取 authToken
        authToken = MVUtils.getString(Constant.TOKEN, null);


        // 初始化讯飞语音识别
        XunFeiUtil.initXunFei(this);

        // 初始化页面指示器
        LocalBroadcastManager.getInstance(this).registerReceiver(playCountUpdateReceiver,
                new IntentFilter("com.example.androidmvvmtest.PLAY_COUNT_UPDATE"));

        //点赞
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLike();
            }
        });
        //收藏
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavor();
            }
        });

        // 录音按钮点击事件
        btnSpeech.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XunFeiUtil.startVoice(MusicActivity.this, MusicActivity.this);
                return true;
            }
        });


        //点读
        ImageButton btnSpeech = findViewById(R.id.btn_speech);
        btnSpeech.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XunFeiUtil.startVoice(MusicActivity.this, MusicActivity.this);
                return true;
            }
        });


        //底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_left:
                    // 处理左侧图标点击事件
                    Intent intent = new Intent(MusicActivity.this, AnswerActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_right:
                    // 处理右侧图标点击事件
                    return true;
                default:
                    return false;
            }
        });


        // 启动 SeekBar 更新
//        startSeekBarUpdate();
    }

    /**
     * activity暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (musicControl != null) {
            musicControl.pausePlay();
        }
    }

    /**
     * 重新启动activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (musicControl != null) {
            musicControl.continuePlay();
        }
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //暂停服务
        destroyMusicPlayService();


//        stopSeekBarUpdate(); // 停止更新 SeekBar
    }

    //endregion


    private String authToken;

    private boolean isLiked = false; // 点赞状态标志
    private int thumbCount = 0; // 点赞数量
    private boolean isFavor = false; // 收藏状态标志

    private final BroadcastReceiver playCountUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int playCount = intent.getIntExtra("playCount", 0);
            updatePlayCount(playCount);
        }
    };

    public void onFinish(RecognizerResult results) {
        // 解析讯飞语音识别的结果
        String recognizedText = XunFeiUtil.parseIatResult(results.getResultString());
        uploadAudioAndGetResponse(recognizedText);

    }

    //点赞
    private void toggleLike() {
        int audioId = 123; // 音频 ID


        // 切换点赞状态
        isLiked = !isLiked;


        // 发送点赞请求
        NetworkApi.createService(Api.class)
                .likeAudio(authToken, audioId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThumbResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 显示加载指示器
                    }

                    @Override
                    public void onNext(ThumbResponse response) {
                        // 更新点赞按钮状态和点赞量
                        if (response.getCode() == 200) {
                            updateLikeButton(isLiked);
                            updateThumbCount(isLiked ? 1 : -1);
                        } else {
                            // 如果请求失败，重置点赞状态
                            isLiked = !isLiked;
                            updateLikeButton(isLiked);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 处理错误
                    }

                    @Override
                    public void onComplete() {
                        // 隐藏加载指示器
                    }
                });
    }

    private void updateLikeButton(boolean isLiked) {
        if (isLiked) {
            btnLike.setImageResource(R.drawable.thumb);
            Toast.makeText(this, "已点赞", Toast.LENGTH_SHORT).show();

        } else {
            btnLike.setImageResource(R.drawable.thumb2);
            Toast.makeText(this, "取消点赞", Toast.LENGTH_SHORT).show();

        }
    }

    private void toggleFavor() {
        int audioId = 1; // 音频 ID
        authToken = MVUtils.getString(Constant.TOKEN, null);
        isFavor = !isFavor;


        // 发送点赞请求
        NetworkApi.createService(Api.class)
                .toggleFavorite(authToken, audioId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavoriteResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 显示加载指示器
                    }

                    @Override
                    public void onNext(FavoriteResponse response) {
                        if (response.getCode() == 200) {
                            updateFavorButton(isFavor);

                        } else {
                            // 如果请求失败，重置状态
                            isFavor = !isFavor;
                            updateLikeButton(isFavor);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 处理错误
                    }

                    @Override
                    public void onComplete() {
                        // 隐藏加载指示器
                    }
                });
    }

    private void updateFavorButton(boolean isFavor) {
        if (isFavor) {
            btnFavorite.setImageResource(R.drawable.collect);
            Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
        } else {
            btnFavorite.setImageResource(R.drawable.collect2);
            Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateThumbCount(int delta) {
        thumbCount += delta;
        tvThumb.setText(String.valueOf(thumbCount));


    }

    //更新播放器详情

    public void playAudioAndUpdateDetails(int audioId) {
        if (authToken == null) {
            Toast.makeText(this, "未登录或认证失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 发送请求获取音频详情
        NetworkApi.createService(Api.class)
                .getAudioDetails(audioId, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AudioDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 显示加载指示器
                    }

                    @Override
                    public void onNext(AudioDetailsResponse response) {
                        if (response.getCode() == 200) {
                            AudioDetailsResponse.Data data = response.getData();
                            if (data.getFind() == 1) {
                                AudioDetails audioDetails = data.getAudio();
                                updatePlayerInfo(audioDetails);
                                playAudio(audioDetails.getUrl());
                            } else {
                                Toast.makeText(MusicActivity.this, "未找到对应章节", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MusicActivity.this, "请求失败: " + response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 处理错误
                        Toast.makeText(MusicActivity.this, "请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // 隐藏加载指示器
                    }
                });
    }

    private void updatePlayerInfo(AudioDetails audioDetails) {
        if (audioDetails != null) {
            // 更新歌曲标题
            songName.setText(audioDetails.getTitle());
            // 更新播放次数
            tvPlayCount.setText(String.valueOf(audioDetails.getPlayNums()));
            // 更新点赞数量
            tvThumb.setText(String.valueOf(audioDetails.getThumbNums()));

            // 更新内容显示
            TextView contentTextView = findViewById(R.id.tv_content);
            contentTextView.setText(audioDetails.getContent());
        }
    }

    private void playAudio(String audioUrl) {
        // 使用 MusicService 来处理音频播放
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("audioUrl", audioUrl);
        startService(intent);
    }

    public void updatePlayCount(int playCount) {
        tvPlayCount.setText(String.valueOf(playCount));
    }

    private String audioFilePath;

    //点读
    private void uploadAudioAndGetResponse(String recognizedText) {
        File audioFile = new File(audioFilePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/mp3"), audioFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);

        NetworkApi.createService(Api.class)
                .uploadAudio(authToken, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AudioDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 显示加载指示器
                    }

                    @Override
                    public void onNext(AudioDetailsResponse response) {
                        if (response.getCode() == 200) {
                            if (response.getData().getFind() == 1) {
                                AudioDetails audioDetails = response.getData().getAudio();
                                updatePlayerInfo(audioDetails);
                                playAudio(audioDetails.getUrl());
                            } else {
                                Toast.makeText(MusicActivity.this, "未找到对应章节", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MusicActivity.this, "请求失败: " + response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 处理错误
                        Toast.makeText(MusicActivity.this, "请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // 隐藏加载指示器
                    }
                });
    }

}

