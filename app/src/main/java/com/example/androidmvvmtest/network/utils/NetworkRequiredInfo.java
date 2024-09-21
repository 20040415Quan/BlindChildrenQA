package com.example.androidmvvmtest.network.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe: 网络访问信息实现类
 */
public class NetworkRequiredInfo implements INetworkRequiredInfo {

    private final Application application;

    public NetworkRequiredInfo(Application application) {
        this.application = application;
    }

    /**
     * 版本名
     * 如：1.0
     */
    @Override
    public String getAppVersionName() {
        try {
            PackageInfo packageInfo =
                    getApplicationContext().getPackageManager()
                            .getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 版本号
     * 如：1
     */
    @Override
    public String getAppVersionCode() {
        try {
            PackageInfo packageInfo =
                    getApplicationContext().getPackageManager()
                            .getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1 + "";
        }
    }

    /**
     * 是否为debug
     * 暂时不知道哪个是对的，先用博主默认的
     */
    @Override
    public boolean isDebug() {
        return (getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        //return BuildConfig.DEBUG;
    }

    /**
     * 应用全局上下文
     */
    @Override
    public Application getApplicationContext() {
        return application;
    }
}
