package com.wj.uikit.player.event;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.kk.taurus.playerbase.assist.AssistPlay;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.OkHttpUtils;
import com.wj.camera.net.request.GetRequest;
import com.wj.camera.response.CameraDeviceLiveUrlResponse;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.entity.EventBusUpdateRtmpConfig;

import org.greenrobot.eventbus.EventBus;

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
 * FileName: WJReconnectEventListener
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description: 重连
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJReconnectEvent extends WJBaseReconnectEvent {
    private AssistPlay mAssistPlay;
    private String mDeviceSerial;
    private String mDevIndex;
    private int liveReconnectCount = 0;
    private Disposable mSubscribe;

    public String getDevIndex() {
        return mDevIndex;
    }

    public void setDevIndex(String devIndex) {
        mDevIndex = devIndex;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAssistPlay(AssistPlay assistPlay) {
        mAssistPlay = assistPlay;
    }

    public void setDeviceSerial(String deviceSerial) {
        mDeviceSerial = deviceSerial;
    }


    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            return WJReconnectEventConfig.token;
        }
        return token;
    }

    public String getHost() {
        if (TextUtils.isEmpty(host)) {
            return WJReconnectEventConfig.host;
        }
        return host;
    }

    public String getDeviceSerial() {
        return mDeviceSerial;
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        WJLogUitl.d("onErrorEvent" + eventCode);
        reconnection();

    }

    private int updateCount = 0;
    private int currentPosition = 0;

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        if (PLAYER_EVENT_ON_VIDEO_RENDER_START == eventCode) {
            liveReconnectCount = 0;
            //mAssistPlay.play(true);
        }

        if (PLAYER_EVENT_ON_TIMER_UPDATE == eventCode) {
            WJLogUitl.d("getCurrentPosition" + currentPosition);
            if (currentPosition == mAssistPlay.getCurrentPosition()) {
                updateCount++;
                if (updateCount >= 4) {
                    updateCount = 0;
                    mAssistPlay.play(true);
                }
            } else {
                currentPosition = mAssistPlay.getCurrentPosition();
                updateCount = 0;
            }
        }
        if (eventCode == OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE) {
            //播放完成尝试重连业务 有可能地址被更换 有可能没人推流
            reconnection();
        }
    }

    @SuppressLint("CheckResult")
    private void reconnection() {
        WJLogUitl.d("reconnection");
        if (mSubscribe != null) {
            if (!mSubscribe.isDisposed()) {
                mSubscribe.dispose();
                mSubscribe = null;
            }
        }

        mSubscribe = Observable.timer(2, TimeUnit.SECONDS).map(new Function<Long, RtmpConfig>() {
                    @Override
                    public RtmpConfig apply(@NonNull Long aLong) throws Exception {
                        RtmpConfig rtmp = ISAPI.getInstance().getRTMP_byVersion(TextUtils.isEmpty(mDevIndex), TextUtils.isEmpty(mDevIndex) ? mDeviceSerial : mDevIndex, null);
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
                        //直播状态 并且没有直播预览地址
           /*     if ("false".equals(rtmp.getPrivatelyEnabled()) && TextUtils.isEmpty(rtmp.getPlayURL1())) {
                    return false;
                }*/
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
                                triggerLiveCheck();
                            }
                        }
                        return rtmpConfig;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RtmpConfig>() {
                    @Override
                    public void accept(RtmpConfig rtmpConfig) throws Exception {
                        if (rtmpConfig.getRTMP() == null) {
                            mAssistPlay.play(true);
                            return;
                        }

                        String privatelyEnabled = rtmpConfig.getRTMP().getPrivatelyEnabled();
                        String url;
                        if ("true".equals(privatelyEnabled)) {
                            url = rtmpConfig.getRTMP().getPlayURL2();
                        } else {
                            url = rtmpConfig.getRTMP().getPlayURL1();
                            liveReconnectCount++;
                        }
                   /*     if (!TextUtils.isEmpty(url)) {
                            url = url.replace("https:", "webrtc:").replace(".flv", "");
                        }*/
                        DataSource dataSource = new DataSource();
                        dataSource.setData(url);

                        WJLogUitl.i(url);
                        if (mAssistPlay.isPlaying()) {
                            mAssistPlay.stop();
                        }
                        mAssistPlay.setDataSource(dataSource);
                        mAssistPlay.play(true);
                    }
                });
    }

    private static final String TAG = "WJReconnectEvent";

    //触发流检测
    private void triggerLiveCheck() {
        GetRequest getRequest = OkHttpUtils.getInstance().get("/api/course/cameraDevicePreviewState?deviceCode=" + getDeviceSerial());
        getRequest.setBaseUrl(getHost());
        getRequest.addHeader("token", getToken()).execute();
        liveReconnectCount = 0;
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
//            ISAPI.getInstance().setRtmp(getDeviceSerial(), rtmpConfig);
//            ISAPI.getInstance().setRtmp(TextUtils.isEmpty(mDevIndex), TextUtils.isEmpty(mDevIndex) ? mDeviceSerial : mDevIndex, rtmpConfig);
            OkHttpUtils.getInstance().setOldVersion(false);
            ResponseStatus responseStatus = ISAPI.getInstance().setRtmp(getDeviceSerial(), rtmpConfig);
            if ("1".equals(responseStatus.ResponseStatus.statusCode)) {
                DataSource dataSource = new DataSource();
                dataSource.setData(data.getDocplay());

                WJLogUitl.i(data.getDocplay() );
                if (mAssistPlay.isPlaying()) {
                    mAssistPlay.stop();
                }
                mAssistPlay.setDataSource(dataSource);
                mAssistPlay.play(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
