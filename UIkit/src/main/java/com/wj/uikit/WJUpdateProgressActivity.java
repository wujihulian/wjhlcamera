package com.wj.uikit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.DeviceUpdateStatus;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.view.WJProgressView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: WJUpdateProgressActivity
 * Author: xiongxiang
 * Date: 2021/5/6
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJUpdateProgressActivity extends BaseUikitActivity {


    private WJProgressView mWj_progress;
    private DeviceInfo mDeviceInfo;
    private TextView mTv_status;
    private final int SEND_PROGRESS = 0x1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_PROGRESS:
                    getProgress();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_update_progress);
        getDeviceInfo();
        initView();
        //getProgress();
        sendProgress();
    }

    /**
     * 获取设备信息
     */
    private void getDeviceInfo() {
        Bundle extras = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
    }

    @SuppressLint("CheckResult")
    private void getProgress() {
        Observable.just(mDeviceInfo).map(new Function<DeviceInfo, BaseDeviceResponse<DeviceUpdateStatus>>() {
            @Override
            public BaseDeviceResponse<DeviceUpdateStatus> apply(@NonNull DeviceInfo deviceInfo) throws Exception {
                BaseDeviceResponse<DeviceUpdateStatus> deviceResponse = DeviceApi.getInstance().deviceUpdateStatus(mDeviceInfo.device_serial);
                return deviceResponse;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(WJUpdateProgressActivity.this))
                .subscribe(new Consumer<BaseDeviceResponse<DeviceUpdateStatus>>() {
                    @Override
                    public void accept(BaseDeviceResponse<DeviceUpdateStatus> deviceUpdateStatusBaseDeviceResponse) throws Exception {
                        DeviceUpdateStatus data = deviceUpdateStatusBaseDeviceResponse.getData();

                       // Gson gson = new Gson();
                       // Log.i("getProgress", "accept: " + gson.toJson(data));
                        if (data != null) {
                            if (data.getStatus() == 0) {
                                mWj_progress.setProgressSmooth(data.getProgress());
                                mTv_status.setText("正在升级");
                                sendProgress();
                            } else if (data.getStatus() == 1) {
                                mTv_status.setText("设备重启中");
                                sendProgress();
                            } else if (data.getStatus() == 2) {

                                if (data.getProgress() == 100) {
                                    mTv_status.setText("设备重启中");
                                    mWj_progress.setProgressSmooth(data.getProgress());
                                    sendProgress();
                                } else {
                                    mWj_progress.setProgressSmooth(100);
                                    mTv_status.setText("升级成功");
                                }
                            } else if (data.getStatus() > 2) {
                                mTv_status.setText("升级失败");
                            }
                         /*   if (data.getStatus() > 2) {
                                mTv_status.setText("升级失败");
                            } else {
                                mWj_progress.setProgressSmooth(data.getProgress());
                                if (data.getProgress() < 100) {
                                    mTv_status.setText("正在升级");
                                } else {
                                    mTv_status.setText("升级成功");
                                }
                                sendProgress();
                            }*/
                        } else {
                            sendProgress();
                        }

                    }
                });

    }

    private void initView() {
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTv_status = findViewById(R.id.tv_status);
        mWj_progress = findViewById(R.id.wj_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(SEND_PROGRESS);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void sendProgress() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(SEND_PROGRESS, 2500);
        }
    }
}
