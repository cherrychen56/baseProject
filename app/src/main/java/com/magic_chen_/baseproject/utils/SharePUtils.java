package com.magic_chen_.baseproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.magic_chen_.baseproject.GlobalApplication;


/**
 * Created by "liulihu" on 2017/11/2 0002.
 * email:511421121@qq.com
 * 启用新的首选项
 */
public class SharePUtils {

    private static SharePUtils mInstance;
    private final SharedPreferences sharedPreferences;

    private SharePUtils(String spName) {
        sharedPreferences = GlobalApplication.getGlobalContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public boolean clearSp(){
        return sharedPreferences.edit().clear().commit();
    }

    public static SharePUtils getInstance(String spName) {
        mInstance=null;
        mInstance = new SharePUtils(spName);
        return mInstance;
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key,false);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public boolean clearSpValue(String key) {
        return sharedPreferences.edit().remove(key).commit();
    }

    public void saveValue(String key, Object value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value).commit();
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value).commit();
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value).commit();
        } else {
            throw new RuntimeException("SP存储类型不支持");
        }
    }

}
