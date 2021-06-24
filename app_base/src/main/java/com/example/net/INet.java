package com.example.net;

import java.util.HashMap;

/**
 * Created by magic_chen_ on 2018/9/5.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public interface INet {
    String onCreateUrl(String url);
    void onCreateParams(String url, HashMap<String, String> params);
}
