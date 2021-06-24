package com.example.net;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;




public interface Globals {
    Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    Gson GSON = new Gson();

}
