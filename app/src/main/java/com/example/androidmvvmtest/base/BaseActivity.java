package com.example.androidmvvmtest.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.utils.ToastUtil;
import com.example.androidmvvmtest.view.dialog.LoadingDialog;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe: 基础Activity，根据常使用到的功能进行添加，不需要太多
 */
public class BaseActivity extends AppCompatActivity {

    protected AppCompatActivity context;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        BaseApplication.getActivityManager().addActivity(this);
    }

    /**
     * 短时间展示消息
     *
     * @param msg 消息
     */
    protected void showSortMsg(CharSequence msg) {
        ToastUtil.sortToast(msg);
    }

    /**
     * 长时间展示消息
     *
     * @param msg 消息
     */
    protected void showLongMsg(CharSequence msg) {
        ToastUtil.sortToast(msg);
    }

    /**
     * 自定义时间展示消息
     *
     * @param msg  消息
     * @param time 时间
     */
    protected void showCustomMsg(CharSequence msg, long time) {
        ToastUtil.customToast(msg, time);
    }

    /**
     * 跳转页面
     *
     * @param clazz 目标页面
     */
    protected void jumpActivity(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
    }

    /**
     * 跳转页面并关闭当前页面
     *
     * @param clazz 目标页面
     */
    protected void jumpActivityFinish(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
        finish();
    }

    /**
     * 状态栏文字图标颜色
     *
     * @param dark 深色 false 为浅色
     */
    protected void setStatusBar(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 显示加载弹窗
     */
    protected void showLoading() {
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            return;
        }
        loadingDialog = new LoadingDialog(this,true,true);
        loadingDialog.show();
    }

    /**
     * 显示加载弹窗
     */
    protected void showLoading( boolean isCanTouchOutsideCancel, boolean isCanBackCancel) {
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            return;
        }
        loadingDialog = new LoadingDialog(this, isCanTouchOutsideCancel, isCanBackCancel);
        loadingDialog.show();
    }

    /**
     * 隐藏加载弹窗
     */
    protected void dismissLoading(){
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

    /**
     * 退出应用
     */
    protected void exitTheProgram() {
        BaseApplication.getActivityManager().finishAllActivity();
    }

}
