package com.magic_chen_.baseproject.presenter;



import java.util.HashMap;

/**
 * Created by "liulihu" on 2017/10/31 0031.
 * email:511421121@qq.com
 */

public class BasePresenter<T> {


    protected HashMap<String, String> params;
    protected T mvpView;


    public BasePresenter(T view) {
        this.mvpView = view;
    }


    public void Detach() {
        if (mvpView != null) {
            mvpView = null;
        }
    }

}
