package com.example.net;


import okhttp3.Response;

/**
 * Created by magic_chen_ on 2018/9/5.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public abstract class INetCallback<R> {

    /**
     * UI Thread
     *
     * @param e
     */
    public abstract void onFail(Exception e);

    /**
     * UI Thread
     *
     * @param response
     */
    public abstract void onSuccess(R response);


    /**
     * UI Thread
     *
     * @param progress  total
     */
    public void inProgress(float progress, long total ){

    }
}
