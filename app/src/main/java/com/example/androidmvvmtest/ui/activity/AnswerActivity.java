package com.example.androidmvvmtest.ui.activity;

import static com.example.androidmvvmtest.base.BaseApplication.token;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.base.BaseApplication;
import com.example.androidmvvmtest.databinding.ActivityAnswerBinding;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.bean.response.ChatRecordsResponse;
import com.example.androidmvvmtest.network.bean.response.QuestionResponse;
import com.example.androidmvvmtest.network.bean.response.VoiceToTextResponse;
import com.example.androidmvvmtest.network.observer.BaseObserver;
import com.example.androidmvvmtest.ui.adapter.AnswerChatListRecAdapter;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.ToastUtil;
import com.example.androidmvvmtest.view.dialog.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AnswerActivity extends BaseActivity {

    private com.example.androidmvvmtest.databinding.ActivityAnswerBinding mBinding;
    /**
     * 录音文件的根目录
     */
    private String recordRootPath = "";
    /**
     * 录音文件的路径
     */
    private String recordFilePath = "";
    /**
     * 录音对象
     */
    private MediaRecorder recorder;
    /**
     * 初始Y位置
     */
    private float initialY = 0;
    /**
     * 是否正在录音
     */
    private boolean isRecording = false;
    /**
     * 聊天列表适配器
     */
    private AnswerChatListRecAdapter answerChatListRecAdapter = new AnswerChatListRecAdapter();
    /**
     * 播放器
     */
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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

        //最早先设置适配器，可以抽到一个方法里面
        mBinding.recCartList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recCartList.setAdapter(answerChatListRecAdapter);

        initClick();
        loadChatList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    /**
     * 点击事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initClick() {
        //录音按钮，长按录音，松开停止录音，录音文件保存在本地，上滑取消录音
        mBinding.btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isRecording = true;
                        // 记录初始Y位置，开始录音
                        initialY = motionEvent.getY();
                        record();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // 如果用户向上滑动超过一定阈值，取消录音
                        if (motionEvent.getY() < initialY - 100) {
                            if (recorder != null) {
                                recorder.stop();
                                recorder.release();
                                recorder = null;
                                isRecording = false;
                                showCustomMsg("取消录音",1000);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isRecording) {
                            isRecording = false;
                            // 停止录音
                            stopRecord();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        //发送按钮
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBinding.edtTextQuestion.getText().toString().trim().isEmpty()) {
                    showCustomMsg("请输入问题",1000);
                    return;
                }

                //发送问题
                mBinding.edtTextQuestion.clearFocus();
                sendQuestion(mBinding.edtTextQuestion.getText().toString().trim(), new OnSendQuestionCallback() {
                    @Override
                    public void onResult(Boolean isSuccess, QuestionResponse.Data result) {
                        if (isSuccess) {
                            mBinding.edtTextQuestion.setText("");
                            //播放在线语音
                            if (result.getAudioPath() != null) {
                                playVoice(result.getAudioPath());
                            }
                            //发送问题成功，刷新聊天列表
                            loadChatList();
                        } else {
                            //请求后端失败
                            ToastUtil.sortToast("发送问题失败,请重试");
                        }
                    }
                });
            }
        });
    }

    /**
     * 录音流程
     */
    private void record() {
        if (!XXPermissions.isGranted(this, Permission.RECORD_AUDIO)) {
            requestPermission(new OnPermissionCallback() {
                @Override
                public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                    if (!allGranted) {
                        showCustomMsg("请先授予录音权限才可以使用录音功能",1000);
                    }
                }
            });
        } else {
            createRecordRootFile();
            if (createNewSaveFile(UUID.randomUUID().toString() + ".aac")) {
                startRecord();
            } else {
                showCustomMsg("创建录音文件失败,请重试",1000);
            }
        }
    }

    /**
     * 0.请求权限
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
     * 1.创建保存录音文件的根目录
     */
    private void createRecordRootFile() {
        //使用临时缓存文件夹，系统会自动清理
        File file = new File(getCacheDir(), "records");
        if (!file.exists()) {
            //创建文件夹
            boolean result = file.mkdirs();
            if (result) {
                //创建成功
                Log.i("TAGG", "第一次创建录音文件夹成功" + file.getAbsolutePath());
                recordRootPath = file.getAbsolutePath();
            } else {
                //创建失败
                Log.i("TAGG", "第一次创建录音文件夹失败");
            }
        } else {
            //文件夹已经存在
            recordRootPath = file.getAbsolutePath();
            Log.i("TAGG", "录音文件夹已经存在" + file.getAbsolutePath());
        }
    }

    /**
     * 2.创建新的保存文件，在启动录音前必须新建好
     *
     * @param fileName 文件名 如：test.aac
     */
    private Boolean createNewSaveFile(String fileName) {
        if (recordRootPath.isEmpty()) {
            Log.i("TAGG", "录音文件夹不存在");
            return false;
        }
        //使用临时缓存文件夹，系统会自动清理
        File newFile = new File(recordRootPath, fileName);
        try {
            if (!newFile.exists()) {
                boolean result;
                //创建文件
                result = newFile.createNewFile();
                if (result) {
                    //创建成功
                    Log.i("TAGG", "创建新文件夹成功" + newFile.getAbsolutePath());
                    recordFilePath = newFile.getAbsolutePath();
                } else {
                    //创建失败
                    Log.i("TAGG", "第一次创建文件夹失败");
                    return false;
                }
            }
        } catch (IOException e) {
            Log.i("TAGG", "创建新文件异常" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 3.启动录音
     */
    @SuppressLint("SetTextI18n")
    private void startRecord() {
        //必须先暂停上次录音，不然不给再次调用
        if (recorder != null) {
            return;
        }
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
        recorder.setOutputFile(recordFilePath);

        //准备并开始
        try {
            ToastUtil.sortToast("开始录音，松手停止录音，上滑取消录音");
            recorder.prepare();
            recorder.start();
            Log.i("TAGG", "开始录音" + recordFilePath);
        } catch (IOException e) {
            Log.i("TAGG", "启动录音异常" + e.getMessage());
            ToastUtil.sortToast("启动录音异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 4.停止录音
     */
    @SuppressLint("SetTextI18n")
    private void stopRecord() {
        if (recorder != null) {
            Log.i("TAGG", "停止录音"+recordFilePath);
            recorder.stop();
            recorder.release();
            recorder = null;
            //显示加载框
            showLoading(false, false);
            //子线程等待然后上传文件
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    //开始上传文件
                    transformRecordsToText(recordFilePath, new OnTransformRecordsToTextCallback() {
                        @Override
                        public void onResult(Boolean isSuccess, String result) {
                            if (isSuccess) {
                                //请求后端成功，继续发送问题
                                sendQuestion(result, new OnSendQuestionCallback() {
                                    @Override
                                    public void onResult(Boolean isSuccess, QuestionResponse.Data result) {
                                        if (isSuccess) {
                                            //发送问题成功，刷新聊天列表
                                            loadChatList();
                                            //请求后端成功，显示结果
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ToastUtil.sortToast("发送问题成功" + result.getAnswer());
                                                }
                                            });
                                        } else {
                                            //请求后端失败
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ToastUtil.sortToast("发送问题失败");
                                                    dismissLoading();
                                                }
                                            });
                                        }
                                    }
                                });
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.sortToast("录音转文字成功：" + result);
                                    }
                                });
                            } else {
                                //请求后端失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.sortToast(result);
                                        dismissLoading();
                                    }
                                });
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 加载聊天列表
     */
    @SuppressLint("CheckResult")
    private void loadChatList() {
        showLoading(false, false);
        //检查token
        if (!token.isEmpty()) {
            //发送请求
            NetworkApi.createService(Api.class)
                    .getChatRecords(token, 0, 10)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<ChatRecordsResponse>() {
                        @Override
                        public void onSuccess(ChatRecordsResponse chatRecordsResponse) {
                            if (chatRecordsResponse != null && chatRecordsResponse.getCode() == 200) {
                                initChatListRec(chatRecordsResponse);
                                //加载成功
                                dismissLoading();
                            } else {
                                showCustomMsg("加载聊天列表失败",1000);
                                dismissLoading();
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            showCustomMsg("加载聊天列表失败",1000);
                            dismissLoading();
                        }
                    }));
        }
    }

    /**
     * 初始化Rec
     *
     * @param chatRecordsResponse
     */
    private void initChatListRec(ChatRecordsResponse chatRecordsResponse) {
        if (chatRecordsResponse.getData() != null) {
            mBinding.viewEmpty.setVisibility(View.GONE);
            mBinding.recCartList.setVisibility(View.VISIBLE);
            //设置数据进入适配器
            answerChatListRecAdapter.setList(chatRecordsResponse.getData().getData());
        } else {
            mBinding.viewEmpty.setVisibility(View.VISIBLE);
            mBinding.recCartList.setVisibility(View.GONE);
        }
    }

    //region 语音转文字

    /**
     * 将语音文件发送到后端转换为文字
     */
    @SuppressLint("CheckResult")
    private void transformRecordsToText(String filePath, OnTransformRecordsToTextCallback callback) {
        //检查token
        if (!token.isEmpty()) {
            //检查文件存在
            File file = new File(filePath);
            if (!file.exists()) {
                callback.onResult(false, "文件不存在");
                return;
            }
            // 上传文件
            //表单发送文件，构建RequestBody
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //构建MultipartBody.Part，其中需要注意的是，传入的参数file需要和服务器约定好Key，这里是file
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), body);
            //构建请求
            NetworkApi.createService(Api.class)
                    .voiceToText(
                            token,
                            filePart
                    ).compose(NetworkApi.applySchedulers(new BaseObserver<VoiceToTextResponse>() {
                        @Override
                        public void onSuccess(VoiceToTextResponse voiceToTextResponse) {
                            if (voiceToTextResponse != null && voiceToTextResponse.getCode() == 200) {
                                callback.onResult(true, voiceToTextResponse.getData());
                            } else {
                                if (voiceToTextResponse != null && voiceToTextResponse.getMsg() != null) {
                                    callback.onResult(false, voiceToTextResponse.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            callback.onResult(false, "转换失败");
                        }
                    }));
        }
    }

    public interface OnTransformRecordsToTextCallback {
        /**
         * 转换结果
         *
         * @param isSuccess 是否成功
         * @param result    结果
         */
        void onResult(Boolean isSuccess, String result);
    }

    //endregion

    //region 问答，发送问题返回结果

    /**
     * 发送问题
     */
    @SuppressLint("CheckResult")
    private void sendQuestion(String question, OnSendQuestionCallback callback) {
        showLoading(false, false);
        //检查token
        if (!token.isEmpty()) {
            //发送请求
            NetworkApi.createService(Api.class)
                    .postQuestion(token, question)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<QuestionResponse>() {
                        @Override
                        public void onSuccess(QuestionResponse questionResponse) {
                            if (questionResponse != null && questionResponse.getCode() == 200) {
                                callback.onResult(true, questionResponse.getData());
                                dismissLoading();
                            } else {
                                callback.onResult(false, null);
                                dismissLoading();
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            callback.onResult(false, null);
                            dismissLoading();
                        }
                    }));
        }
    }

    public interface OnSendQuestionCallback {
        /**
         * 发送结果
         *
         * @param isSuccess 是否成功
         * @param result    结果
         */
        void onResult(Boolean isSuccess, QuestionResponse.Data result);
    }

    /**
     * 播放在线语音
     * @param audioPath
     */
    private void playVoice(String audioPath) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion

}