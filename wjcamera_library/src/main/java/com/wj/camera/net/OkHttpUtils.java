package com.wj.camera.net;

import com.wj.camera.net.map.RepeatKeyHasMap;
import com.wj.camera.net.request.GetRequest;
import com.wj.camera.net.request.PostFromRequest;
import com.wj.camera.net.request.PostJsonRequest;
import com.wj.camera.net.request.PutRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * FileName: OkHttpUtils
 * Author: xiongxiang
 * Date: 2021/6/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class OkHttpUtils {
    private static OkHttpUtils instance;
    private String baseUrl;
    private OkHttpClient mOkHttpClient;
    private HashMap<String, String> commonHeads = new HashMap<>();
    private static List<Interceptor> mInterceptors;
    private RepeatKeyHasMap<String, Call> mTagHasMap = new RepeatKeyHasMap<>();
    public static boolean httpLogging = false;//接口請求日志

    public static void addInterceptor(Interceptor interceptor) {
        if (mInterceptors == null) {
            mInterceptors = new ArrayList<>();
        }
        mInterceptors.add(interceptor);
    }

    private OkHttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (mInterceptors != null) {
            for (Interceptor mInterceptor : mInterceptors) {
                builder.addInterceptor(mInterceptor);
            }
        }
        if (httpLogging = true) {
            HttpLoggingInterceptor mInterceptor = new HttpLoggingInterceptor();
            mInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(mInterceptor);
        }

        mOkHttpClient = builder
                .addInterceptor(new SafeGuardInterceptor())
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();
        setBaseUrl(Api.baseUrl);
    }

    public void commonHead(String key, String value) {
        commonHeads.put(key, value);
    }

    public HashMap<String, String> getCommonHeads() {
        return commonHeads;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    public GetRequest get(String url) {
        return new GetRequest(url);
    }

    public PutRequest put(String url) {
        return new PutRequest(url);
    }

    public PostJsonRequest PostJson(String url) {
        return new PostJsonRequest(url);
    }


    public PostFromRequest postFrom(String url) {
        return new PostFromRequest(url);
    }

    public void cancel(String tag) {
        List<Call> calls = mTagHasMap.get(tag);
        if (calls != null) {
            for (Call call : calls) {
                call.cancel();
            }
            calls.clear();
            mTagHasMap.remove(tag);
        }
    }

    public RepeatKeyHasMap<String, Call> getTagHasMap() {
        return mTagHasMap;
    }
}
