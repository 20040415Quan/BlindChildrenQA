package com.example.androidmvvmtest.ui.activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.base.BaseApplication;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.bean.request.LoginRequest;
import com.example.androidmvvmtest.network.bean.request.Request;
import com.example.androidmvvmtest.network.bean.response.LoginResponse;
import com.example.androidmvvmtest.network.bean.response.Result;
import com.example.androidmvvmtest.network.bean.response.Token;
import com.example.androidmvvmtest.network.observer.BaseObserver;
import com.example.androidmvvmtest.utils.Constant;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.MVUtils;
import com.google.gson.Gson;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity {

    private EditText emailEditText;
    private EditText codeEditText;
    private Button getCodeButton;
    private Button loginButton;

    private CountDownTimer countDownTimer;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email);
        codeEditText = findViewById(R.id.password1);
        getCodeButton = findViewById(R.id.get_password);
        loginButton = findViewById(R.id.login);

        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                startCountDown();
                sendCode(email);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String code = codeEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
                    Toast.makeText(MainActivity.this, "请输入邮箱和验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(email, code);
            }
        });
    }

    private void startCountDown() {
        getCodeButton.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCodeButton.setText(millisUntilFinished / 1000 + "秒后重发");
            }

            @Override
            public void onFinish() {
                getCodeButton.setEnabled(true);
                getCodeButton.setText("获取验证码");
            }
        }.start();
    }

    private void sendCode(String email) {
        Request request = new Request(email);
        Gson gson = new Gson();
        String json = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        NetworkApi.createService(Api.class)
                .sendCode(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Result<Token>>() {
                    @Override
                    public void onSuccess(Result<Token> result) {
                        KLog.i("TAGG","senCode"+result.toString());
                        if (result.getCode() == 200) {
                            Toast.makeText(MainActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                        KLog.i("TAGG","senCode"+e.toString());
                        Toast.makeText(MainActivity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void login(String email, String code) {
        LoginRequest loginRequest = new LoginRequest(email, code);

        NetworkApi.createService(Api.class)
                .login(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(loginRequest))) // 直接传递 LoginRequest 对象
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse result) {
                        if (result.getCode() == 200) {
                            MVUtils.put(Constant.TOKEN, result.getData().getToken());
                            BaseApplication.token = result.getData().getToken();
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            // 跳转到另一个界面
                            Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}