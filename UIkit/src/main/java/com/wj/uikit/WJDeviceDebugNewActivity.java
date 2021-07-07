package com.wj.uikit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.videogo.openapi.EZPlayer;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.config.WJDeviceSceneEnum;
import com.wj.camera.config.WJRateTypeEnum;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.CheckDevcieUpdate;
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
import com.wj.uikit.player.WJRelationAssistUtil;
import com.wj.uikit.player.WJVideoPlayer;
import com.wj.uikit.player.event.WJReconnectEvent;
import com.wj.uikit.pop.FocusSelectPop;
import com.wj.uikit.pop.SelectPop;
import com.wj.uikit.uitl.OnControlClickListener;
import com.wj.uikit.view.TouchProgressView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_device_debug_new);

        getDeviceInfo();
        findView();
        initClick();
        initAction();
        getData();
        initAudio();
        isLatestVersion();
    }

    private void initAudio() {
        mIsapi.getAudio(new JsonCallback<TwoWayAudio>() {
            @Override
            public void onSuccess(TwoWayAudio data) {
                mTwoWayAudio = data;
                if (mTwoWayAudio != null && mTwoWayAudio.getTwoWayAudioChannel() != null) {
                    mTv_volume.setText(mTwoWayAudio.getTwoWayAudioChannel().getSpeakerVolume() + "");
                    mTp_volume.setProgress(Integer.valueOf(mTwoWayAudio.getTwoWayAudioChannel().getSpeakerVolume()));
                }
            }
        });

    }


    @SuppressLint("CheckResult")
    private void getData() {
        mIsapi = ISAPI.getInstance();
        mIsapi.config(deviceSerial);
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
        FrameLayout frameLayout = findViewById(R.id.fl_video);
        mWjVideoPlayer = new WJVideoPlayer(this);
        mWjVideoPlayer.init();
        WJReconnectEvent mWjReconnectEvent = new WJReconnectEvent();
        mWjReconnectEvent.setDeviceSerial(deviceSerial);
        // mWjVideoPlayer.setData("");
        mWjVideoPlayer.registerReconnect(mWjReconnectEvent);
        mWjVideoPlayer.attachContainer(frameLayout);
        mWjVideoPlayer.getWjControlCover().getWj_full_iv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WJRelationAssistUtil.getInstance().switchFull(WJDeviceDebugNewActivity.this, mWjVideoPlayer, frameLayout);
            }
        });
        //  mWjVideoPlayer.play();
        ISAPI.getInstance().getRTMP(mDeviceInfo.device_serial, new JsonCallback<RtmpConfig>() {
            @Override
            public void onSuccess(RtmpConfig data) {
                if (data != null && data.getRTMP() != null) {
                    String url = "";
                    if ("false".equals(data.getRTMP().getPrivatelyEnabled())) {
                        url = data.getRTMP().getPlayURL1() ;
                    } else {
                        url = data.getRTMP().getPlayURL2() ;
                    }
               /*     if (!TextUtils.isEmpty(url)) {
                        url = url.replace("https:", "webrtc:").replace(".flv", "");
                    }*/
                    if (mWjVideoPlayer != null) {
                        mWjVideoPlayer.setData(url+"");
                        mWjVideoPlayer.play();
                    }
                }

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
                checkDevice();
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
                        ISAPI.getInstance().wirelessServer(mDeviceInfo.device_serial);
                    }
                }).show();
            }
        });

        findViewById(R.id.fl_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(WJDeviceDebugNewActivity.this).asConfirm("设备重置", "你确定要重置设备？", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ISAPI.getInstance().factoryResetFull(deviceSerial);
                    }
                }).show();
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
        /*        BaseDeviceResponse<DeviceUpdateStatus> deviceUpdateStatus = DeviceApi.getInstance().deviceUpdateStatus(mDeviceInfo.device_serial);
                if (deviceResponse.getData() != null && deviceUpdateStatus != null) {
                    deviceResponse.getData().mDeviceResponse = deviceUpdateStatus.getData();
                }*/
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
                            if (responseData.getIsNeedUpgrade() == 1) {
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


                            if (responseData.getIsNeedUpgrade() == 1) {
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
    }

    @SuppressLint("CheckResult")
    public void deviceUpdate() {
        Observable.just(mDeviceInfo).map(new Function<DeviceInfo, BaseDeviceResponse<CheckDevcieUpdate>>() {
            @Override
            public BaseDeviceResponse apply(@NonNull DeviceInfo deviceInfo) throws Exception {
                BaseDeviceResponse deviceResponse = DeviceApi.getInstance().deviceUpdate(deviceInfo.device_serial);
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

        mDevice_update_fl = findViewById(R.id.device_update_fl);
        mLl_high = findViewById(R.id.ll_high);
        mIv_high = findViewById(R.id.iv_high);
    }

    /**
     * 获取设备信息
     */
    private void getDeviceInfo() {
        Bundle extras = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
        deviceSerial = mDeviceInfo.getDevice_serial();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWjVideoPlayer != null) {
            mWjVideoPlayer.destroy();
            mWjVideoPlayer = null;
            WJRelationAssistUtil.getInstance().destroy();
        }
    }

}
