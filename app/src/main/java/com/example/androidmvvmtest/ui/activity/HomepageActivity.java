package com.example.androidmvvmtest.ui.activity;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.base.BaseApplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

public class HomepageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.activity_homepage);

        // 获取按钮控件
        ImageButton playButton = findViewById(R.id.imageButton);

        // 设置按钮点击事件
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 拦截
                if (BaseApplication.token.isEmpty()){
                    Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(HomepageActivity.this, ChooseActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
