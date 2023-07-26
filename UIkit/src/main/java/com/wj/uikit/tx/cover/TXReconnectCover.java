package com.wj.uikit.tx.cover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.OkHttpUtils;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.net.request.GetRequest;
import com.wj.camera.response.CameraDeviceLiveUrlResponse;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.player.event.WJReconnectEventConfig;
import com.wj.uikit.tx.bs.TXBaseCover;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * FileName: TXReconnectCover
 * Author: xiongxiang
 * Date: 2021/8/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXReconnectCover extends TXBaseCover {
    private int fps;
    private FragmentActivity mFragmentActivity;
    private Disposable mSubscribe;

    public TXReconnectCover(Context context) {
        super(context);
        if (context instanceof FragmentActivity){
            mFragmentActivity= (FragmentActivity) context;
        }
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return null;
    }


    @Override
    public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
        super.onError(player, code, msg, extraInfo);
        reconnection();
        WJLogUitl.d("onError "+(System.currentTimeMillis()/1000));
    }

    @Override
    public void onVideoLoading(V2TXLivePlayer player, Bundle extraInfo) {
        super.onVideoLoading(player, extraInfo);
        if (fps == 0) {
            reconnection();
            WJLogUitl.d("V2TXLivePlayStatusStopped "+(System.currentTimeMillis()/1000));
        }
    }

    /*
    @Override
    public void onVideoPlayStatusUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayStatus status, V2TXLiveDef.V2TXLiveStatusChangeReason reason, Bundle extraInfo) {
        super.onVideoPlayStatusUpdate(player, status, reason, extraInfo);
        switch (status) {
            case V2TXLivePlayStatusStopped:
                if (fps == 0) {
                    reconnection();
                    WJLogUitl.d("V2TXLivePlayStatusStopped "+(System.currentTimeMillis()/1000));
                }
                break;
        }
    }
*/


    @Override
    public void onStatisticsUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayerStatistics statistics) {
        super.onStatisticsUpdate(player, statistics);
        fps = statistics.fps;
    }
    boolean isReconnection =false;
    @SuppressLint("CheckResult")
    public void reconnection() {
        isReconnection=true;
        if (mSubscribe!=null){
            if (!mSubscribe.isDisposed()) {
                mSubscribe.dispose();
                mSubscribe=null;
            }
        }
        WJLogUitl.d("reconnection");
        mSubscribe = Observable.timer(2, TimeUnit.SECONDS).map(new Function<Long, RtmpConfig>() {
            @Override
            public RtmpConfig apply(@NonNull Long aLong) throws Exception {
                RtmpConfig rtmp = ISAPI.getInstance().getRTMP(mDevIndex);
                if (rtmp == null) {
                    return new RtmpConfig();
                }
                return rtmp;
            }
        }).filter(new Predicate<RtmpConfig>() {
            @Override
            public boolean test(@NonNull RtmpConfig rtmpConfig) throws Exception {
                RtmpConfig.RTMPDTO rtmp = rtmpConfig.getRTMP();
                if (rtmp == null) {
                    return true;
                }

                //设备未开启  开启预览功能
                if ("false".equals(rtmp.getEnabled())) {
                    rtmpConfig.getRTMP().setEnabled("true");
                    rtmpConfig.getRTMP().setPrivatelyEnabled("true");
                    //确定预览地址
                    String playURL2 = rtmp.getPlayURL2();
                    String privatelyURL = rtmp.getPrivatelyURL();
                    if (TextUtils.isEmpty(privatelyURL) || TextUtils.isEmpty(playURL2)) {
                        //预览地址为空 或者 预览推流地址为空  需要给设备配置 推流地址 啦流地址
                        WJLogUitl.i("apply: 预览地址为空 开始配置预览地址");
                        configPrivatelyURL(rtmpConfig);
                    } else if (checkPreviewUrl(playURL2)) {
                        WJLogUitl.i("apply: 预览地址过期 开始配置预览地址");
                        configPrivatelyURL(rtmpConfig);
                    } else {
                        ISAPI.getInstance().setRtmp(getDeviceSerial(), rtmpConfig);
                    }
                }

                return true;
            }
        }).map(new Function<RtmpConfig, RtmpConfig>() {
            @Override
            public RtmpConfig apply(@NonNull RtmpConfig rtmpConfig) throws Exception {
                if (TextUtils.isEmpty(getHost())) {
                    return rtmpConfig;
                }
                if (rtmpConfig.getRTMP() == null) {
                    return rtmpConfig;
                }
                if (liveReconnectCount>=2){
                   return triggerLiveCheck();
                }

                //确定预览地址
                RtmpConfig.RTMPDTO rtmp = rtmpConfig.getRTMP();
                String playURL2 = rtmp.getPlayURL2();
                String privatelyURL = rtmp.getPrivatelyURL();
                String url = rtmp.getURL();
                String playURL1 = rtmp.getPlayURL1();
                String privatelyEnabled = rtmp.getPrivatelyEnabled();

                if ("true".equals(privatelyEnabled)) {

                    if (TextUtils.isEmpty(privatelyURL) || TextUtils.isEmpty(playURL2)) {
                        //预览地址为空 或者 预览推流地址为空  需要给设备配置 推流地址 啦流地址
                        WJLogUitl.i("apply: 预览地址为空 开始配置预览地址");
                        configPrivatelyURL(rtmpConfig);
                    } else if (checkPreviewUrl(playURL2)) {
                        WJLogUitl.i("apply: 预览地址过期 开始配置预览地址");
                        configPrivatelyURL(rtmpConfig);
                    }

                } else {
                    if (liveReconnectCount >= 2 || TextUtils.isEmpty(playURL1)) {
                        //直播流重连3次取不到
                        return  triggerLiveCheck();
                    }
                }
                return rtmpConfig;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        isReconnection = false;
                        liveReconnectCount++;
                        WJLogUitl.d("doOnError");
                        reconnection();
                    }
                })
                .doOnSubscribe(new RxConsumer(mFragmentActivity))
                .subscribe(new Consumer<RtmpConfig>() {
                    @Override
                    public void accept(RtmpConfig rtmpConfig) throws Exception {
                        liveReconnectCount++;
                        String url = null;
                        if (rtmpConfig != null && rtmpConfig.getRTMP() != null) {
                            String privatelyEnabled = rtmpConfig.getRTMP().getPrivatelyEnabled();
                            if ("true".equals(privatelyEnabled)) {
                                url = rtmpConfig.getRTMP().getPlayURL2();
                            } else {
                                url = rtmpConfig.getRTMP().getPlayURL1();
                            }
                        }
                        if (TextUtils.isEmpty(url)) {

                            reconnection();
                            return;
                        }
                        url = WJReconnectEventConfig.transformUrl(url);
                        WJLogUitl.i(url);
                        startPlay(url);
                        isReconnection = false;

                    }
                });
    }

    //触发流检测
    private RtmpConfig triggerLiveCheck() {
        WJLogUitl.d("triggerLiveCheck");
        GetRequest getRequest = OkHttpUtils.getInstance().get("/api/course/cameraDevicePreviewState?isCheck=1&deviceCode=" + getDeviceSerial());
        getRequest.setBaseUrl(getHost());
        getRequest.addHeader("token", getToken()).execute();
        liveReconnectCount = 0;

        RtmpConfig rtmp = ISAPI.getInstance().getRTMP(mDevIndex);
        if (rtmp == null) {
            return new RtmpConfig();
        }
        return rtmp;
    }


    //检查推流是否过期
    public boolean checkPreviewUrl(String previewUrl) {
        if (TextUtils.isEmpty(previewUrl)) {
            return true;
        }
        String[] split = previewUrl.split("\\?");
        if (split.length >= 1) {
            for (String s : split) {
                String[] split1 = s.split("&");
                for (String s1 : split1) {
                    WJLogUitl.i(s1);
                    String[] split2 = s1.split("=");
                    String s2 = split2[0];
                    if ("txTime".equals(s2)) {
                        String hex = split2[1];
                        WJLogUitl.i(hex);
                        long time = Long.valueOf(hex, 16);
                        long currentTimeMillis = System.currentTimeMillis() / 1000;
                        WJLogUitl.i("checkPreviewUrl: 剩余时间 " + (time - currentTimeMillis));
                        if (time <= currentTimeMillis) {
                            WJLogUitl.i("拉流地址过期");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String token;
    private String host;
    private String mDevIndex;
    private String mDeviceSerial;
    private int liveReconnectCount = 0;

    public String getHost() {
        if (TextUtils.isEmpty(host)) {
            return WJReconnectEventConfig.host;
        }
        return host;
    }

    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            return WJReconnectEventConfig.token;
        }
        return token;
    }

    public String getDeviceSerial() {
        return mDeviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        mDeviceSerial = deviceSerial;
    }

    public String getDevIndex() {
        return mDevIndex;
    }

    public void setDevIndex(String devIndex) {
        mDevIndex = devIndex;
    }

    public void configPrivatelyURL(RtmpConfig rtmpConfig) {
        GetRequest getRequest = OkHttpUtils.getInstance().get("/api/course/getCameraDeviceLiveUrl?deviceCode=" + getDeviceSerial());
        getRequest.setBaseUrl(getHost());
        Response response = getRequest.addHeader("token", getToken()).execute();
        if (response == null || response.body() == null) {
            return;
        }
        String string = null;
        try {
            string = response.body().string();
            CameraDeviceLiveUrlResponse deviceLiveUrlResponse = new Gson().fromJson(string, CameraDeviceLiveUrlResponse.class);
            if (deviceLiveUrlResponse == null || deviceLiveUrlResponse.getData() == null) {
                return;
            }
            CameraDeviceLiveUrlResponse.CameraDeviceLiveUrlData data = deviceLiveUrlResponse.getData();
            rtmpConfig.getRTMP().setPrivatelyURL(data.getDocpub());
            rtmpConfig.getRTMP().setPlayURL2(data.getDocplay());

            //必传参数 我也没办法 随便设置一个咯
            if (TextUtils.isEmpty(rtmpConfig.getRTMP().getURL())) {
                rtmpConfig.getRTMP().setURL(data.getDocpub());
                rtmpConfig.getRTMP().setPlayURL1(data.getDocplay());
            }
            ISAPI.getInstance().setRtmp(getDeviceSerial(), rtmpConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
