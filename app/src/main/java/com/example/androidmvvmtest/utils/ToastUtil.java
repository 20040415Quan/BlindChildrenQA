package com.example.androidmvvmtest.utils;

import android.widget.Toast;

import com.example.androidmvvmtest.base.BaseApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/4/4 0:16
 * @Description: Toast工具类
 */
public class ToastUtil {

    /**
     * 短时间Toast
     * @param message 提示的消息
     */
    public static void sortToast(CharSequence message) {
        if (message==null){
            return;
        }
        Toast.makeText(BaseApplication.sContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间Toast
     * @param message 提示的消息
     */
    public static void longToast(CharSequence message) {
        if (message==null){
            return;
        }
        Toast.makeText(BaseApplication.sContext, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义展示时间的Toast
     * @param message 提示的消息
     * @param time 展示的时间
     */
    public static void customToast(CharSequence message,long time){
        if (message==null){
            return;
        }
        //构造Toast
        Toast toast = Toast.makeText(BaseApplication.sContext, message, Toast.LENGTH_LONG);
        //新建一个timer展示Toast
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        //延时关闭Toast和上一个Timer
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        },time);
    }
}
