package com.wj.camera;

import android.annotation.SuppressLint;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.callback.StringCallbck;
import com.wj.camera.net.Api;
import com.wj.camera.net.OkHttpUtils;
import com.wj.camera.request.TokenRequest;
import com.wj.camera.response.AccessToken;
import com.wj.camera.uitl.MD5Util;
import com.wj.camera.uitl.SPUtil;
import com.wj.camera.uitl.WJLogUitl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FileName: WJCamera
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJCamera {

    public static WJCamera wjCamera;
    private String accessToken;
    private OkHttpClient mClient;
    private Application mApplication;
    private String id;
    private String key;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public static WJCamera getInstance() {
        if (wjCamera == null) {
            wjCamera = new WJCamera();
        }
        return wjCamera;
    }

    private WJCamera() {
        creteaClient();
    }

    private void creteaClient() {
        mClient = OkHttpUtils.getInstance().getOkHttpClient();
    }

    public OkHttpClient getClient() {
        if (mClient == null) {
            creteaClient();
        }
        return mClient;
    }

    /**
     * 初始化
     * @param context
     * @param appKey
     */
    public void init(Application context, String appKey, String appSecret) {
        // sdk日志开关，正式发布需要去掉
        mApplication = context;
        EZOpenSDK.showSDKLog(false);
        EZOpenSDK.initLib(context, appKey);
        String str = (String) SPUtil.getData(mApplication, "AccessToken", "");
        if (!TextUtils.isEmpty(str)) {
            AccessToken accessToken = new Gson().fromJson(str, AccessToken.class);
            Long expireTime = accessToken.getData().getExpireTime();
            if (System.currentTimeMillis() >= expireTime - 86400000) {
                getToken(appKey, appSecret);
            } else {
                login(accessToken.getData().getAccessToken());
            }
        } else {
            getToken(appKey, appSecret);
        }
    }


    private void getToken(String appKey, String appSecret) {
        getToken(appKey, appSecret, new JsonCallback<AccessToken>() {
            @Override
            public void onSuccess(AccessToken data) {
                login(data.getData().getAccessToken());
                SPUtil.saveData(mApplication, "AccessToken", new Gson().toJson(data));
            }
        });
    }

    @SuppressLint("CheckResult")
    public void init(String appId, String appKey, Application context) {
        EZOpenSDK.showSDKLog(false);
        EZOpenSDK.initLib(context, "aab9716cd40740508e5ad6ecbe5d8a65");
        this.id = appId;
        this.key = appKey;
        mApplication = context;

        String str = (String) SPUtil.getData(mApplication, "WJAccessToken", "");
        if (!TextUtils.isEmpty(str)) {
            AccessToken accessToken = new Gson().fromJson(str, AccessToken.class);
            Long expireTime = accessToken.getData().getExpireTime();
            if (System.currentTimeMillis() >= expireTime - 86400000) {
            } else {
                //获取缓存token
                WJLogUitl.i(  "get cache " + accessToken.getCode());
                login(accessToken.getData().getAccessToken());
                return;
            }
        }
        WJLogUitl.i(  "init camera");
        OkHttpClient client = getClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.appId = appId;
        long currentTimeMillis = System.currentTimeMillis();
        tokenRequest.timestamp = currentTimeMillis + "";
        String sign = MD5Util.stringToMD5(appId + appKey + currentTimeMillis);
        tokenRequest.sign = sign;
        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(tokenRequest));
        Request request = new Request.Builder()
                .post(requestBody)
                .url("https://syswx.xx.cn/api/course/nLive/token/get")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e != null) {
                    WJLogUitl.i(  "onFailure: " + e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                try {
                    AccessToken accessToken = new Gson().fromJson(string, AccessToken.class);
                    if ("200".equals(accessToken.getCode())) {
                        SPUtil.saveData(mApplication, "WJAccessToken", new Gson().toJson(accessToken));
                        WJLogUitl.i(  "WJCamera init succeed");
                        login(accessToken.getData().getAccessToken());
                    } else {
                        WJLogUitl.i(  "WJCamera init failure " + accessToken.getCode());
                    }
                } catch (Exception e) {
                    WJLogUitl.i(  "WJCamera init failure " + e.getMessage());
                    e.printStackTrace();
                }


            }
        });


    }


    private static final String TAG = "WJCamera";

    /**
     * 登录 适用sdk相关的 必须先登录
     * @param accessToken
     */
    public void login(String accessToken) {
        this.accessToken = accessToken;
        OkHttpUtils.getInstance().commonHead("EZO-AccessToken", WJCamera.getInstance().getAccessToken());
        EZOpenSDK.getInstance().setAccessToken(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    /**
     *创建设备Playe
     * @param deviceSerial 设备序列号
     * @param cameraNo 相机通道
     */
    public EZPlayer createPlayer(String deviceSerial, int cameraNo) {
        EZPlayer player = EZOpenSDK.getInstance().createPlayer(deviceSerial, cameraNo);
        return player;
    }

    public EZPlayer createPlayer(String deviceSerial) {
        return createPlayer(deviceSerial, 1);
    }

    public void getToken(String appKey, String appSecret, JsonCallback<AccessToken> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("appKey", appKey);
        builder.addEncoded("appSecret", appSecret);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.Token)
                .post(formBody)
                .build();
        getClient().newCall(request).enqueue(new StringCallbck(callback));

    }

}
