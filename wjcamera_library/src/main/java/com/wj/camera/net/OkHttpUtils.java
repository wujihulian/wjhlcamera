package com.wj.camera.net;

import com.wj.camera.net.request.GetRequest;
import com.wj.camera.net.request.PutRequest;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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


    private OkHttpUtils() {
        mOkHttpClient = (new OkHttpClient()).newBuilder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
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

    public PutRequest put(String url, String json) {
        return new PutRequest(url).jsons(json);
    }

}
