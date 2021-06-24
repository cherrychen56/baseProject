/**
 * 
 */
package com.magic_chen_.baseproject.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.magic_chen_.baseproject.GlobalApplication;
import com.magic_chen_.baseproject.config.AppConfig;


public class ToastUtil {

    private static String oldMsg;
    private static long time;

    public static void showToast(String msg) {
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(GlobalApplication.getGlobalContext(), msg, Toast.LENGTH_LONG).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(GlobalApplication.getGlobalContext(), msg, Toast.LENGTH_LONG).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }
	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
	public static void show(String info) {
        showToast(info);
//		Toast.makeText(MyApplication.getContext(), info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
	

    private static void logError(String info, int errorCode) {
        print(LINE);//start
        print("                                   错误信息                                     ");
        print(LINE);//title
        print(info);
        print("错误码: " + errorCode);
        print("                                                                               ");
        print("如果需要更多信息，请根据错误码到以下地址进行查询");
        print("  http://lbs.amap.com/api/android-sdk/guide/map-tools/error-code/");
        print("如若仍无法解决问题，请将全部log信息提交到工单系统，多谢合作");
        print(LINE);//end
    }

    //log
    public static final String TAG = "AMAP_ERROR";
    static final String LINE_CHAR="=";
    static final String BOARD_CHAR="|";
    static final int LENGTH = 80;
    static String LINE;
    static{
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<LENGTH;i++){
            sb .append(LINE_CHAR);
        }
        LINE = sb.toString();
    }


    private static void printLog(String s){
        if(s.length()<LENGTH-2){
            StringBuilder sb = new StringBuilder();
            sb.append(BOARD_CHAR).append(s);
            for(int i = 0 ;i <LENGTH-2-s.length();i++){
                sb.append(" ");
            }
            sb.append(BOARD_CHAR);
            print(sb.toString());
        }else{
            String line = s.substring(0,LENGTH-2);
            print(BOARD_CHAR+line+BOARD_CHAR);
            printLog(s.substring(LENGTH-2));
        }
    }

    private static void print(String s) {
        Log.i(TAG,s);
    }

    public static void v(String tag, String msg) {
        if (AppConfig.IS_SHOW_LOG) {
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg) {
        if (AppConfig.IS_SHOW_LOG) {
            Log.d(tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if (AppConfig.IS_SHOW_LOG) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if (AppConfig.IS_SHOW_LOG) {
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if (AppConfig.IS_SHOW_LOG) {
            Log.e(tag, msg);
        }
    }
}
