package com.example.net;

import android.text.TextUtils;
import android.util.Log;

import com.example.net.guava.Joiner;
import com.example.net.guava.Preconditions;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.example.net.Globals.GSON;


/**
 * Created by magic_chen_ on 2018/9/5.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public class AbstractNetBuilder<R> {
    private final String TAG = "AbstractNetBuilder";
    private static final MediaType CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded");

    private static final MediaType CONTENT_TYPE_FILE
            = MediaType.parse("multipart/form-data");

    public static final MediaType CONTENT_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");


    protected final HashMap<String, String> mParams = new HashMap<String, String>();
    protected final List<String> mFileList = new ArrayList<>();
    final Class<R> mRespClz;
    final OkHttpClient mHttpClient;
    final INet mNet;
    private String mUrl;
    private INetCallback<R> mNetCallBack;

    AbstractNetBuilder(int timeOut, String url, INet net, Class<R> respClz) {
        mNet = net;
        mUrl = url;
        mRespClz = respClz;
        mHttpClient = new OkHttpClient.Builder().readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS).build();

    }

    public AbstractNetBuilder<R> addParam(String key, Object object) {
        Preconditions.checkArgument(!TextUtils.isEmpty(key));

        if (object == null) {
            return this;
        }
        mParams.put(key, object.toString());
        return this;
    }

    public AbstractNetBuilder<R> addListParam(String[] values) {
        if (values == null) {
            return this;
        }
        for (int i = 0; i < values.length; ++i) {
            mFileList.add(values[i]);
        }
        return this;
    }

    public AbstractNetBuilder<R> addNetCallback(INetCallback netCallBack) {
        if (netCallBack == null) {
            return this;
        }
        mNetCallBack = netCallBack;
        return this;
    }


    /**
     * Get 方式请求网络
     */
    public Void get() {
        initParam();
        String fullUrl = mUrl;
        if (mParams.size() > 0) {
            fullUrl = mUrl + "?" + Joiner.on("&").withKeyValueSeparator("=").useForNull("").join(mParams);
        }
        Log.d(TAG, "get url =" + fullUrl);
        OkhttpUtils.getInstance().get(fullUrl, new OkhttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
//                Log.i("网络请求异常--->", );
                sendFailCallback(e);

            }

            @Override
            public void onResponse(String response) {
                try {
                    final R result;

                    if (mRespClz == String.class) {
                        result = (R) response;
                    } else {
                        result = GSON.fromJson(response, mRespClz);
                    }
                    sendSuccessCallback(result);
                } catch (Exception e) {
                    sendFailCallback(e);
                }
            }
        });
        return null;
    }

    /**
     * Post 方式请求网络
     */
    public Void post() {
        initParam();
        mHttpClient.newCall(new Request.Builder().url(mUrl)
                .post(RequestBody.create(CONTENT_TYPE, Joiner.on("&").withKeyValueSeparator("=").useForNull("").join(mParams))).build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sendFailCallback(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if (!response.isSuccessful()) {
                                sendFailCallback(new IOException("request failed , reponse's code is : " + response.code()));
                                return;
                            }

                            final String respStr = response.body().string();
                            Log.d(TAG, "post result =" + respStr);
                            final R result;
                            if (mRespClz == String.class) {
                                result = (R) respStr;
                            } else {
                                result = GSON.fromJson(respStr, mRespClz);
                            }
                            sendSuccessCallback(result);
                        } catch (Exception e) {
                            sendFailCallback(e);
                        }
                    }
                });
        return null;
    }


    /**
     * Post  json格式上传数据
     */
    public Void postJsonResponse() {

        mUrl = mNet.onCreateUrl(mUrl);
        JSONObject jsonObject = new JSONObject();
        try {
            if (mParams.size() > 0) {
                Iterator iter = mParams.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    jsonObject.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        OkhttpUtils.getInstance().postByte(mUrl, s, new OkhttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                sendFailCallback(e);
            }

            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, "post json result =" + response);
                    R result;
                    if (mRespClz == String.class) {
                        result = (R) response;
                    } else {
                        result = GSON.fromJson(response, mRespClz);
                    }
                    sendSuccessCallback(result);
                } catch (Exception e) {
                    sendFailCallback(e);
                }
            }
        });
        return null;
    }

    public Observable<R> rxGet() {
        return Observable.create(new Observable.OnSubscribe<R>() {
            @Override
            public void call(Subscriber<? super R> subscriber) {
                initParam();
                String fullUrl = mUrl + "?" + Joiner.on("&").withKeyValueSeparator("=").useForNull("").join(mParams);
                Log.d(TAG, "rxGet url =" + fullUrl);
                try {
                    //新增http get请求缓存
                    Response response = mHttpClient.newCall(new Request.Builder()
                            .cacheControl(new CacheControl.Builder().maxStale(20, TimeUnit.SECONDS).build()).url(fullUrl)
                            .get().build()).execute();

                    final String respStr = response.body().string();
//                    Log.d(TAG, "rxGet result =" + respStr);
                    R data;
                    Log.d(TAG, mRespClz.toString());
                    if (mRespClz == String.class) {
                        data = (R) respStr;
                    } else {
                        data = GSON.fromJson(respStr, mRespClz);
                    }

                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.d(TAG, "rxGet result  exception =" + e.getMessage());
                    R data;
                    if (mRespClz == String.class) {
                        String msg = "{\"msg\":\"" + e.getMessage() + "\",\"status\":0}";
                        data = (R) msg;
                    } else {
                        data = createInvalidResponse(IResponse.ERROR_RESPONSE, e.getMessage());
                    }
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    R createInvalidResponse(int code, String msg) {
        try {
            R result = mRespClz.newInstance();
            ((IResponse) result).setMsg(msg);
            ((IResponse) result).setErrorCode(code);

            return result;
        } catch (Exception e) {
            Log.d(TAG, "createInvalidResponse  Exception  =  " + e.getMessage());
            throw new Error(e);
        }
    }

    /**
     * Post  以KeyValue形式请求数据
     */
    public Void postKeyValueResponse() {
        mUrl = mNet.onCreateUrl(mUrl);
        OkhttpUtils.getInstance().postKeyValue(mUrl, mParams, new OkhttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(TAG, "   error:" + e.toString());

            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "    response:" + response);
                try {
                    Log.d(TAG, "post json result =" + response);
                    R result;
                    if (mRespClz == String.class) {
                        result = (R) response;
                    } else {
                        result = GSON.fromJson(response, mRespClz);
                    }
                    sendSuccessCallback(result);
                } catch (Exception e) {
                    sendFailCallback(e);
                }
            }
        });
        return null;
    }

    private void initParam() {
        mNet.onCreateParams(mUrl, mParams);
        mUrl = mNet.onCreateUrl(mUrl);
    }


    private void sendFailCallback(final Exception e) {
        if (mNetCallBack == null)
            return;
        Globals.UI_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (mNetCallBack != null) {
                    mNetCallBack.onFail(e);
                }
            }
        });
    }

    private void sendSuccessCallback(final R response) {
        if (mNetCallBack == null)
            return;
        Globals.UI_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (mNetCallBack != null) {
                    mNetCallBack.onSuccess(response);
                }
            }
        });
    }

}
