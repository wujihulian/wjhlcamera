package com.wj.uikit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.videogo.openapi.EZPlayer;
import com.wj.camera.WJCamera;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.CheckDevcieUpdate;
import com.wj.camera.response.DeviceUpdateStatus;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.TwoWayAudio;
import com.wj.camera.response.VideoConfig;
import com.wj.camera.response.ZoomResponse;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.camera.view.WJPlayView;
import com.wj.camera.view.control.ErrorControl;
import com.wj.camera.view.control.LoadingControl;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.control.DeviceDebugControl;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.pop.FocusSelectPop;
import com.wj.uikit.pop.SelectPop;
import com.wj.uikit.view.TouchProgressView;

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
public class WJDeviceDebugActivity extends BaseUikitActivity {
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_device_debug);


        getDeviceInfo();
        findView();
        initClick();

        initAction();
        //  startAp();
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
                    Log.i(TAG, "onSuccess: " + mVideoConfig.getStreamingChannel().getVideo().getVbrUpperCap());
                    if (!TextUtils.isEmpty(mVideoConfig.getStreamingChannel().getVideo().getVbrUpperCap())) {
                        Integer value = Integer.valueOf(mVideoConfig.getStreamingChannel().getVideo().getVbrUpperCap());
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
    }

    private void initAction() {
        mPlayer = WJCamera.getInstance().createPlayer(deviceSerial, 1);
        mPlayer.setPlayVerifyCode(deviceCode);
        mWJPlayView.setEZPlayer(mPlayer);
        mWJPlayView.addControl(new LoadingControl(this));
        mWJPlayView.addControl(new ErrorControl(this));
        DeviceDebugControl control = new DeviceDebugControl(this, mDeviceInfo);
        mWJPlayView.addControl(control);

        //   mPlayer.startRealPlay();
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
                    mFocusSelectPop = new FocusSelectPop(WJDeviceDebugActivity.this);
                    mFocusSelectPop.setAtIndex(zoomIndex);
                    mFocusSelectPop.setListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int position) {
                            zoomIndex = position;
                            mFocus_tv.setText(s);
                            ISAPI.getInstance().zoom(position + 1);
                        }
                    });
                    new XPopup.Builder(WJDeviceDebugActivity.this).asCustom(mFocusSelectPop);
                }
                mFocusSelectPop.show();
            }
        });


        mRatio_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRatioSelectPop == null) {
                    mRatioSelectPop = new SelectPop(WJDeviceDebugActivity.this, xy);
                    new XPopup.Builder(WJDeviceDebugActivity.this).asCustom(mRatioSelectPop);
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
                                            Toast.makeText(WJDeviceDebugActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
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
                    mBitrateSelectPop = new SelectPop(WJDeviceDebugActivity.this, bitrateTitle);
                    new XPopup.Builder(WJDeviceDebugActivity.this).asCustom(mBitrateSelectPop);
                    mBitrateSelectPop.setListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int position) {

                            if (mVideoConfig != null) {
                                mVideoConfig.getStreamingChannel().getVideo().setVbrUpperCap(bitrate[position]);
                                mIsapi.setVideoConfig(mVideoConfig, new JsonCallback<ResponseStatus>() {
                                    @Override
                                    public void onSuccess(ResponseStatus data) {
                                        if (data.ResponseStatus != null && "1".equals(data.ResponseStatus.statusCode)) {
                                            mBitrate_tv.setText(bitrateTitle[position]);
                                        } else {
                                            Toast.makeText(WJDeviceDebugActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
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
                BaseDeviceResponse<CheckDevcieUpdate> deviceResponse = DeviceApi.getInstance().checkDeviceUpdate(deviceInfo.device_serial);
                BaseDeviceResponse<DeviceUpdateStatus> deviceUpdateStatus = DeviceApi.getInstance().deviceUpdateStatus(mDeviceInfo.device_serial);
                if (deviceResponse.getData() != null && deviceUpdateStatus != null) {
                    deviceResponse.getData().mDeviceResponse = deviceUpdateStatus.getData();
                }
                return deviceResponse;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(WJDeviceDebugActivity.this))
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
                                new XPopup.Builder(WJDeviceDebugActivity.this).asConfirm("检测到新版本", responseData.getCurrentVersion() + " 是否升级到 " + responseData.getLatestVersion(), "否", "是", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        deviceUpdate();

                                    }
                                }, null, false, 0).show();
                            } else {

                                // 最新版
                                new XPopup.Builder(WJDeviceDebugActivity.this).asConfirm("已经是最新版", responseData.getCurrentVersion(), null, "确定", new OnConfirmListener() {
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
        Intent intent = new Intent(WJDeviceDebugActivity.this, WJUpdateProgressActivity.class);
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
                .doOnSubscribe(new RxConsumer(WJDeviceDebugActivity.this))
                .subscribe(new Consumer<BaseDeviceResponse<CheckDevcieUpdate>>() {
                    @Override
                    public void accept(BaseDeviceResponse<CheckDevcieUpdate> checkDevcieUpdateBaseDeviceResponse) throws Exception {
                        if (checkDevcieUpdateBaseDeviceResponse.getCode() == 200) {
                            toUpdateProgress();
                        } else {
                            Toast.makeText(WJDeviceDebugActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }





    private void findView() {
        mBack_iv = findViewById(R.id.back_iv);
        mWJPlayView = findViewById(R.id.wj_playview);
        mRatio_fl = findViewById(R.id.ratio_fl);
        mRatio_tv = findViewById(R.id.ratio_tv);
        mBitrate_fl = findViewById(R.id.bitrate_fl);
        mBitrate_tv = findViewById(R.id.bitrate_tv);
        mFull_iv1 = findViewById(R.id.full_iv);
        mFocus_fl = findViewById(R.id.focus_fl);
        mFocus_tv = findViewById(R.id.focus_tv);

        mTp_volume = findViewById(R.id.tp_volume);
        mTv_volume = findViewById(R.id.tv_volume);

        mDevice_update_fl = findViewById(R.id.device_update_fl);
    }

    /**
     * 获取设备信息
     */
    private void getDeviceInfo() {
        Bundle extras = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
        deviceSerial = mDeviceInfo.device_serial;
        deviceCode = mDeviceInfo.device_code;
        deviceType = mDeviceInfo.device_type;


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer != null) {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            mCountDownTimer = new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    boolean b = mPlayer.startRealPlay();
                }
            };
            mCountDownTimer.start();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.stopRealPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
