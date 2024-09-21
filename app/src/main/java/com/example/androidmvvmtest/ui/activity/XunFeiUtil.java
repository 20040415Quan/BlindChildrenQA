package com.example.androidmvvmtest.ui.activity;

import android.content.Context;
import android.widget.Toast;

import com.example.androidmvvmtest.network.Interface.XunFeiCallbackListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class XunFeiUtil {

    public static String appid = "b55b1094"; // 替换为您的实际 app id
    private static SpeechRecognizer recognizer;

    public static void initXunFei(Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + appid);
        recognizer = SpeechRecognizer.createRecognizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(context, "SpeechRecognizer 初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "SpeechRecognizer 初始化成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void startVoice(Context context, final XunFeiCallbackListener callbackListener) {
        if (recognizer == null) {
            Toast.makeText(context, "SpeechRecognizer 未初始化", Toast.LENGTH_SHORT).show();
            return;
        }

        RecognizerDialog dialog = new RecognizerDialog(context, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(context, "RecognizerDialog 初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "RecognizerDialog 初始化成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setParameter(SpeechConstant.ASR_PTT, "0");
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                callbackListener.onFinish(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
                Toast.makeText(context, "语音识别错误: " + speechError.getErrorCode(), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}