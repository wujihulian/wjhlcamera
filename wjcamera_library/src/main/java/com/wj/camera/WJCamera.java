package com.wj.camera;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.callback.StringCallbck;
import com.wj.camera.net.Api;
import com.wj.camera.net.SafeGuardInterceptor;
import com.wj.camera.response.AccessToken;
import com.wj.camera.uitl.SPUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

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

    public static WJCamera getInstance() {
        if (wjCamera == null) {
            wjCamera = new WJCamera();
        }
        return wjCamera;
    }

    private WJCamera() {
        ;
        creteaClient();
    }

    private void creteaClient() {
        mClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new SafeGuardInterceptor())
                .writeTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS).build();
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

    /**
     * 登录 适用sdk相关的 必须先登录
     * @param accessToken
     */
    public void login(String accessToken) {
        this.accessToken = accessToken;
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
