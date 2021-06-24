package com.magic_chen_.baseproject.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;

import com.magic_chen_.baseproject.presenter.BasePresenter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.magic_chen_.baseproject.Globals.BUS;

abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    private ActivityHandler mActivityHandler;
    protected P mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        initView();
        initData();
        BUS.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActivityHandler != null) {
            mActivityHandler.removeCallbacksAndMessages(null);
        }
        BUS.unregister(this);
        if (mPresenter != null) {
            mPresenter.Detach();
        }
    }


    protected abstract void initData();


    protected abstract void initView();
    protected P createPresenter() {
        return null;
    }

    /**
     * 获取handler
     */
    protected ActivityHandler getHandler() {
        if (mActivityHandler == null)
            initHandler();
        return mActivityHandler;
    }

    /**
     * 在activity中重写此方法   即可处理handler 消息
     *
     * @param msg
     */
    protected void handelMessage(Message msg) {

    }

    /**
     * 初始化handler方法
     */
    protected void initHandler() {
        mActivityHandler = new ActivityHandler(this);
    }


    /**
     *
     */
    private static class ActivityHandler<T extends BaseActivity> extends BaseHandler<T> {

        public ActivityHandler(T t) {
            super(t);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            T t = mModel.get();
            if (t != null) {
                t.handelMessage(msg);
            }
        }
    }

    /**
     * 获取context
     */
    protected Context getContext() {
        return this.getApplicationContext();
    }
}
