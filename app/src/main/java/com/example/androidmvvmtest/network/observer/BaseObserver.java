package com.example.androidmvvmtest.network.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/15
 * @Discribe: 继承rxjava的Observer，只需要onSuccess和onFailure
 */
public abstract class BaseObserver<T> implements Observer<T> {
    //开始
    @Override
    public void onSubscribe(Disposable d) {

    }

    //继续
    //这里我并没有重写Observer的所有方法，只用了两个，onNext和onError。
    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    //异常
    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    //完成
    @Override
    public void onComplete() {

    }

    //成功
    public abstract void onSuccess(T t);

    //失败
    public abstract void onFailure(Throwable e);



}
