package com.example.androidmvvmtest.ui.activity;

import static com.example.androidmvvmtest.base.BaseApplication.token;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.base.BaseActivity;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.bean.response.ChatRecordsResponse;
import com.example.androidmvvmtest.ui.adapter.ChatAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChatActivity extends BaseActivity {
    private ListView chatListView;
    private ChatAdapter chatAdapter;
    private Disposable chatRecordsDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatListView = findViewById(R.id.chat_listview);
        chatListView.setDivider(null); // 移除 ListView 的默认分割线

        // 初始化适配器
        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        chatListView.setAdapter(chatAdapter);

        // 获取聊天记录
        getChatRecords(token,1, 10); // 例如获取第1页，每页10条记录
    }

    private void getChatRecords(String token,int current, int size) {
        if (chatRecordsDisposable != null && !chatRecordsDisposable.isDisposed()) {
            chatRecordsDisposable.dispose(); // 取消之前的订阅
        }
        NetworkApi.createService(Api.class)
                .getChatRecords(token,current, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChatRecordsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        chatRecordsDisposable = d; // 保存订阅
                    }

                    @Override
                    public void onNext(ChatRecordsResponse response) {
                        if (response.getCode() == 200) {
                            List<ChatRecordsResponse.Data.Record> records = response.getData().getRecords();
                            chatAdapter.clear();
                            chatAdapter.addAll(records);
                            chatAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ChatActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ChatActivity.this, "Network Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // 完成时调用
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatRecordsDisposable != null && !chatRecordsDisposable.isDisposed()) {
            chatRecordsDisposable.dispose(); // 取消订阅
        }
    }
}