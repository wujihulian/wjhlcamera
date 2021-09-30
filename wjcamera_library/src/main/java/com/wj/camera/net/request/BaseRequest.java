package com.wj.camera.net.request;

import android.text.TextUtils;

import com.wj.camera.net.OkHttpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FileName: BaseRequest
 * Author: xiongxiang
 * Date: 2021/6/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class BaseRequest<R extends BaseRequest> {
    private HashMap<String, String> mHeaders = new HashMap<>();
    private HashMap<String, String> params = new HashMap<>();
    private String baseUrl;
    private String url;
    private String json;
    private String tag;
    private Call mCall;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BaseRequest(String url) {
        this.url = url;
        baseUrl = OkHttpUtils.getInstance().getBaseUrl();
    }

    public String getUrl() {
        return url;
    }


    public R setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return (R) this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getFullUrl() {
        return getBaseUrl() + getUrl();
    }

    public HashMap<String, String> getParams() {
        return params;
    }


    public R addHeader(String key, String value) {
        mHeaders.put(key, value);
        return (R) this;
    }

    public R removeHeader(String key) {
        mHeaders.remove(key);
        return (R) this;
    }

    public R setParams(HashMap<String, String> params) {
        this.params = params;
        return (R) this;
    }

    public R params(String key, String value) {
        params.put(key, value);
        return (R) this;
    }

    public R jsons(String json) {
        this.json = json;
        return (R) this;
    }

    public String getJson() {
        return json;
    }

    public HashMap<String, String> getCommonHeads() {
        return OkHttpUtils.getInstance().getCommonHeads();
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    abstract RequestBody createRequestBody();

    abstract Request.Builder buildRequest();


    public Call getCall() {
        return mCall;
    }

    public String getTag() {
        if (TextUtils.isEmpty(tag)) {
            return "BaseRequest";
        }
        return tag;
        //   return tag;
    }

    public Call enqueue(Callback callback) {
        Request.Builder builder = buildRequest();
        for (Map.Entry<String, String> headers : getHeaders().entrySet()) {
            builder.header(headers.getKey(), headers.getValue());
        }
        HashMap<String, String> commonHeads = OkHttpUtils.getInstance().getCommonHeads();
        if (commonHeads != null) {
            for (Map.Entry<String, String> headers : commonHeads.entrySet()) {
                builder.header(headers.getKey(), headers.getValue());
            }
        }
        Call call = OkHttpUtils.getInstance().getOkHttpClient().newCall(builder.build());
        mCall = call;

        if (!TextUtils.isEmpty(getTag())) {
            OkHttpUtils.getInstance().getTagHasMap().put(getTag(), getCall());
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }
        });
        return call;
    }


    public void cancel(String tag) {
        OkHttpUtils.getInstance().cancel(tag);
    }

    public Response execute() {
        Request.Builder builder = buildRequest();
        for (Map.Entry<String, String> headers : getHeaders().entrySet()) {
            builder.header(headers.getKey(), headers.getValue());
        }
        HashMap<String, String> commonHeads = OkHttpUtils.getInstance().getCommonHeads();
        for (Map.Entry<String, String> headers : commonHeads.entrySet()) {
            builder.header(headers.getKey(), headers.getValue());
        }
        Call call = OkHttpUtils.getInstance().getOkHttpClient().newCall(builder.build());
        try {
            Response execute = call.execute();
            return execute;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
