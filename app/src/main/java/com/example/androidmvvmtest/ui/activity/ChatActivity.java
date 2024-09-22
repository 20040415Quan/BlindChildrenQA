package com.example.androidmvvmtest.ui.activity;

import static com.example.androidmvvmtest.base.BaseApplication.token;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.databinding.ActivityAnswerBinding;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.bean.response.QuestionResponse;
import com.example.androidmvvmtest.network.bean.response.VoiceToTextResponse;
import com.example.androidmvvmtest.network.observer.BaseObserver;
import com.example.androidmvvmtest.utils.ToastUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatActivity extends BaseActivity {

    /**
     * Binding
     */
    private com.example.androidmvvmtest.databinding.ActivityAnswerBinding mBinding;

    /**
     * 本次录音的文件名
     */
    private String fileName = "";

    /**
     * 是否正在录音
     */
    private boolean isRecording = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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
                ToastUtil.customToast("未找到录音文件，请重新录音", 1000);
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
                                if (voiceToTextResponse != null&&voiceToTextResponse.getMsg()!=null){
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
        //检查token
        if (!token.isEmpty()) {
            //发送请求
            NetworkApi.createService(Api.class)
                    .postQuestion(token,question)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<QuestionResponse>() {
                        @Override
                        public void onSuccess(QuestionResponse questionResponse) {
                            if (questionResponse != null && questionResponse.getCode() == 200) {
                                callback.onResult(true, questionResponse.getData());
                            } else {
                                callback.onResult(false, null);
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            callback.onResult(false, null);
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

    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}