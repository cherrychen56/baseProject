package com.example.net;

import android.util.Log;

import com.example.net.annotation.Download;
import com.example.net.annotation.Get;
import com.example.net.annotation.Json;
import com.example.net.annotation.KeyValue;
import com.example.net.annotation.NetCallback;
import com.example.net.annotation.Param;
import com.example.net.annotation.Post;
import com.example.net.annotation.TimeOut;
import com.example.net.annotation.Url;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * Created by magic_chen_ on 2018/9/5.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public class ApiInvokeHandler implements InvocationHandler {
    private final String TAG = "ApiInvokeHandler";
    public final INet mNet;
    private final int DEFAULT_TIMEOUT = 20;

    public ApiInvokeHandler(INet net) {
        mNet = net;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final Get get = method.getAnnotation(Get.class);
        final Post post = method.getAnnotation(Post.class);
        final Url url = method.getAnnotation(Url.class);
        final TimeOut timeOut = method.getAnnotation(TimeOut.class);
        final Json json = method.getAnnotation(Json.class);
        final KeyValue keyValue = method.getAnnotation(KeyValue.class);
        String urlString = "";
        if (url != null) {
            urlString = url.value();
        }
        Log.d(TAG, "" + urlString);
         Class<?> respClz = null;
        if(get!=null){
            respClz = get.value();
        }else if(post !=null){
            respClz = post.value();
        }else if(json!=null){
            respClz = json.value();
        }else if(keyValue !=null){
            respClz = keyValue.value();
        }
        int iTimeOut = DEFAULT_TIMEOUT;
        if (timeOut != null)
            iTimeOut = timeOut.value();
        AbstractNetBuilder builder = new AbstractNetBuilder(iTimeOut, urlString, mNet, respClz);
        boolean bWithNetCallback = false;
        if (args != null) {
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < args.length; ++i) {
                Annotation annotation = annotations[i][0];
                if (annotation.annotationType() == Param.class) {
                    final Param param = (Param) annotation;
                    if (args[i] == null) {
                        builder.addParam(param.value(), "");
                    } else {
                        builder.addParam(param.value(), args[i].toString());
                    }
                } else if (annotation.annotationType() == NetCallback.class) {  //接口回调
                    INetCallback netCallBack = (INetCallback) args[i];
                    if (netCallBack != null) {
                        builder.addNetCallback(netCallBack);
                        bWithNetCallback = true;
                    }
                }
            }
        }
        if (json != null) {
            return builder.postJsonResponse();
        } else if (keyValue != null) {
            return builder.postKeyValueResponse();
        } else {
            return get == null ? builder.post() : builder.rxGet();
        }

    }
}
