package com.wj.uikit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.tencent.live2.V2TXLiveCode;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.videogo.openapi.EZPlayer;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.config.WJDeviceSceneEnum;
import com.wj.camera.config.WJRateTypeEnum;
import com.wj.camera.net.Api;
import com.wj.camera.net.ApiNew;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.OkHttpUtils;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.CheckDevcieUpdate;
import com.wj.camera.response.DeviceInfoListResponse;
import com.wj.camera.response.DeviceUpdateStatus;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.response.SceneResponse;
import com.wj.camera.response.TwoWayAudio;
import com.wj.camera.response.VideoConfig;
import com.wj.camera.response.ZoomResponse;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.camera.view.WJPlayView;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.entity.EventBusUpdateRtmpConfig;
import com.wj.uikit.player.WJRelationAssistUtil;
import com.wj.uikit.player.WJVideoPlayer;
import com.wj.uikit.player.event.WJReconnectEvent;
import com.wj.uikit.player.event.WJReconnectEventConfig;
import com.wj.uikit.player.interfac.OnVolumeChangeListener;
import com.wj.uikit.pop.FocusSelectPop;
import com.wj.uikit.pop.SelectPop;
import com.wj.uikit.subscribe.LoadingObserver;
import com.wj.uikit.tx.TXVideoPlayer;
import com.wj.uikit.tx.cover.TXControlCover;
import com.wj.uikit.uitl.OnControlClickListener;
import com.wj.uikit.view.TouchProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.Response;

/**
 * FileName: DeviceDebugActivity
 * Author: xiongxiang
 * Date: 2021/1/22
 * Description: 调试设备
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceDebugNewActivity extends BaseUikitActivity {
    private String deviceSerial;
    private String deviceCode;
    private String deviceType;
    private FrameLayout mRatio_fl;
    private FrameLayout mBitrate_fl;
    private TextView mRatio_tv;
    private TextView mBitrate_tv;
    private WJPlayView mWJPlayView;

    private int audio = 1;
    private int video = 1;
    private static final String TAG = "DeviceDebugActivity";
    private SelectPop mRatioSelectPop;
    private ISAPI mIsapi;
    private VideoConfig mVideoConfig;
    private ImageView mBack_iv;
    private SelectPop mBitrateSelectPop;
    private SelectPop mBitrateTypeSelectPop;
    private ImageView mFull_iv1;
    private EZPlayer mPlayer;
    private DeviceInfo mDeviceInfo;
    private View mFocus_fl;
    private TextView mFocus_tv;
    private FocusSelectPop mFocusSelectPop;
    private TwoWayAudio mTwoWayAudio;
    private TouchProgressView mTp_volume;
    private TextView mTv_volume;
    private String[] xy = {"1280*720", "1920*1080", "2560*1440"};
    private String[] bitrate = {"2048", "3072", "4096", "6144", "8192"};
    private String[] bitrateTitle = {"2M", "3M", "4M", "6M", "8M"};
    private FrameLayout mDevice_update_fl;
    private Disposable mDisposable;
    private CountDownTimer mCountDownTimer;
    private FrameLayout mScene_fl;
    private TextView mScene_tv;
    private LinearLayout mLl_high;
    private ImageView mIv_high;
    private FrameLayout mBitrate_type_fl;
    private TextView mBitrate_type_tv;
    private FrameLayout mFrameLayout;
    private ImageView mIv_volume;
    private V2TXLivePlayerImpl mLivePlayer;
    private TXVideoPlayer mTxVideoPlayer;
    private String mDevIndex = "";
    AudioManager audioManager = null;
    private boolean isOldVersion = false; //是否是旧版

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_device_debug_new);
        EventBus.getDefault().register(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getDeviceInfo();
        checkDeviceStatus();//判断设备是否需要更新
//        isLatestVersion();
        findView();
        initClick();
//        getData();
//        initAudio();
    }

    /**
     * 判断设备是否需要更新
     */
    @SuppressLint("CheckResult")
    private void checkDeviceStatus() {
        Observable.fromCallable(new Callable<DeviceInfoListResponse>() {
                    @Override
                    public DeviceInfoListResponse call() throws Exception {
                        DeviceInfoListResponse deviceInfoListResponse = DeviceApi.getInstance().getDeviceList(mDeviceInfo.device_serial);
                        return deviceInfoListResponse;
                    }
                })
                .map(new Function<DeviceInfoListResponse, DeviceInfoListResponse>() {
                    @Override
                    public DeviceInfoListResponse apply(DeviceInfoListResponse deviceUpdateStatus) throws Exception {
                        BaseDeviceResponse<CheckDevcieUpdate> deviceResponse = new BaseDeviceResponse<>();
                        if (0 == deviceUpdateStatus.getSearchResult().getNumOfMatches()) {
                            deviceResponse = DeviceApi.getInstance().checkDeviceUpdate(mDeviceInfo.device_serial);
                            isOldVersion = true;
                            if (null != deviceResponse.getData() && deviceResponse.getData().getIsUpgrading() == 1) {
                                //设备正在升级
                                toUpdateProgress();
                            } else {
                                OkHttpUtils.getInstance().setOldVersion(isOldVersion);
                                if (deviceResponse.getData().getIsNeedUpgrade() != 0) {

                                    // 有新版本
                                    new XPopup.Builder(WJDeviceDebugNewActivity.this).setPopupCallback(new SimpleCallback() {
                                                @Override
                                                public boolean onBackPressed(BasePopupView popupView) {
                                                    finish();
                                                    return true;
                                                }
                                            })
                                            .dismissOnTouchOutside(false).asConfirm("该设备需要更新", "是否将设备更新到新版本", "否", "是", new OnConfirmListener() {
                                                @Override
                                                public void onConfirm() {
                                                    deviceUpdate();
                                                }
                                            }, null, true, 0).show();
                                }
                                return deviceUpdateStatus;
                            }
                        }

                        return deviceUpdateStatus;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(this))
                .subscribe(new Consumer<DeviceInfoListResponse>() {
                    @Override
                    public void accept(DeviceInfoListResponse deviceUpdateStatus) throws Exception {
                        if (!deviceUpdateStatus.getSearchResult().getMatchList().isEmpty()) {
                            isOldVersion = false;
                            OkHttpUtils.getInstance().setOldVersion(isOldVersion);
                            mDevIndex = deviceUpdateStatus.getSearchResult().getMatchList().get(0).getDevice().getDevIndex();
                            initVideoPlay();

                        }
                    }
                });
//        DeviceApi.getInstance().getDeviceList(mDeviceInfo.device_serial, new Callback() {
//            @Override
//            public void onFailure(@androidx.annotation.NonNull Call call, @androidx.annotation.NonNull IOException e) {
//                System.out.println("onFailure--> " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@androidx.annotation.NonNull Call call, @androidx.annotation.NonNull Response response) throws IOException {
//                String json = response.body().string();
//                System.out.println("onResponse--> " + json);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DeviceInfoListResponse infoListResponse = new Gson().fromJson(json, DeviceInfoListResponse.class);
//                        if (0 == infoListResponse.getSearchResult().getNumOfMatches()) {
//                            isOldVersion = true;
//                            OkHttpUtils.getInstance().setOldVersion(isOldVersion);
//                            // 有新版本
//                            new XPopup.Builder(WJDeviceDebugNewActivity.this).setPopupCallback(new SimpleCallback() {
//                                        @Override
//                                        public boolean onBackPressed(BasePopupView popupView) {
//                                            finish();
//                                            return true;
//                                        }
//                                    })
//                                    .dismissOnTouchOutside(false).asConfirm("该设备需要更新", "是否将设备更新到新版本", "否", "是", new OnConfirmListener() {
//                                        @Override
//                                        public void onConfirm() {
//                                            deviceUpdate();
//                                        }
//                                    }, null, true, 0).show();
//                            return;
//                        }
//                        if (!infoListResponse.getSearchResult().getMatchList().isEmpty()) {
//                            isOldVersion = false;
//                            OkHttpUtils.getInstance().setOldVersion(isOldVersion);
//                            mDevIndex = infoListResponse.getSearchResult().getMatchList().get(0).getDevice().getDevIndex();
//                            initVideoPlay();
//
//                        }
//                    }
//                });
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != audioManager) {
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    volume = Math.max(0, volume - 1);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
                    break;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    volume = Math.min(100, volume + 1);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
                    break;
                case KeyEvent.KEYCODE_BACK:
                    finish();
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initVideoPlay() {
        if (WJReconnectEventConfig.isWebRtc) {
            getData();
            initWebrtc();
            initAudio();
        } else {
            initAction();
        }
    }
    private void initWebrtc() {
        if (null == mDeviceInfo) {
            return;
        }
        FrameLayout frameLayout = findViewById(R.id.fl);
        frameLayout.setVisibility(View.VISIBLE);
        mTxVideoPlayer = new TXVideoPlayer(this);
        mTxVideoPlayer.getReconnectCover().setDeviceSerial(mDeviceInfo.device_serial);
        mTxVideoPlayer.getReconnectCover().setDevIndex(mDevIndex);
        mTxVideoPlayer.attachContainer(frameLayout);
        Observable.just(mDeviceInfo)
                .map(new Function<DeviceInfo, RtmpConfig>() {
                    @Override
                    public RtmpConfig apply(DeviceInfo deviceInfo) throws Exception {
//                        BaseDeviceResponse<CheckDevcieUpdate> deviceResponse = DeviceApi.getInstance().checkDeviceUpdate(deviceInfo.device_serial);
//                        boolean oldVersion = false;//是不是旧版的
//                        try {
//                            String currentVersion = deviceResponse.getData().getCurrentVersion();
//                            String version = currentVersion.substring(currentVersion.lastIndexOf(" ") + 1);
//                            oldVersion = (Integer.parseInt(version.substring(0, 2)) <= 22);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                        RtmpConfig rtmp = null;
                        if (isOldVersion) {
                            rtmp = ISAPI.getInstance().getRTMP_byVersion(isOldVersion, deviceInfo.device_serial, null);
                        } else {
                            DeviceInfoListResponse infoListResponse = DeviceApi.getInstance().getDeviceList(deviceInfo.device_serial);

                            if (0 != infoListResponse.getSearchResult().getNumOfMatches()) {
                                if (!infoListResponse.getSearchResult().getMatchList().isEmpty()) {
                                    String mDevIndex = infoListResponse.getSearchResult().getMatchList().get(0).getDevice().getDevIndex();
                                    deviceInfo.setDevIndex(mDevIndex);
                                    rtmp = ISAPI.getInstance().getRTMP_byVersion(false, mDevIndex, null);
                                }
                            }

                        }
                        System.out.println("DeviceInfoListResponse-- " + rtmp);
                        return rtmp;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(this))
                .subscribe(new LoadingObserver<RtmpConfig>(this) {
                    @Override
                    public void onNext(RtmpConfig data) {
                        super.onNext(data);
                        if (data != null) {
                            RtmpConfig.RTMPDTO rtmp = data.getRTMP();
                            if (rtmp != null) {
                                String url;
                                if ("true".equals(rtmp.getPrivatelyEnabled())) {
                                    url = rtmp.getPlayURL2();
                                } else {
                                    url = rtmp.getPlayURL1();
                                }

//                        url = WJReconnectEventConfig.transformUrl(url);
                                WJLogUitl.d(url);
                                if (TextUtils.isEmpty(url)||checkPreviewUrl(url)) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ResponseStatus responseStatus = ISAPI.getInstance().configPrivatelyURL(mDevIndex, deviceSerial, data);
                                            if (responseStatus.ResponseStatus != null && "1".equals(responseStatus.ResponseStatus.statusCode)) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        initWebrtc();
                                                    }
                                                });
                                            }

                                        }
                                    }).start();
//                                    Toast.makeText(WJDeviceDebugNewActivity.this, "设备不在线，请重新添加", Toast.LENGTH_SHORT).show();
//                                    finish();
                                    return;
                                }
                                if (mTxVideoPlayer != null) {
                                    int startCode = mTxVideoPlayer.startPlay(url);
                                    WJLogUitl.d("startPlay-- " + startCode + "  " + (System.currentTimeMillis() / 1000));
                                    if (startCode == V2TXLiveCode.V2TXLIVE_OK) {

                                    } else {
                                        mTxVideoPlayer.getReconnectCover().reconnection();
                                    }
                                }
                            }
                        } else {
                            System.out.println("获取不到数据~~");
                        }
                    }
                });


        TXControlCover txControlCover = (TXControlCover) mTxVideoPlayer.getReceiver("TXControlCover");
        txControlCover.getWj_full_iv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WJTXDeviceFullActivity.start(WJDeviceDebugNewActivity.this, mDeviceInfo);
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDevice(DeviceInfo deviceInfo) {
        //initAction();
        getData();
        initAudio();
    }

    private void initAudio() {
        mIsapi.getAudio(new JsonCallback<TwoWayAudio>() {
            @Override
            public void onSuccess(TwoWayAudio data) {
                mTwoWayAudio = data;
                if (mTwoWayAudio != null && mTwoWayAudio.getTwoWayAudioChannel() != null) {
                    mTv_volume.setText(mTwoWayAudio.getTwoWayAudioChannel().getSpeakerVolume() + "");
                    mTp_volume.setProgress(Integer.valueOf(mTwoWayAudio.getTwoWayAudioChannel().getSpeakerVolume()));

                    volumeChange();

                }
            }
        });

    }

    private void volumeChange() {
        if (mTp_volume.getProgress() == 0) {
            mIv_volume.setImageResource(R.mipmap.wj_volume_off);
        } else {
            mIv_volume.setImageResource(R.mipmap.wj_volume_on);
        }
    }


    @SuppressLint("CheckResult")
    private void getData() {
        mIsapi = ISAPI.getInstance();
        mIsapi.config(deviceSerial);
        mIsapi.setDevIndex(mDevIndex);
        mIsapi.getVideoConfig(new JsonCallback<VideoConfig>() {
            @Override
            public void onSuccess(VideoConfig data) {
                mVideoConfig = data;
                if (mVideoConfig != null && mVideoConfig.getStreamingChannel() != null && mVideoConfig.getStreamingChannel().getVideo() != null) {
                    VideoConfig.StreamingChannelDTO.VideoDTO video = mVideoConfig.getStreamingChannel().getVideo();
                    String videoResolutionWidth = video.getVideoResolutionWidth();
                    String videoResolutionHeight = video.getVideoResolutionHeight();
                    mRatio_tv.setText(String.format("%s*%s", videoResolutionWidth, videoResolutionHeight));
                    String videoQualityControlType = mVideoConfig.getStreamingChannel().getVideo().getVideoQualityControlType();
                    String rate = "";
                    if (WJRateTypeEnum.VBR.getRate().equalsIgnoreCase(videoQualityControlType)) {
                        rate = mVideoConfig.getStreamingChannel().getVideo().getVbrUpperCap();
                        mBitrate_type_tv.setText(WJRateTypeEnum.VBR.getTitle());
                    } else if (WJRateTypeEnum.CBR.getRate().equalsIgnoreCase(videoQualityControlType)) {
                        rate = mVideoConfig.getStreamingChannel().getVideo().getConstantBitRate();
                        mBitrate_type_tv.setText(WJRateTypeEnum.CBR.getTitle());
                    }
                    WJLogUitl.i("onSuccess: " + rate);
                    if (!TextUtils.isEmpty(rate)) {
                        Integer value = Integer.valueOf(rate);
                        int m = value / 1024;
                        mBitrate_tv.setText(String.format("%sM", m));
                    }

                }
            }
        });

        mIsapi.getZoom(new JsonCallback<ZoomResponse>() {
            @Override
            public void onSuccess(ZoomResponse data) {
                if (data != null && data.getZoom() != null) {
                    zoom = data.getZoom().getRatio();
                    zoomIndex = data.getZoom().getRatio() - 1;
                    mFocus_tv.setText(zoom + "x");
                }
            }
        });

        mIsapi.getScene(mDeviceInfo.device_serial, new JsonCallback<SceneResponse>() {
            @Override
            public void onSuccess(SceneResponse data) {
                if (data != null && data.getMountingScenario() != null) {
                    String mode = data.getMountingScenario().getMode();
                    String sceneTitle = WJDeviceSceneEnum.getSceneTitle(mode);
                    mScene_tv.setText(sceneTitle);
                }
            }
        });
    }

    private WJVideoPlayer mWjVideoPlayer;

    private void initAction() {
        mFrameLayout.setVisibility(View.VISIBLE);
        if (mWjVideoPlayer == null) {

            mWjVideoPlayer = new WJVideoPlayer(this);
            mWjVideoPlayer.init();
            WJReconnectEvent mWjReconnectEvent = new WJReconnectEvent();
            mWjReconnectEvent.setDeviceSerial(deviceSerial);

            mWjVideoPlayer.getGestureCover().setDeviceSerial(deviceSerial);
            mWjVideoPlayer.getGestureCover().setGesture(true);
            mWjVideoPlayer.setData("");
            mWjVideoPlayer.registerReconnect(mWjReconnectEvent);
            mWjVideoPlayer.attachContainer(mFrameLayout);

            mWjVideoPlayer.getWjControlCover().getWj_full_iv().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WJRelationAssistUtil.getInstance().switchFull(WJDeviceDebugNewActivity.this, mWjVideoPlayer, mFrameLayout);
                }
            });
            mWjVideoPlayer.getGestureCover().setOnVolumeChangeListener(new OnVolumeChangeListener() {
                @Override
                public void volumeChange(int volume) {
                    //mTp_volume.setProgress(volume);
       /*             float v=volume/100f;

                    Log.i(TAG, "volumeChange: "+v);
                    mWjVideoPlayer.setVolume(v,v);
                    if (volume>0){
                        mWjVideoPlayer.getWjControlCover().setAudio(false);
                    }else {
                        mWjVideoPlayer.getWjControlCover().setAudio(true);
                    }*/


                }
            });
        }
        TextView wj_device_update_tv = findViewById(R.id.wj_device_update_tv);

        //  mWjVideoPlayer.play();
        ISAPI.getInstance().getRTMP(mDeviceInfo.device_serial, new JsonCallback<RtmpConfig>() {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                wj_device_update_tv.setText("(离线)");

            }

            @Override
            public void onSuccess(RtmpConfig data) {
                if (data != null && data.getRTMP() != null) {
                    String url = "";
                    if ("false".equals(data.getRTMP().getPrivatelyEnabled())) {
                        url = data.getRTMP().getPlayURL1();
                    } else {
                        url = data.getRTMP().getPlayURL2();
                    }
               /*     if (!TextUtils.isEmpty(url)) {
                        url = url.replace("https:", "webrtc:").replace(".flv", "");
                    }*/
                    if (mWjVideoPlayer != null) {
                        mWjVideoPlayer.setData(url + "");
                        mWjVideoPlayer.play();
                    }
                    wj_device_update_tv.setText("(在线)");
                } else {
                    wj_device_update_tv.setText("(离线)");
                }

            }
        });
    }

    /**
     * 获取新版设备信息
     */
    private void getDevice() {
        DeviceApi.getInstance().getDeviceList(mDeviceInfo.device_serial, new Callback() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Call call, @androidx.annotation.NonNull IOException e) {
                System.out.println("onFailure--> " + e.getMessage());
            }

            @Override
            public void onResponse(@androidx.annotation.NonNull Call call, @androidx.annotation.NonNull Response response) throws IOException {
                String json = response.body().string();
                System.out.println("onResponse--> " + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DeviceInfoListResponse infoListResponse = new Gson().fromJson(json, DeviceInfoListResponse.class);
                        if (0 == infoListResponse.getSearchResult().getNumOfMatches()) {
                            Toast.makeText(WJDeviceDebugNewActivity.this, "设备不在线，请检查设备后重试", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!infoListResponse.getSearchResult().getMatchList().isEmpty()) {

                            mDevIndex = infoListResponse.getSearchResult().getMatchList().get(0).getDevice().getDevIndex();
                            initVideoPlay();

                        }
                    }
                });
            }
        });
    }

    int zoom = 1;
    int zoomIndex = 0;

    private void initClick() {
        mBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFocus_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFocusSelectPop == null) {
                    mFocusSelectPop = new FocusSelectPop(WJDeviceDebugNewActivity.this);
                    mFocusSelectPop.setAtIndex(zoomIndex);
                    mFocusSelectPop.setDeviceSerial(mDeviceInfo.device_serial);

                    mFocusSelectPop.setListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int position) {
                            zoomIndex = position;
                            mFocus_tv.setText(s);
                            ISAPI.getInstance().zoom(position + 1);


                        }
                    });
                    new XPopup.Builder(WJDeviceDebugNewActivity.this).hasShadowBg(false).asCustom(mFocusSelectPop);
                }
                mFocusSelectPop.show();
            }
        });


        mRatio_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRatioSelectPop == null) {
                    mRatioSelectPop = new SelectPop(WJDeviceDebugNewActivity.this, xy);
                    new XPopup.Builder(WJDeviceDebugNewActivity.this).asCustom(mRatioSelectPop);
                    mRatioSelectPop.setListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int position) {

                            if (mVideoConfig != null && mVideoConfig.getStreamingChannel() != null && mVideoConfig.getStreamingChannel().getVideo() != null) {
                                String[] split = s.split("\\*");
                                mVideoConfig.getStreamingChannel().getVideo().setVideoResolutionHeight(split[1]);
                                mVideoConfig.getStreamingChannel().getVideo().setVideoResolutionWidth(split[0]);
                                mIsapi.setVideoConfig(mVideoConfig, new JsonCallback<ResponseStatus>() {
                                    @Override
                                    public void onSuccess(ResponseStatus data) {
                                        if (data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                            mRatio_tv.setText(s);
                                        } else {
                                            Toast.makeText(WJDeviceDebugNewActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                mRatioSelectPop.show();
            }
        });

        mBitrate_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBitrateSelectPop == null) {
                    mBitrateSelectPop = new SelectPop(WJDeviceDebugNewActivity.this, bitrateTitle);
                    new XPopup.Builder(WJDeviceDebugNewActivity.this).asCustom(mBitrateSelectPop);
                    mBitrateSelectPop.setListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int position) {
                            if (mVideoConfig != null) {
                                mVideoConfig.getStreamingChannel().getVideo().setVbrUpperCap(bitrate[position]);
                                mVideoConfig.getStreamingChannel().getVideo().setConstantBitRate(bitrate[position]);
                                mIsapi.setVideoConfig(mVideoConfig, new JsonCallback<ResponseStatus>() {
                                    @Override
                                    public void onSuccess(ResponseStatus data) {
                                        if (data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                            mBitrate_tv.setText(bitrateTitle[position]);
                                        } else {
                                            Toast.makeText(WJDeviceDebugNewActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                mBitrateSelectPop.show();
            }
        });
        mBitrate_type_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBitrateTypeSelectPop == null) {

                    mBitrateTypeSelectPop = new SelectPop(WJDeviceDebugNewActivity.this, WJRateTypeEnum.VBR.getTitle(), WJRateTypeEnum.CBR.getTitle());
                    new XPopup.Builder(WJDeviceDebugNewActivity.this).asCustom(mBitrateTypeSelectPop);
                    mBitrateTypeSelectPop.setListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int position) {
                            if (mVideoConfig != null) {
                                if (WJRateTypeEnum.CBR.getTitle().equals(s)) {
                                    //定码率
                                    mVideoConfig.getStreamingChannel().getVideo().setVideoQualityControlType(WJRateTypeEnum.CBR.getRate());
                                } else if (WJRateTypeEnum.VBR.getTitle().equals(s)) {
                                    //变码率
                                    mVideoConfig.getStreamingChannel().getVideo().setVideoQualityControlType(WJRateTypeEnum.VBR.getRate());
                                }
                                ISAPI.getInstance().setVideoConfig(mVideoConfig, new JsonCallback<ResponseStatus>() {
                                    @Override
                                    public void onSuccess(ResponseStatus data) {
                                        if (data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                            mBitrate_type_tv.setText(s);
                                        } else {
                                            Toast.makeText(WJDeviceDebugNewActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                mBitrateTypeSelectPop.show();

            }
        });


        mTp_volume.setOnProgressChangedListener(new TouchProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(View view, int progress) {
                mTv_volume.setText(progress + "");
                volumeChange();
            }

            @Override
            public void onUpProgressChanged(View view, int progress) {
                mIsapi.getAudio(new JsonCallback<TwoWayAudio>() {
                    @Override
                    public void onSuccess(TwoWayAudio data) {
                        mTwoWayAudio = data;
                        if (mTwoWayAudio != null && mTwoWayAudio.getTwoWayAudioChannel() != null) {
                            if (!String.valueOf(progress).equals(mTwoWayAudio.getTwoWayAudioChannel().getSpeakerVolume())) {
                                mTwoWayAudio.getTwoWayAudioChannel().setSpeakerVolume(progress + "");
                                mIsapi.setAuido(mTwoWayAudio, new JsonCallback<ResponseStatus>() {
                                    @Override
                                    public void onSuccess(ResponseStatus data) {
                                        volumeChange();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });


        mDevice_update_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkDevice();
            }
        });


        mScene_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPop selectPop = new SelectPop(WJDeviceDebugNewActivity.this, WJDeviceSceneEnum.toArrayString());
                new XPopup.Builder(WJDeviceDebugNewActivity.this).asCustom(selectPop).show();
                selectPop.setListener(new OnItemClickListener<String>() {
                    @Override
                    public void onClick(String s, int position) {
                        WJDeviceSceneEnum[] values = WJDeviceSceneEnum.values();
                        ISAPI.getInstance().setScene(mDeviceInfo.device_serial, values[position], new JsonCallback<ResponseStatus>() {
                            @Override
                            public void onSuccess(ResponseStatus data) {
                                if (data != null && data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                    mScene_tv.setText(s);
                                } else {
                                    Toast.makeText(WJDeviceDebugNewActivity.this, "切换场景失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }
        });

        if (mIv_high.getRotation() <= 0) {

            mLl_high.setVisibility(View.GONE);
        } else {
            mLl_high.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.fl_high).setOnClickListener(new OnControlClickListener(200) {
            @Override
            public void onViewClick(View view) {
                if (mIv_high.getRotation() <= 0) {
                    mIv_high.setRotation(90);
                    mLl_high.setVisibility(View.VISIBLE);
                } else {
                    mIv_high.setRotation(0);
                    mLl_high.setVisibility(View.GONE);
                }
            }
        });


        findViewById(R.id.tv_left).setOnClickListener(new OnControlClickListener() {
            @Override
            public void onViewClick(View view) {
                // ISAPI.getInstance().focus(mDeviceInfo.device_serial,false);
                ISAPI.getInstance().FOCUSCTRL(mDeviceInfo.device_serial, "far");
            }
        });
        findViewById(R.id.tv_right).setOnClickListener(new OnControlClickListener() {
            @Override
            public void onViewClick(View v) {
                //ISAPI.getInstance().focus(mDeviceInfo.device_serial,true);
                ISAPI.getInstance().FOCUSCTRL(mDeviceInfo.device_serial, "near");
            }
        });

        findViewById(R.id.tv_pzt_left).setOnClickListener(new OnControlClickListener() {
            @Override
            public void onViewClick(View v) {
                // ISAPI.getInstance().pztData(mDeviceInfo.device_serial,false);

                ISAPI.getInstance().ZOOMCRTL(mDeviceInfo.device_serial, "tele");
            }
        });
        findViewById(R.id.tv_pzt_right).setOnClickListener(new OnControlClickListener() {
            @Override
            public void onViewClick(View v) {
                //ISAPI.getInstance().pztData(mDeviceInfo.device_serial,true);

                ISAPI.getInstance().ZOOMCRTL(mDeviceInfo.device_serial, "wide");
            }
        });

        findViewById(R.id.fl_recover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(WJDeviceDebugNewActivity.this).asConfirm("重新配网", "你确定设备重新配网？", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ISAPI.getInstance().wirelessServer(OkHttpUtils.getInstance().isOldVersion(), mDeviceInfo.device_serial);
                        Intent intent = new Intent(WJDeviceDebugNewActivity.this, WJSettingModeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(WJDeviceConfig.DEVICE_INFO, mDeviceInfo);
//                        bundle.putInt(WJDeviceConfig.DEVICE_CODE, 120020);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).show();
            }
        });

        findViewById(R.id.fl_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(WJDeviceDebugNewActivity.this).asConfirm("设备重置", "重置完成，请重新扫码配网？", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ISAPI.getInstance().factoryResetFull(deviceSerial);
                    }
                }).show();
            }
        });


        findViewById(R.id.tv_auto_focus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一键聚焦
                ISAPI.getInstance().onepushFoucsStart(mDeviceInfo.device_serial);
            }
        });


        mIv_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTp_volume.getProgress() == 0) {
                    ISAPI.getInstance().setVolume(mDeviceInfo.device_serial, 50, new JsonCallback<ResponseStatus>() {
                        @Override
                        public void onSuccess(ResponseStatus data) {
                            if (data != null && data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                mTp_volume.setProgress(50);
                            } else {
                                Toast.makeText(WJDeviceDebugNewActivity.this, "音量设置失败", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(int code, String msg) {
                            super.onError(code, msg);
                            Toast.makeText(WJDeviceDebugNewActivity.this, "音量设置失败", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    ISAPI.getInstance().setVolume(mDeviceInfo.device_serial, 0, new JsonCallback<ResponseStatus>() {
                        @Override
                        public void onError(int code, String msg) {
                            super.onError(code, msg);
                            Toast.makeText(WJDeviceDebugNewActivity.this, "音量设置失败", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(ResponseStatus data) {
                            if (data != null && data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                mTp_volume.setProgress(0);
                            } else {
                                Toast.makeText(WJDeviceDebugNewActivity.this, "音量设置失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    //检查是不是最新版
    @SuppressLint("CheckResult")
    private void isLatestVersion() {
        Observable.fromCallable(new Callable<BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public BaseDeviceResponse<CheckDevcieUpdate> call() throws Exception {
                        BaseDeviceResponse<CheckDevcieUpdate> deviceResponse = DeviceApi.getInstance().checkDeviceUpdate(mDeviceInfo.device_serial);
                        BaseDeviceResponse<DeviceUpdateStatus> deviceUpdateStatus = DeviceApi.getInstance().deviceUpdateStatus(mDeviceInfo.device_serial);
                        if (deviceResponse.getData() != null && deviceUpdateStatus != null) {
                            deviceResponse.getData().mDeviceResponse = deviceUpdateStatus.getData();
                        }
                        return deviceResponse;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(WJDeviceDebugNewActivity.this))
                .subscribe(new Consumer<BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public void accept(BaseDeviceResponse<CheckDevcieUpdate> response) throws Exception {
                        if (response.getCode() == 200 && response.getData() != null) {
                            CheckDevcieUpdate responseData = response.getData();
//                            responseData.setIsNeedUpgrade(1);
                            try {
                                String currentVersion = responseData.getCurrentVersion();
                                String version = currentVersion.substring(currentVersion.lastIndexOf(" ") + 1);
                                OkHttpUtils.getInstance().setOldVersion(Integer.parseInt(version.substring(0, 2)) <= 22);
                                if (Integer.parseInt(version.substring(0, 2)) <= 22) {
                                    checkDevice();
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            OkHttpUtils.getInstance(WJDeviceDebugNewActivity.this).setBaseUrl(OkHttpUtils.getInstance().isOldVersion() ? ApiNew.baseUrl : Api.baseUrl);

                            if (OkHttpUtils.getInstance().isOldVersion()) {
                                initVideoPlay();
                            } else {
                                getDevice();
                            }
                            if (responseData.getIsUpgrading() == 1) {
                                //设备正在升级
                                //toUpdateProgress();
                                return;
                            }

                            if (response.getData().mDeviceResponse != null) {
                                int progress = response.getData().mDeviceResponse.getProgress();
                                int status = response.getData().mDeviceResponse.getStatus();
                                if (status == 0) {
                                    //toUpdateProgress();
                                    return;
                                } else if (status == 2) {
                                    if (progress == 100) {
                                        //toUpdateProgress();
                                        return;
                                    }
                                }
                            }
                            String latestVersion = response.getData().getLatestVersion();
                            String currentVersion = response.getData().getCurrentVersion();


                            if (responseData.getIsNeedUpgrade() >= 1 && !(currentVersion.equals(latestVersion))) {
                                // 有新版本
                                new XPopup.Builder(WJDeviceDebugNewActivity.this).asConfirm("检测到新版本", responseData.getCurrentVersion() + " 是否升级到 " + responseData.getLatestVersion(), "否", "是", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        deviceUpdate();
                                    }
                                }, null, false, 0).show();
                            }


                        }
                    }
                });
    }


    @SuppressLint("CheckResult")
    private void checkDevice() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
                mDisposable = null;
            }
        }

        mDisposable = Observable.just(mDeviceInfo).map(new Function<DeviceInfo, BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public BaseDeviceResponse<CheckDevcieUpdate> apply(@NonNull DeviceInfo deviceInfo) throws Exception {
                        BaseDeviceResponse<CheckDevcieUpdate> deviceResponse = DeviceApi.getInstance().checkDeviceUpdate(mDeviceInfo.device_serial);
                        BaseDeviceResponse<DeviceUpdateStatus> deviceUpdateStatus = DeviceApi.getInstance().deviceUpdateStatus(mDeviceInfo.device_serial);
                        if (deviceResponse.getData() != null && deviceUpdateStatus != null) {
                            deviceResponse.getData().mDeviceResponse = deviceUpdateStatus.getData();
                        }
                        return deviceResponse;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(WJDeviceDebugNewActivity.this))
                .subscribe(new Consumer<BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public void accept(BaseDeviceResponse<CheckDevcieUpdate> response) throws Exception {


                        if (response.getCode() == 200 && response.getData() != null) {
                            CheckDevcieUpdate responseData = response.getData();
//                            boolean oldVersion = false;//是不是旧版的
//                            try {
//                                String currentVersion = responseData.getCurrentVersion();
//                                String version = currentVersion.substring(currentVersion.lastIndexOf(" ") + 1);
//                                oldVersion = (Integer.parseInt(version.substring(0, 2)) <= 22);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            if (isOldVersion) {
                                // 有新版本
                                new XPopup.Builder(WJDeviceDebugNewActivity.this).dismissOnBackPressed(false)
                                        .dismissOnTouchOutside(false).asConfirm("检测到新版本", responseData.getCurrentVersion() + " 是否升级到 " + responseData.getLatestVersion(), "否", "是", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                deviceUpdate();
                                            }
                                        }, null, true, 0).show();
                                return;
                            }
                            if (responseData.getIsUpgrading() == 1) {
                                //设备正在升级
                                toUpdateProgress();
                                return;
                            }

                            if (response.getData().mDeviceResponse != null) {
                                int progress = response.getData().mDeviceResponse.getProgress();
                                int status = response.getData().mDeviceResponse.getStatus();
                                if (status == 0) {
                                    toUpdateProgress();
                                    return;
                                } else if (status == 2) {
                                    if (progress == 100) {
                                        toUpdateProgress();
                                        return;
                                    }
                                }
                            }

                            String latestVersion = response.getData().getLatestVersion();
                            String currentVersion = response.getData().getCurrentVersion();
                            if (responseData.getIsNeedUpgrade() >= 1 && !(currentVersion.equals(latestVersion))) {
                                // 有新版本
                                new XPopup.Builder(WJDeviceDebugNewActivity.this).asConfirm("检测到新版本", responseData.getCurrentVersion() + " 是否升级到 " + responseData.getLatestVersion(), "否", "是", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        deviceUpdate();
                                    }
                                }, null, false, 0).show();
                            } else {

                                // 最新版
                                new XPopup.Builder(WJDeviceDebugNewActivity.this).asConfirm("已经是最新版", responseData.getCurrentVersion(), null, "确定", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        //toUpdateProgress();
                                    }
                                }, null, true, 0).show();
                            }


                        }

                    }
                });
    }

    private void toUpdateProgress() {
        Intent intent = new Intent(WJDeviceDebugNewActivity.this, WJUpdateProgressActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(WJDeviceConfig.DEVICE_INFO, mDeviceInfo);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    @SuppressLint("CheckResult")
    public void deviceUpdate() {
        Observable.just(mDeviceInfo).map(new Function<DeviceInfo, BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public BaseDeviceResponse apply(@NonNull DeviceInfo deviceInfo) throws Exception {
                        BaseDeviceResponse deviceResponse = DeviceApi.getInstance().deviceUpdate(deviceInfo.device_serial);
                        if (deviceResponse.getCode() != 200) {
                            finish();
                            return deviceResponse;
                        }
                        return deviceResponse;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(WJDeviceDebugNewActivity.this))
                .subscribe(new Consumer<BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public void accept(BaseDeviceResponse<CheckDevcieUpdate> checkDevcieUpdateBaseDeviceResponse) throws Exception {
                        if (checkDevcieUpdateBaseDeviceResponse.getCode() == 200) {
                            toUpdateProgress();
                        } else {
                            Toast.makeText(WJDeviceDebugNewActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void findView() {
        mBack_iv = findViewById(R.id.back_iv);
        mWJPlayView = findViewById(R.id.wj_playview);
        mScene_fl = findViewById(R.id.scene_fl);
        mScene_tv = findViewById(R.id.scene_tv);
        mRatio_fl = findViewById(R.id.ratio_fl);
        mRatio_tv = findViewById(R.id.ratio_tv);
        mBitrate_fl = findViewById(R.id.bitrate_fl);
        mBitrate_tv = findViewById(R.id.bitrate_tv);

        mBitrate_type_fl = findViewById(R.id.bitrate_type_fl);
        mBitrate_type_tv = findViewById(R.id.bitrate_type_tv);
        mFull_iv1 = findViewById(R.id.full_iv);
        mFocus_fl = findViewById(R.id.focus_fl);
        mFocus_tv = findViewById(R.id.focus_tv);

        mTp_volume = findViewById(R.id.tp_volume);
        mTv_volume = findViewById(R.id.tv_volume);
        mIv_volume = findViewById(R.id.iv_volume);


        mDevice_update_fl = findViewById(R.id.device_update_fl);
        mLl_high = findViewById(R.id.ll_high);
        mIv_high = findViewById(R.id.iv_high);

        mFrameLayout = findViewById(R.id.fl_video);

    }

    boolean isUpdate = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateConfig(EventBusUpdateRtmpConfig updateRtmpConfig) {
        if (isUpdate) {
//            initWebrtc();
            isUpdate=false;
        }

    }

    /**
     * 获取设备信息
     */
    private void getDeviceInfo() {
        Bundle extras = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
        deviceSerial = mDeviceInfo.getDevice_serial();

        mIsapi = ISAPI.getInstance();
        mIsapi.config(deviceSerial);
        mIsapi.setDevIndex(mDevIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTxVideoPlayer != null) {
            mTxVideoPlayer.startPlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTxVideoPlayer != null) {
            mTxVideoPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWjVideoPlayer != null) {
            mWjVideoPlayer.destroy();
            mWjVideoPlayer = null;
            WJRelationAssistUtil.getInstance().destroy();
        }
        if (mTxVideoPlayer != null) {
            mTxVideoPlayer.destroy();
            mTxVideoPlayer = null;
        }
        OkHttpUtils.getInstance().cancelAll();

        EventBus.getDefault().unregister(this);
    }

}
