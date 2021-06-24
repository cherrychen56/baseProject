package com.magic_chen_.baseproject.base;

import android.os.Handler;

import java.lang.ref.WeakReference;

class BaseHandler<T>  extends Handler {

    public WeakReference<T> mModel;

    public BaseHandler(T t) {
        mModel = new WeakReference<T>(t);
    }
}
