package com.magic_chen_.baseproject.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.magic_chen_.baseproject.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


/**
 * Created by leed on 2016/12/12.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    protected P mPresenter;
    public Bundle mSavedInstanceState;
    protected FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentID(), container, false);
//        View rootView = ViewMapUtil.map(this, getContentID(), inflater);
        mSavedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            fragmentManager = getChildFragmentManager();//重新创建Manager，防止此对象为空
            fragmentManager.popBackStackImmediate(null, 1);//弹出所有fragment
        }else{
            fragmentManager = getChildFragmentManager();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initFirst();
        if (mPresenter == null) {
            mPresenter = creatPresenter();
        }
        addListeners();
        setOther();
    }

    protected void setOther() {

    }

    protected P creatPresenter() {
        return null;
    }
    protected abstract int getContentID();

    protected abstract void initFirst();

    protected void addListeners() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        dismissProgress();
        if (mPresenter != null) {
            mPresenter.Detach();
        }
        super.onDestroy();
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 进度条的显示
     */
    protected void showProgress() {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity ba = (BaseActivity) getActivity();
        }
    }

    /**
     * 进度条的隐藏
     */
    public void dismissProgress() {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity ba = (BaseActivity) getActivity();
        }
    }





    //    public void account_tape_out(String msg){
//        dismissProgress();
//        App.app.stopService();
//        ReminderDialog reminderDialog = new ReminderDialog(getActivity(), 12);
//        reminderDialog.show();
//        reminderDialog.setMyListener(new ReminderDialog.MyListener() {
//            @Override
//            public void OnMyListener(int flag) {
//                startActivity(new Intent(getActivity(),LoadActivity.class));
//                getActivity().finish();
//            }
//        });
//    }

}
