package com.wj.uikit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.AddCameraInfoResultResponse;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.DeviceUpdateStatus;
import com.wj.camera.response.NetworkInterface;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.uitl.WJActivityControl;
import com.wj.uikit.view.WJProgressView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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


    private LoadingPopupView mLoadingPopupView;
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

    int progress = -1;

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
                        WJLogUitl.i("getProgress", "accept: " + new Gson().toJson(data));
                        if (data != null) {
                            if (data.getProgress() > progress) {
                                progress = data.getProgress() + 1;
                            } else {
                                progress = progress < 100 ? progress + 1 : 100;
                            }

                            if (data.getStatus() == 0) {

                                mWj_progress.setProgressSmooth(progress);
                                mTv_status.setText("正在升级");
                                sendProgress();
                            } else if (data.getStatus() == 1) {
                                mTv_status.setText("设备重启中");
                                sendProgress();
                            } else if (data.getStatus() == 2) {
                                if (data.getProgress() == 0) {
                                    return;
                                }
                                addDevice();
                                if (data.getProgress() == 100) {
                                    mTv_status.setText("设备重启中");
                                    mWj_progress.setProgressSmooth(progress);
                                    sendProgress();
                                } else {
                                    mWj_progress.setProgressSmooth(100);
                                    mTv_status.setText("升级成功");
                                }
                                if (handler != null) {
                                    handler.removeMessages(SEND_PROGRESS);
                                    handler.removeCallbacksAndMessages(null);
                                    handler = null;
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

    /**
     * 添加设备到后台
     */
    public void addDevice() {
        if (mLoadingPopupView != null) {
            mLoadingPopupView.show();
        }
        DeviceApi.getInstance().addDevice(mDeviceInfo.device_serial, mDeviceInfo.device_code, new JsonCallback<AddCameraInfoResultResponse>() {
            @Override
            public void onSuccess(AddCameraInfoResultResponse data) {
                if (mLoadingPopupView != null) {
                    mLoadingPopupView.dismiss();
                }
                post(mDeviceInfo);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                if (mLoadingPopupView != null) {
                    mLoadingPopupView.dismiss();
                }
                Toast.makeText(WJUpdateProgressActivity.this, "注册平台失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void post(DeviceInfo deviceInfo) {
        WJLogUitl.d("这个就是全部的数据", new Gson().toJson(deviceInfo));
        ISAPI.getInstance().getNetworkInterface(deviceInfo.device_serial, new JsonCallback<NetworkInterface>() {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                EventBus.getDefault().post(mDeviceInfo);
                Toast.makeText(WJUpdateProgressActivity.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                WJActivityControl.getInstance().finishActivity(WJSettingModeActivity.class);
                WJActivityControl.getInstance().finishActivity(WJDeviceDebugNewActivity.class);
                finish();
            }

            @Override
            public void onSuccess(NetworkInterface data) {

                if (data != null) {
                    NetworkInterface.NetworkInterfaceListDTO networkInterfaceList = data.getNetworkInterfaceList();
                    if (networkInterfaceList != null) {
                        List<NetworkInterface.NetworkInterfaceListDTO.NetworkInterfaceDTO> networkInterface = networkInterfaceList.getNetworkInterface();
                        if (networkInterface != null && networkInterface.size() >= 2) {
                            deviceInfo.setIpAaddress(networkInterface.get(1).getIPAddress().getIpAddress());


                        }
                    }
                }
                EventBus.getDefault().post(deviceInfo);
                Toast.makeText(WJUpdateProgressActivity.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                WJActivityControl.getInstance().finishActivity(WJSettingModeActivity.class);
                WJActivityControl.getInstance().finishActivity(WJDeviceDebugNewActivity.class);
                finish();
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

        mLoadingPopupView = new XPopup.Builder(WJUpdateProgressActivity.this).dismissOnTouchOutside(false).asLoading();


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
