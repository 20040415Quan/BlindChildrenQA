package com.example.androidmvvmtest.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidmvvmtest.R;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/4/15 13:49
 * @Description: 加载对话框
 */
public class LoadingDialog extends Dialog {

    TextView tvLoadingTx;
    ImageView ivLoading;

    public LoadingDialog(Context context, boolean isCanTouchOutsideCancel, boolean isCanBackCancel) {
        this(context, R.style.loading_dialog, context.getString(R.string.loading), isCanTouchOutsideCancel, isCanBackCancel);
    }

    public LoadingDialog(Context context, String string, boolean isCanTouchOutsideCancel, boolean isCanBackCancel) {
        this(context, R.style.loading_dialog, string, isCanTouchOutsideCancel, isCanBackCancel);
    }

    protected LoadingDialog(Context context, int theme, String string, boolean isCanTouchOutsideCancel, boolean isCanBackCancel) {
        super(context, theme);
        //点击外部关闭
        setCanceledOnTouchOutside(isCanTouchOutsideCancel);
        //加载布局
        setContentView(R.layout.dialog_loading);
        tvLoadingTx = findViewById(R.id.tv_loading_tx);
        tvLoadingTx.setText(string);
        ivLoading = findViewById(R.id.iv_loading);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        ivLoading.startAnimation(hyperspaceJumpAnimation);
        //居中显示
        getWindow().getAttributes().gravity = Gravity.CENTER;
        //背景透明度  取值范围 0 ~ 1
        getWindow().getAttributes().dimAmount = 0.5f;
        // 拦截返回键
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && isCanBackCancel) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    //关闭弹窗
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
