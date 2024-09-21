package com.example.androidmvvmtest.utils;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author     ：allen.wang
 * @date       ：2024/7/18 9:54
 * @description：BottomNavigationView底部导航工具类
 */
public class BottomNavigationViewUtils {
    /**
     * BottomNavigationView禁用Toast提示
     *
     * @param bottomNavigationView 控件
     */
    public static void disableNavViewLongClick(BottomNavigationView bottomNavigationView) {
        assert bottomNavigationView != null;
        final int childCount = bottomNavigationView.getChildCount();
        if (childCount < 0) {
            return;
        }
        final View childAtView = bottomNavigationView.getChildAt(0);
        if (!(childAtView instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) childAtView;
        int viewGroupChildCount = viewGroup.getChildCount();
        for (int i = 0; i < viewGroupChildCount; i++) {
            View v = viewGroup.getChildAt(i);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }
}
