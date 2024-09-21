package com.example.androidmvvmtest.network.utils;

import android.app.Application;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe: App运行信息接口，这里就是要在请求网络接口的时候打印当前的App的运行信息，可以根据实际的需求再进行一次补充。
 */
public interface INetworkRequiredInfo {
    /**
     * 获取App版本名
     */
    String getAppVersionName();

    /**
     * 获取App版本号
     */
    String getAppVersionCode();

    /**
     * 判断是否为Debug模式
     */
    boolean isDebug();

    /**
     * 获取全局上下文参数
     */
    Application getApplicationContext();
}
