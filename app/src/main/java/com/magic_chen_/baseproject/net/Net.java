package com.magic_chen_.baseproject.net;

import android.text.TextUtils;

import com.example.net.INet;
import com.magic_chen_.baseproject.config.Const;

import java.util.HashMap;

public class Net  implements INet {
    @Override
    public String onCreateUrl(String url) {
        if(TextUtils.isEmpty(url)){
            return Const.BASE_URL;
        }else if(url.contains("http")){
            return url;
        } else
            return Const.BASE_URL + url;
    }

    @Override
    public void onCreateParams(String url, HashMap<String, String> params) {

    }
}
