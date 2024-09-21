package com.example.androidmvvmtest.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.bean.BaseResponse;
import com.example.androidmvvmtest.bean.VideoListBean;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.observer.BaseObserver;
import com.example.androidmvvmtest.repository.GlobalRepository;
import com.example.androidmvvmtest.utils.Constant;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.MVUtils;

public class ChooseActivity extends BaseActivity {
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        KLog.i("TAGG","token:"+ MVUtils.getString(Constant.TOKEN));

        //提前缓存
        GlobalRepository.getVideoList(0,16).compose(
                NetworkApi.applySchedulers(new BaseObserver<BaseResponse<VideoListBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<VideoListBean> videoListBeanBaseResponse) {
                        if (videoListBeanBaseResponse!=null&&videoListBeanBaseResponse.code==200){
                            //保存到仓库
                            GlobalRepository.videoList = videoListBeanBaseResponse.data;
                            KLog.i("TAGG","Choose提前缓存list成功"+
                                    GlobalRepository.videoList.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                        KLog.i("提前缓存失败error:"+e.toString());
                    }
                })
        );


        // 获取按钮控件
        ImageButton playButton = findViewById(R.id.myButton_mark);

        // 设置按钮点击事件
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, AnswerActivity.class);
                startActivity(intent);

            }
        });

        // 获取按钮控件
        ImageButton playButton1 = findViewById(R.id.myButton_mark_2);

        // 设置按钮点击事件
        playButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, MusicActivity.class);
                startActivity(intent);

            }
        });




    }


}
