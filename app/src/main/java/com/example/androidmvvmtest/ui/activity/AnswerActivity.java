package com.example.androidmvvmtest.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.base.BaseApplication;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.Interface.XunFeiCallbackListener;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.bean.response.QuestionResponse;
import com.example.androidmvvmtest.network.bean.response.VoiceToTextResponse;
import com.example.androidmvvmtest.network.observer.BaseObserver;
import com.example.androidmvvmtest.utils.Constant;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.MVUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AnswerActivity extends BaseActivity  {

    //是否正在录音
    private boolean isRecording = false;
    //文件夹路径
    private String filePath = "";
    //录音器
    MediaRecorder recorder;
    //本次录音文件名
    String fileName = "";

    private ImageButton btn_click;
    private ImageButton btn_send;
    private EditText mResultText;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        btn_click = findViewById(R.id.btn_click);
        btn_send = findViewById(R.id.btn_send);
        mResultText = findViewById(R.id.result);
        tvMessage = findViewById(R.id.tv_message);
        test();



        //底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_left:
                    // 处理左侧图标点击事件
                    return true;
                case R.id.navigation_right:
                    // 处理右侧图标点击事件
                    Intent intent = new Intent(AnswerActivity.this, MusicActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        });
    }


    private void test() {
        //0.最早先创建好保存录音文件的路径
        createSaveFile();
        //1.请求权限
        requestPermission(new OnPermissionCallback() {
            @Override
            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                //2.权限通过
                if (allGranted) {
                    btn_click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isRecording) {
                                //3.正在录音，停止并保存
                                isRecording = false;
                                stopRecord();
                            } else {
                                //4.未在录音就进行录音
                                isRecording = true;
                                //TODO 根据格式修改后缀
                                startRecord(UUID.randomUUID().toString() + ".aac");

                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 请求权限
     */
    private void requestPermission(OnPermissionCallback onPermissionCallback) {
        if (onPermissionCallback == null) {
            return;
        }
        //请求录音权限
        XXPermissions.with(this).permission(Permission.RECORD_AUDIO)
                .request(onPermissionCallback);
    }

    /**
     * 创建保存文件的保存文件夹路径，必须开始就调用
     */
    private void createSaveFile() {
        //使用临时缓存文件夹，系统会自动清理
        File file = new File(getCacheDir(), "records");
        if (!file.exists()) {
            //创建文件夹
            boolean result = file.mkdirs();
            if (result) {
                //创建成功
                Log.i("TAGG", "第一次创建文件夹成功" + file.getAbsolutePath());
                filePath = file.getAbsolutePath();
                tvMessage.setText("第一次创建文件夹成功"+file.getAbsolutePath());
            } else {
                //创建失败
                Log.i("TAGG", "第一次创建文件夹失败");
                tvMessage.setText("第一次创建文件夹失败");
            }
        } else {
            //文件夹已经存在
            filePath = file.getAbsolutePath();
            Log.i("TAGG", "文件夹已经存在" + file.getAbsolutePath());
            tvMessage.setText("文件夹已经存在"+file.getAbsolutePath());
        }
    }

    /**
     * 创建新的保存文件，在启动录音前必须新建好
     *
     * @param fileName 文件名 如：test.aac
     */
    private Boolean createNewSaveFile(String fileName) {
        //使用临时缓存文件夹，系统会自动清理
        File newFile = new File(filePath, fileName);
        try {
            if (!newFile.exists()) {
                boolean result;
                //创建文件
                result = newFile.createNewFile();
                if (result) {
                    //创建成功
                    Log.i("TAGG", "创建新文件夹成功" + newFile.getAbsolutePath());
                } else {
                    //创建失败
                    Log.i("TAGG", "第一次创建文件夹失败");
                    return false;
                }
            }
        } catch (IOException e) {
            Log.i("TAGG", "创建新文件异常" + e.getMessage());
            tvMessage.setText("创建新文件异常"+e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 启动录音，需要文件名的详细后缀
     *
     * @param fileName 如：test.aac
     */
    @SuppressLint("SetTextI18n")
    private void startRecord(String fileName) {
        //必须先暂停上次录音，不然不给再次调用
        if (recorder != null) {
            return;
        }
        //先创建新的文件
        createNewSaveFile(fileName);
        //不同版本要求不同
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = new MediaRecorder(this);
        } else {
            recorder = new MediaRecorder();
        }

        //设置音频来源，MIC麦克风,VOICE_RECOGNITION语音识别优化使用，但可能不可用
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        } catch (Exception e) {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        //设置音频输出格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        //设置编码器
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置输出文件
        recorder.setOutputFile(new File(filePath, fileName).getAbsolutePath());

        //准备并开始
        try {
            recorder.prepare();
            recorder.start();
            Log.i("TAGG", "开始录音");
            tvMessage.setText("开始录音，录音文件：" + fileName);
            this.fileName = fileName;
        } catch (IOException e) {
            Log.i("TAGG", "启动录音异常" + e.getMessage());
            tvMessage.setText("启动录音异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 停止录音
     */
    @SuppressLint("SetTextI18n")
    private void stopRecord() {
        if (recorder != null) {
            Log.i("TAGG", "停止录音");
            tvMessage.setText("停止录音，录音文件：" + fileName);
            recorder.stop();
            recorder.release();
            recorder = null;

            //TODO 后续上传文件，但是需要延时上传，因为录音文件可能还在写入

            // 上传文件
            File audioFile = new File(filePath, fileName);
            String token =  BaseApplication.token;
            uploadAudioFile(token, audioFile);
        }
    }

    private void uploadAudioFile(String token, File audioFile) {
        if (audioFile == null || !audioFile.exists()) {
            Toast.makeText(AnswerActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/aac"), audioFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);



        // 发起请求
        NetworkApi.createService(Api.class)
                .voiceToText(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<VoiceToTextResponse>() {
                    @Override
                    public void onSuccess(VoiceToTextResponse response) {
                        KLog.i("TAGG", "uploadAudioFile" + response.toString());
                        if (response.getCode() == 200) {
                            mResultText.setText(response.getData());
                            Toast.makeText(AnswerActivity.this, "转换成功: " + response.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AnswerActivity.this, "转换失败: " + response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        KLog.i("TAGG", "uploadAudioFile" + e.toString());
                        Toast.makeText(AnswerActivity.this, "上传失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}