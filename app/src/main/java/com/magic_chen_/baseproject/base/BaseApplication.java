package com.magic_chen_.baseproject.base;

import android.app.Application;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.screen_adaptation.AutoFrameLayout;
import com.example.screen_adaptation.AutoLinearLayout;
import com.example.screen_adaptation.AutoRelativeLayout;

public class BaseApplication  extends Application {

    public static BaseApplication sBaseApplication;
    public static BaseApplication getGlobalContext() {
        return  sBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sBaseApplication = this;
        initLayoutInflater();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    /**
     *
     * for autolayout
     *
     * */
    private LayoutInflater mLayoutInflater = null;
    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    private void initLayoutInflater() {
        mLayoutInflater = LayoutInflater.from(this);
        mLayoutInflater.setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return onMyCreateView(name, context, attrs);
            }
        });
    }

    public static View onMyCreateView(String name, Context context, AttributeSet attrs) {
        if (name.equals("LinearLayout")) {
            return new AutoLinearLayout(context, attrs);
        }

        if (name.equals("FrameLayout")) {
            return new AutoFrameLayout(context, attrs);
        }

        if (name.equals("RelativeLayout")) {
            return new AutoRelativeLayout(context, attrs);
        }


        if (name.equals("TextView")) {
            TextView textView = new TextView(context, attrs);
            return textView;
        }

        if (name.equals("EditText")) {
            EditText editText = new EditText(context, attrs);
            return editText;
        }

        if (name.equals("Button")) {
            Button button = new Button(context, attrs);
            return button;
        }

        return null;
    }
}
