package com.magic_chen_.baseproject.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64DataException;
import android.util.Log;
import android.webkit.WebSettings;

import com.example.net.PullService;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.magic_chen_.baseproject.GlobalApplication;


import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by leed on 2016/12/15.
 */
public class OkhttpUtils {

    private OkHttpClient mHttpClient;
    private final Gson gson;
    private Handler mDelivery;
    private static OkhttpUtils mInstance;
    //	private final static boolean DEBUG = BuildConfig.DEBUG;
    private final static boolean DEBUG = true;
    private Call call;
    private String cookie = "";//跨站点操作时需要对自行对不同cookie进行保存，访问时设置cookie以延续会话

    private OkhttpUtils() {
        mHttpClient = new OkHttpClient.Builder()//重试
                .followRedirects(false)
                .followSslRedirects(false)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkhttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkhttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkhttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据自身需要配置OkhttpClient
     *
     * @param client
     */
    public void setOkHttpClient(OkHttpClient client) {
        mHttpClient = client;
    }

    public OkHttpClient getClient() {
        return mHttpClient;
    }

    private void deliveryResult(final Request request, final ResultCallback callback) {
        call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
//                    Headers headers = response.headers();
//                    List<String> values = headers.values("Set-Cookie");
//                    ToastUtil.i("okhttp重定向--->",values.toString());
//                    String cookie = response.header("cookie");
//                    ToastUtil.i("okhttp重定向--->",cookie);
//                    Logger.i("okhttp--->", "code--->" + response.code());
                    if (response.code() == 301 || response.code() == 302) {
                        cookie = response.header("set-cookie");
                       ToastUtil.i("okhttp重定向--->", "deliveryResult--->" + cookie);
                        if (callback != null) {
                            String location = response.header("Location");
                           ToastUtil.i("okhttp重定向--->", "location--->" + location);
//                            String cookie = response.header("cookie");
                            sendSuccessResultCallback(location, callback);
                        }
                    } else if (response.code() == 200) {
                        cookie = "";
                        String string = response.body().string();
                        byte[] decode = Base64.decode(string.getBytes(), Base64.DEFAULT);
                        byte[] abcs1 = Rc4.RC4Base(decode, "abc");
                        byte[] decompress = ZLibUtils.decompress(abcs1);
                        if (callback != null) {
                            Log.i("response okhttp:", string);
                            Log.i("response okhttp decode:", new String(decompress));
                            if (callback.mType == String.class) {
//                                Logger.i("okhttp--->",new String(decompress));
                                sendSuccessResultCallback(new String(decompress), callback);
                            } else {
                                Object o = gson.fromJson(new String(decompress), callback.mType);
                                sendSuccessResultCallback(o, callback);
                            }
                        }
                    } else {
                        NullPointerException e = new NullPointerException();
                        sendFailedStringCallback(response.request(), e, callback);
                    }
                } catch (Base64DataException e) {
                    if (response.body() != null) {
//                        try {
//                            InputStream in_withcode = new ByteArrayInputStream(response.body().string().getBytes("UTF-8"));
//                            String s1 = PullService.readXml(in_withcode);
//                            String regex = "'(.*)'";
//                            Pattern pattern = Pattern.compile(regex);
//                            Matcher matcher = pattern.matcher(s1);//匹配类
//                            while (matcher.find()) {
//                                sendSuccessResultCallback(matcher.group(1), callback);
//                            }
//                        } catch (UnsupportedEncodingException e1) {
//                            sendFailedStringCallback(response.request(), e1, callback);
//                        } catch (Exception e2) {
//                            sendFailedStringCallback(response.request(), e2, callback);
//                        }
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e) {
                    //Json解析的错误
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void getResult(final Request request, final ResultCallback callback) {
        call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code() == 301 || response.code() == 302) {
                        cookie = response.header("set-cookie");
                       ToastUtil.i("okhttp重定向--->", "getResult--->" + cookie);
                        if (callback != null) {
                            String location = response.header("Location");
                            ToastUtil.i("okhttp重定向--->", "location--->" + location);
                            sendSuccessResultCallback(location, callback);
                        }
                    } else if (response.code() == 200) {
                        cookie = "";
                        String string = response.body().string();
                        ToastUtil.i("response--->", string);
                        if (callback != null) {
                            if (callback.mType == String.class) {
                                sendSuccessResultCallback(string, callback);
                            } else {
                                Object o = gson.fromJson(string, callback.mType);
                                sendSuccessResultCallback(o, callback);
                            }
                        }
                    } else {
                        NullPointerException e = new NullPointerException();
                        sendFailedStringCallback(response.request(), e, callback);
                    }
                } catch (Base64DataException e) {
                    if (response.body() != null) {
//                        try {
//                            InputStream in_withcode = new ByteArrayInputStream(response.body().string().getBytes("UTF-8"));
//                            String s1 = PullService.readXml(in_withcode);
//                            String regex = "'(.*)'";
//                            Pattern pattern = Pattern.compile(regex);
//                            Matcher matcher = pattern.matcher(s1);//匹配类
//                            while (matcher.find()) {
//                                sendSuccessResultCallback(matcher.group(1), callback);
//                            }
//                        } catch (UnsupportedEncodingException e1) {
//                            sendFailedStringCallback(response.request(), e1, callback);
//                        } catch (Exception e2) {
//                            sendFailedStringCallback(response.request(), e2, callback);
//                        }
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e) {
                    //Json解析的错误
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
//                    if (e instanceof ConnectException) {
//                        ToastUtil.show(App.app,App.app.getResources().getString(R.string.check_network));
//                    } else if (e instanceof SocketTimeoutException) {
//                        ToastUtil.show(App.app,App.app.getResources().getString(R.string.time_out));
//                    } else if (e instanceof com.google.gson.JsonParseException) {
//                        ToastUtil.show(App.app,App.app.getResources().getString(R.string.data_parsing_error));
//                    }
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    if (object != null) {
//                        if (Constant.API_FLAG != 0) {
//                            if (Constant.API_FLAG <= Constant.ApiList.size())
//                                Collections.swap(Constant.ApiList, 0, Constant.API_FLAG);
//                            Constant.API_FLAG = 0;
//                        }
                        callback.onResponse
                                (object);
                    }
                }
            }
        });
    }

    /**
     * get请求
     *
     * @param url
     * @param callback
     */
    public void get(String url, ResultCallback callback) {
        if (OtherUtils.isZh()) {
            url = url + "?lang=zh_CN";
        } else {
            url = url + "?lang=en";
        }
        Request request;
        if (TextUtils.isEmpty(cookie)) {
            request = new Request.Builder()
                    .get().url(url).build();
        } else {
            request = new Request.Builder()
                    .header("Cookie", cookie)
                    .get().url(url).build();
        }
        getResult(request, callback);
    }

    /**
     * get请求
     *
     * @param url
     * @param callback
     */
    public void getBanner(String url, ResultCallback callback) {
        if (OtherUtils.isZh()) {
            url = url + "&lang=zh_CN";
        } else {
            url = url + "&lang=en";
        }
        Request request;
        if (TextUtils.isEmpty(cookie)) {
            request = new Request.Builder()
                    .get().url(url).build();
        } else {
            request = new Request.Builder()
                    .header("Cookie", cookie)
                    .get().url(url).build();
        }
        getResult(request, callback);
    }

    /**
     * get请求
     *
     * @param url
     * @param callback
     */
    public void firstGet(String url, ResultCallback callback) {
        Request request;
        if (TextUtils.isEmpty(cookie)) {
            request = new Request.Builder()
                    .get().url(url).build();
        } else {
            request = new Request.Builder()
                    .header("Cookie", cookie)
                    .get().url(url).build();
        }
        getResult(request, callback);
    }

    /**
     * 不带参数的post请求
     *
     * @param url
     * @param callback
     */
    public void post(String url, ResultCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (OtherUtils.isZh()) {
            builder.add("lang", "zh_CN");
        } else {
            builder.add("lang", "en");
        }
        RequestBody body = builder.build();
        Request request;
        if (TextUtils.isEmpty(cookie)) {
            request = new Request.Builder().post(body).url(url).build();
        } else {
            request = new Request.Builder().header("Cookie", cookie).post(body).url(url).build();
        }

        deliveryResult(request, callback);
    }

    /**
     * 将参数转换为json字符串的post请求
     *
     * @param url
     * @param o
     * @param callback
     */
    public void postJson(String url, @Nullable JSONObject o, ResultCallback callback) {
        if (DEBUG) {
            Log.e("requestUrl", url);
            assert o != null;
            Log.e("requestParams", o.toString());
        }
        assert o != null;
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), o.toString());
        Request request = new Request.Builder().post(body).url(url).build();

        getResult(request, callback);
    }

    /**
     * 传递byte数组进行请求
     */
    public void postByte(String url, JSONObject data, ResultCallback callback) {
//        Log.i("load--->",data);
        try {
            if (OtherUtils.isZh()) {
                data.put("lang", "zh_CN");
            } else {
                data.put("lang", "en");
            }
            byte[] compress = ZLibUtils.compress(data.toString().getBytes());
            byte[] abcs = Rc4.RC4Base(compress, "abc");
            byte[] encode = Base64.encode(abcs, Base64.DEFAULT);
           ToastUtil.i("main--->", new String(encode));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), encode);
            Request request;
            if (TextUtils.isEmpty(cookie)) {
                request = new Request.Builder().post(requestBody).url(url).build();
            } else {
                request = new Request.Builder().header("Cookie", cookie).post(requestBody).url(url).build();
            }

            deliveryResult(request, callback);
        } catch (Exception e) {
            ToastUtil.i("okhttppost异常--->", e.toString());
        }

    }

    /**
     * 传递byte数组进行请求
     */
    public void postByteAndHead(String url, String time, JSONObject data, ResultCallback callback) {
//        Log.i("load--->",data);
        try {
            if (OtherUtils.isZh()) {
                data.put("lang", "zh_CN");
            } else {
                data.put("lang", "en");
            }
            byte[] compress = ZLibUtils.compress(data.toString().getBytes());
            byte[] abcs = Rc4.RC4Base(compress, "abc");
            byte[] encode = Base64.encode(abcs, Base64.DEFAULT);
//        Log.i("load--->",new String(encode));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), encode);
            Request request;
            if (TextUtils.isEmpty(cookie)) {
                request = new Request.Builder().removeHeader("User-Agent")
                        .addHeader("User-Agent", getUserAgent(time)).post(requestBody).url(url).build();
            } else {
                request = new Request.Builder().header("Cookie", cookie).removeHeader("User-Agent")
                        .addHeader("User-Agent", getUserAgent(time)).post(requestBody).url(url).build();
            }
            deliveryResult(request, callback);
        } catch (Exception e) {
            ToastUtil.i("okhttppost异常--->", e.toString());
        }
    }

    private static String getUserAgent(String time) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(GlobalApplication.getGlobalContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0, length = userAgent.length(); i < length; i++) {
//            char c = userAgent.charAt(i);
//            if (c <= '\u001f' || c >= '\u007f') {
//                sb.append(String.format("\\u%04x", (int) c));
//            } else {
//                sb.append(c);
//            }
//        }
        userAgent += " (RequestTime/" + time + ")";
//        userAgent += " (RequestTime/" + "2018-11-19 13:18:00" + ")";
//        Log.i("okhttp-->",userAgent);
        return userAgent;
    }

    /**
     * 参数为键值对的post请求
     *
     * @param url
     * @param params
     * @param callback
     */
    public void postKeyValue(String url, @Nullable Map<String, String> params, @Nullable ResultCallback callback) {
        if (DEBUG) {
            Log.e("requestUrl", url);
            Log.e("requestParams", params.toString());
        }
        if (OtherUtils.isZh()) {
            params.put("lang", "zh_CN");
        } else {
            params.put("lang", "en");
        }

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        //LogUtils.d(url+"   "  + params.get(0));
        RequestBody body = builder.build();
        Request request;
        if (TextUtils.isEmpty(cookie)) {
            request = new Request.Builder().post(body).url(url).build();
        } else {
            request = new Request.Builder().header("Cookie", cookie).post(body).url(url).build();
        }
        getResult(request, callback);
    }


    /**
     * 上传多个文件，不带参数
     *
     * @param url
     * @param files
     * @param fileKey
     * @param callback
     */
    public void postFiles(String url, File[] files, String fileKey, ResultCallback callback) {
        postFiles(url, null, files, fileKey, callback);
    }

    /**
     * 上传多个文件，携带参数，文件键值相同
     *
     * @param url
     * @param params
     * @param files
     * @param fileKey
     * @param callback
     */
    public void postFiles(String url, Map<String, String> params, File[] files, String fileKey, ResultCallback callback) {
        String boundry = "--" + UUID.randomUUID().toString() + "--";
        MultipartBody.Builder builder = new MultipartBody.Builder(boundry).setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files[i]);
                builder.addFormDataPart(fileKey, files[i].getName(), fileBody);
            }
        }
        Request request = new Request.Builder().post(builder.build()).url(url).build();
        deliveryResult(request, callback);
    }

    /**
     * 上传多个文件，携带参数,文件键值不同
     *
     * @param url
     * @param params
     * @param files
     * @param fileKeys
     * @param callback
     */
    public void postMultiFiles(String url, Map<String, String> params, File[] files, String[] fileKeys, ResultCallback callback) {
        String boundry = "--" + UUID.randomUUID().toString() + "--";
        MultipartBody.Builder builder = new MultipartBody.Builder(boundry).setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files[i]);
                builder.addFormDataPart(fileKeys[i], files[i].getName(), fileBody);
            }
        }
        Request request = new Request.Builder().post(builder.build()).url(url).build();
        deliveryResult(request, callback);
    }

    /**
     * 请求回调
     *
     * @param <T>
     */
    public static abstract class ResultCallback<T> {
        public Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);

    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}
