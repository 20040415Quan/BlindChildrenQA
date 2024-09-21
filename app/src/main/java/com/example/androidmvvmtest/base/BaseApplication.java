package com.example.androidmvvmtest.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.example.androidmvvmtest.db.room.database.AppDatabase;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.utils.NetworkRequiredInfo;
import com.example.androidmvvmtest.ui.activity.ActivityManager;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.MVUtils;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;


/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe: 主application，入口 TODO 需要配置application什么操作就在这里配置
 */
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context sContext;

    //数据库
    public static AppDatabase db;

    //token
    public static String token = "";

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        //MMKV初始化和MVUtils工具类初始化
        String initialize = MMKV.initialize(sContext);
        KLog.i("TAGG","MMKV INIT " + initialize);//查看缓存文件
        MVUtils.getInstance();
        //创建本地数据库
//        db = AppDatabase.getInstance(sContext);
        //网络日志初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
        //腾讯WebView初始化
        initX5WebView();
        //token
        token = MVUtils.getString("token","");
        KLog.i("TAGG","application token"+token);
    }

    /**
     * 获取application context
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * 获取app的数据库对象
     *
     * @return db
     */
    public static AppDatabase getDb() {
        return db;
    }

    /**
     * 初始化X5WebView
     */
    private void initX5WebView() {
        HashMap map = new HashMap(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                KLog.i("TAGG", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 获取app的ActivityManager
     *
     * @return ActivityManager
     */
    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }

}
