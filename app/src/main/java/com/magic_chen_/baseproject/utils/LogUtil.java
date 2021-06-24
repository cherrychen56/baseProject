package com.magic_chen_.baseproject.utils;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.magic_chen_.baseproject.GlobalApplication;


public class LogUtil {


    public static void d(String tag, String content) {
        if (isApkDebugable()) {
            Log.d(tag, content);
        }
    }

    public static boolean isApkDebugable() {
        //debug 返回true  release 返回false
        boolean ret = false;
        try {
            ApplicationInfo info = GlobalApplication.getGlobalContext().getApplicationInfo();
            ret = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            return ret;
        } catch (Exception e) {
           return  false;
        }

    }

}
