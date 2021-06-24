package com.magic_chen_.baseproject.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;


import com.magic_chen_.baseproject.GlobalApplication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by magic_chen_ on 2018/8/29.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public class MessageCodeUtil {
    private static MessageCodeUtil singleton;

    private MessageCodeUtil() {
    }

    public static MessageCodeUtil getInstance() {
        if (singleton == null)
            singleton = new MessageCodeUtil();
        return singleton;
    }

    private static final int MSG_UPDATE_TIME = 888;
    private static final int MAX_TIME_SECOND = 60;
    private static final int MIN_TIME_SECOND = 0;
    private static final long HANDLER_MSG_DELAY = 1000;
    private HashMap<String, ViewStatus> mViewStatuss;
    private int mCurrentStatus = STATUS_NOT_START;
    private int mCurrentSeconds = MIN_TIME_SECOND;
    private MyHandler mHandler;
    private static final int STATUS_STOP = 0;
    public static final int STATUS_ING = 1;
    private static final int STATUS_NOT_START = -1;


    public void addFragmentView(String fragmentString, TextView textView, TextView textView2, int type) {
        if (mViewStatuss == null) {
            mViewStatuss = new HashMap<>();
        }
        if (mViewStatuss.containsKey(fragmentString)) {
            mViewStatuss.remove(fragmentString);
        }
        mViewStatuss.put(fragmentString, new ViewStatus(textView, 60, textView2, type));
    }


    public void removeFragmentView(String fragmentString) {
        if (mViewStatuss == null)
            return;
        if (mViewStatuss.size() == 0) {
            stop();
            return;
        }
        if (!mViewStatuss.containsKey(fragmentString)) {
            return;
        }
        mViewStatuss.remove(fragmentString);
        if (mViewStatuss.size() == 0) {
            stop();
        }
    }

    public int getSignNumberStatus() {
        return mCurrentStatus;
    }

    public boolean isEnable(String fragmentString) {
        if (mViewStatuss == null || mViewStatuss.size() == 0) {
            return true;
        } else if (!mViewStatuss.containsKey(fragmentString)) {
            return true;
        } else {
            return false;
        }
    }

    public void start() {
        if (mCurrentStatus == STATUS_ING) {
            return;
        }
//        if (mTextViews.size() == 0)
//            return;
        if (mViewStatuss.size() == 0)
            return;
        if (mHandler == null) {
            mHandler = new MyHandler();
        }
        mCurrentStatus = STATUS_ING;
//        mCurrentSeconds = MAX_TIME_SECOND;
//        setViewsTextContentBusying();
        mHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    public void stop() {
        try {
            Iterator iter = singleton.mViewStatuss.entrySet().iterator();
            while (iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                ViewStatus value = (ViewStatus) entry.getValue();
//                if (value.mAnotherText != null) {
//                    value.mAnotherText.setTextColor(GlobalApplication.getGlobalContext().getResources().getColor(R.color.common_white_3));
//                    value.mAnotherText.setClickable(true);
//                }
//                if (value.type == ViewStatus.TYPE_1) {
//                    value.mTextView.setText(App.app.getResources().getString(R.string.string_get_message_code));
//                } else if (value.type == ViewStatus.TYPE_2) {
//                    value.mTextView.setText(App.app.getResources().getString(R.string.string_get_message_code_by_voice));
//                } else if (value.type == ViewStatus.TYPE_3) {
//                    value.mTextView.setText(App.app.getResources().getString(R.string.get_email_code));
//                }
//                value.mTextView.setTextColor(App.app.getResources().getColor(R.color.common_white_3));
            }
        } catch (Exception e) {
            Log.d("MessageCodeUtil", "remove  error:" + e.toString());
        }
        mCurrentStatus = STATUS_NOT_START;
        mCurrentSeconds = MIN_TIME_SECOND;
        if (mViewStatuss != null) {
            mViewStatuss.clear();
        }
        if (mHandler != null) {
            mHandler.removeMessages(MSG_UPDATE_TIME);
            mHandler = null;
        }

    }

    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_TIME) {
                if (singleton.mViewStatuss == null || singleton.mViewStatuss.size() == 0) {
                    return;
                }

                Iterator iter = singleton.mViewStatuss.entrySet().iterator();
                try {
//                    while (iter.hasNext()) {
//                        Map.Entry entry = (Map.Entry) iter.next();
//                        String key = (String) entry.getKey();
//                        ViewStatus value = (ViewStatus) entry.getValue();
//                        if (value.mCurrentTime <= 0) {
//                            value.mTextView.setTextColor(App.app.getResources().getColor(R.color.common_white_3));
//                            if (value.mAnotherText != null) {
//                                value.mAnotherText.setTextColor(App.app.getResources().getColor(R.color.common_white_3));
//                                value.mAnotherText.setClickable(true);
//                            }
//                            if (value.type == ViewStatus.TYPE_1) {
//                                value.mTextView.setText(App.app.getResources().getString(R.string.string_get_message_code));
//                            } else if (value.type == ViewStatus.TYPE_2) {
//                                value.mTextView.setText(App.app.getResources().getString(R.string.string_get_message_code_by_voice));
//                            } else if (value.type == ViewStatus.TYPE_3) {
//                                value.mTextView.setText(App.app.getResources().getString(R.string.get_email_code));
//                            }
//                            iter.remove();
//                        } else {
//                            String ss = App.app.getResources().getString(R.string.string_message_code_sended) + "(" + value.mCurrentTime + "s)";
//                            value.mTextView.setTextColor(App.app.getResources().getColor(R.color.white));
//                            value.mTextView.setText(ss);
//                            value.mCurrentTime--;
//                            if (value.mAnotherText != null) {
//                                value.mAnotherText.setTextColor(App.app.getResources().getColor(R.color.loading_account_white));
//                                value.mAnotherText.setClickable(false);
//                            }
//                        }
//                    }
                } catch (Exception e) {
                    Log.d("MessageCodeUtil", "remove  error:" + e.toString());
                }

                if (singleton.mViewStatuss.size() > 0) {
                    singleton.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, HANDLER_MSG_DELAY);
                } else {
                    singleton.stop();
                }
            }
        }
    }

    public static class ViewStatus {
        public TextView mTextView;
        public int mCurrentTime;
        public TextView mAnotherText;
        public int type;
        public static final int TYPE_1 = 1;
        public static final int TYPE_2 = 2;
        public static final int TYPE_3 = 3;

        public ViewStatus(TextView mTextView, int mCurrentTime, TextView mAnotherText, int type) {
            this.mTextView = mTextView;
            this.mCurrentTime = mCurrentTime;
            this.mAnotherText = mAnotherText;
            this.type = type;
        }
    }
}
