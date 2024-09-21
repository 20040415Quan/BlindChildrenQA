package com.example.androidmvvmtest.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidmvvmtest.utils.ToastUtil;
import com.example.androidmvvmtest.view.dialog.LoadingDialog;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe: 基础Fragment
 */
public class BaseFragment extends Fragment {

    protected AppCompatActivity context;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 绑定到Activity
     *
     * @param context 上下文
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.context = (AppCompatActivity) context;
        }
    }

    /**
     * 未绑定到Activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
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
     * 显示加载弹窗
     */
    protected void showLoading() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true点击其他区域弹窗关闭，false不关闭
     */
    protected void showLoading(boolean isClose) {
        loadingDialog = new LoadingDialog(context, isClose);
        loadingDialog.show();
    }

    /**
     * 隐藏加载弹窗
     */
    protected void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

}
