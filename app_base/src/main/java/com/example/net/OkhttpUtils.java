package com.example.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Base64DataException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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

    private OkhttpUtils() {
        mHttpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
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
                    Log.i("okhttp--->","code--->"+response.code());
                    if (response.code()==301 || response.code()==302){
                        if (callback != null) {
                            String location = response.header("Location");
                            sendSuccessResultCallback(location, callback);
                        }
                    } else if (response.code()==200){
                        String string = response.body().string();
                        byte[] decode = Base64.decode(string.getBytes(), Base64.DEFAULT);
                        byte[] abcs1 = Rc4.RC4Base(decode, "abc");
                        byte[] decompress = ZLibUtils.decompress(abcs1);
                        if (callback != null) {
                            Log.i("response okhttp:" , string);
                            Log.i("response okhttp decode:" , new String(decompress));
                            if (callback.mType == String.class) {
//                                Logger.i("okhttp--->",new String(decompress));
                                sendSuccessResultCallback(new String(decompress), callback);
                            } else {
                                Object o = gson.fromJson(new String(decompress), callback.mType);
                                sendSuccessResultCallback(o, callback);
                            }
                        }
                    }else {
                        NullPointerException e = new NullPointerException();
                        sendFailedStringCallback(response.request(),e, callback);
                    }
                } catch (Base64DataException e){
                    if (response.body()!=null){
                        try {
                            InputStream in_withcode   =   new ByteArrayInputStream(response.body().string().getBytes("UTF-8"));
                            String s1 = PullService.readXml(in_withcode);
                            String regex = "'(.*)'";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(s1);//匹配类
                            while (matcher.find()) {
                                sendSuccessResultCallback(matcher.group(1), callback);
                            }
                        } catch (UnsupportedEncodingException e1) {
                            sendFailedStringCallback(response.request(), e1, callback);
                        } catch (Exception e2) {
                            sendFailedStringCallback(response.request(), e2, callback);
                        }
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
                    if (response.code()==301 || response.code()==302){
                        if (callback != null) {
                            String location = response.header("Location");
                            sendSuccessResultCallback(location, callback);
                        }
                    } else if (response.code()==200){
                        String string = response.body().string();
                        Log.i("response:" , string);
                        if (callback != null) {
                            if (callback.mType == String.class) {
                                sendSuccessResultCallback(string, callback);
                            } else {
                                Object o = gson.fromJson(string, callback.mType);
                                sendSuccessResultCallback(o, callback);
                            }
                        }
                    }else {
                        NullPointerException e = new NullPointerException();
                        sendFailedStringCallback(response.request(),e, callback);
                    }
                }catch (Base64DataException e){
                    if (response.body()!=null){
                        try {
                            InputStream in_withcode   =   new ByteArrayInputStream(response.body().string().getBytes("UTF-8"));
                            String s1 = PullService.readXml(in_withcode);
                            String regex = "'(.*)'";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(s1);//匹配类
                            while (matcher.find()) {
                                sendSuccessResultCallback(matcher.group(1), callback);
                            }
                        } catch (UnsupportedEncodingException e1) {
                            sendFailedStringCallback(response.request(), e1, callback);
                        } catch (Exception e2) {
                            sendFailedStringCallback(response.request(), e2, callback);
                        }
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
        Request request = new Request.Builder().get().url(url).build();
        getResult(request, callback);
    }

    /**
     * 不带参数的post请求
     *
     * @param url
     * @param callback
     */
    public void post(String url, ResultCallback callback) {
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().post(body).url(url).build();
        deliveryResult(request, callback);
    }

    /**
     * 将参数转换为json字符串的post请求
     *
     * @param url
     * @param o
     * @param callback
     */
    public void postJson(String url, @Nullable Object o, ResultCallback callback) {
        if (DEBUG) {
            Log.e("requestUrl", url);
            Log.e("requestParams", gson.toJson(o));
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(o));
        Request request = new Request.Builder().post(body).url(url).build();
        getResult(request, callback);
    }

    /**
     *传递byte数组进行请求
     */
    public void postByte(String url,String data,ResultCallback callback) {
        Log.i("load--->",data);
        byte[] compress = ZLibUtils.compress(data.getBytes());
        byte[] abcs = Rc4.RC4Base(compress, "abc");
        byte[] encode = Base64.encode(abcs, Base64.DEFAULT);
        Log.i("load--->",new String(encode));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), encode);
        Request request = new Request.Builder().post(requestBody).url(url).build();
        deliveryResult(request, callback);
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
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        //LogUtils.d(url+"   "  + params.get(0));
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).build();
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
