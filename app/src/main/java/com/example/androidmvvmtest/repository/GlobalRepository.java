package com.example.androidmvvmtest.repository;



import android.util.Log;

import com.example.androidmvvmtest.base.BaseApplication;
import com.example.androidmvvmtest.bean.BaseResponse;
import com.example.androidmvvmtest.bean.VideoListBean;
import com.example.androidmvvmtest.bean.VideoListRequestBean;
import com.example.androidmvvmtest.network.Interface.Api;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.google.gson.Gson;



import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class GlobalRepository {
    //获取音频列表
    public static VideoListBean videoList = null;

    public static Observable<BaseResponse<VideoListBean>> getVideoList(int current, int size){
        VideoListRequestBean bean = new VideoListRequestBean();
        bean.current = current;
        bean.size = size;
        return NetworkApi.createService(Api.class).getVideList(
                BaseApplication.token,
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        new Gson().toJson(bean))
        );


    }


}
