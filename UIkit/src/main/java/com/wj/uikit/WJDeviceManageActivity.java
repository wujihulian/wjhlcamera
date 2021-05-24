package com.wj.uikit;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.net.SafeGuardInterceptor;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.DeviceCameraData;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.uitl.DPUtil;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.adapter.WJDeviceManageAdapter;
import com.wj.uikit.db.DBHelper;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.subscribe.LoadingObserver;
import com.wj.uikit.subscribe.SingleObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * FileName: WJDevicemanageActivity
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description: 设备管理
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceManageActivity extends BaseUikitActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private WJDeviceManageAdapter mAdapter;
    private LoadingPopupView mLoadingPopupView;
    private View mWj_device_default;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.wj_activity_device_manage);
        initView();
        getData();
    }

    @SuppressLint("CheckResult")
    private void getData() {

        Observable.just("1")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<DeviceInfo>>() {
                    @Override
                    public List<DeviceInfo> apply(@io.reactivex.annotations.NonNull String dbHelper) throws Exception {
                        return DBHelper.getInstance().queryAll();
                    }
                }).map(new Function<List<DeviceInfo>, DeviceInfo[]>() {
            @Override
            public DeviceInfo[] apply(@io.reactivex.annotations.NonNull List<DeviceInfo> deviceInfos) throws Exception {
                DeviceInfo[] infos = new DeviceInfo[deviceInfos.size()];
                return deviceInfos.toArray(infos);
            }
        }).doOnSubscribe(new RxConsumer(this))
                .subscribe(new Consumer<DeviceInfo[]>() {
                    @Override
                    public void accept(DeviceInfo[] deviceInfos) throws Exception {
                        getDeviceData(deviceInfos);
                    }
                });
    }

    private static final String TAG = "WJDeviceManageActivity";

    @SuppressLint("CheckResult")
    private void getDeviceData(DeviceInfo[] deviceInfos) {
        Observable.fromArray(deviceInfos)
                .map(new Function<DeviceInfo, DeviceInfo>() {
                    @Override
                    public DeviceInfo apply(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) throws Exception {
                        BaseDeviceResponse<List<DeviceCameraData>> deviceCameraData = DeviceApi.getInstance().deviceCameraInfo(deviceInfo.device_serial);
                        if (deviceCameraData != null && deviceCameraData.getData() != null && deviceCameraData.getData().size() >= 1) {
                            deviceInfo.deviceCamera = deviceCameraData.getData().get(0);
                        }
                        return deviceInfo;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(this))
                .subscribe(new SingleObserver<DeviceInfo>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) {
                        for (DeviceInfo allDatum : mAdapter.getAllData()) {
                            if (allDatum.device_serial.equals(deviceInfo.device_serial)) {
                                allDatum = deviceInfo;
                                return;
                            }
                        }
                        mAdapter.addData(deviceInfo);
                        mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                        mWj_device_default.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() == 0) {
                            mWj_device_default.setVisibility(View.VISIBLE);
                        } else {
                            mWj_device_default.setVisibility(View.GONE);
                        }
                    }
                });


    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mWj_device_default = findViewById(R.id.wj_device_default);
        findViewById(R.id.device_add).setOnClickListener(this);
        findViewById(R.id.back_iv).setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new WJDeviceManageAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition == 0) {
                    outRect.set(0, DPUtil.dip2px(WJDeviceManageActivity.this, 14), 0, DPUtil.dip2px(WJDeviceManageActivity.this, 14));
                } else {
                    outRect.set(0, 0, 0, DPUtil.dip2px(WJDeviceManageActivity.this, 14));
                }
            }
        });

        mAdapter.setOnRemoveItemListener(new OnItemClickListener<DeviceInfo>() {
            @Override
            public void onClick(DeviceInfo deviceInfo, int position) {
                new XPopup.Builder(WJDeviceManageActivity.this).asConfirm("删除设备", "你确定要删除当前设备？", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        DBHelper.getInstance().delete(deviceInfo.device_serial);
                        mAdapter.remove(position);
                        mAdapter.notifyDataSetChanged();

                        if (mAdapter.getItemCount() == 0) {
                            mWj_device_default.setVisibility(View.VISIBLE);
                        } else {
                            mWj_device_default.setVisibility(View.GONE);
                        }
                    }
                }).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDevice(DeviceInfo deviceInfo) {
        boolean query = DBHelper.getInstance().query(deviceInfo.device_serial);
        Log.i(TAG, "addDevice: " + query);
        DeviceInfo[] deviceInfos = {deviceInfo};
        if (query == false) {
            Log.i(TAG, "addDevice: insert");
            DBHelper.getInstance().insert(deviceInfo);
        }
        getDeviceData(deviceInfos);
        // add(deviceInfo);

    }

    public void add(DeviceInfo deviceInfo) {
        Observable.create(new ObservableOnSubscribe<DeviceInfo>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<DeviceInfo> emitter) throws Exception {
                boolean isApi = false;
                for (int i = 0; i < 2; i++) {
                    OkHttpClient mClient = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .addInterceptor(new SafeGuardInterceptor())
                            .writeTimeout(1 * 1000, TimeUnit.SECONDS)
                            .connectTimeout(1 * 1000, TimeUnit.SECONDS)
                            .readTimeout(1 * 1000, TimeUnit.SECONDS).build();

                    RtmpConfig rtmp = ISAPI.getInstance().getRTMP(deviceInfo.device_serial, mClient);
                    if (rtmp == null || rtmp.getRTMP() == null) {

                    } else {
                        isApi = true;

                        break;
                    }
                }
                if (isApi == false) {
                    emitter.onError(new Throwable("设备不在线"));
                    return;
                }

                BaseDeviceResponse<List<DeviceCameraData>> deviceCameraData = DeviceApi.getInstance().deviceCameraInfo(deviceInfo.device_serial);
                if (deviceCameraData != null && deviceCameraData.getData() != null && deviceCameraData.getData().size() >= 1) {
                    deviceInfo.deviceCamera = deviceCameraData.getData().get(0);
                    emitter.onNext(deviceInfo);
                    //Log.i(TAG, "subscribe: " + new Gson().toJson(deviceInfo));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(this))
                .subscribe(new LoadingObserver<DeviceInfo>(this) {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) {
                        for (DeviceInfo allDatum : mAdapter.getAllData()) {
                            if (allDatum.device_serial.equals(deviceInfo.device_serial)) {
                                allDatum = deviceInfo;
                                return;
                            }
                        }
                        mAdapter.addData(deviceInfo);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        super.onError(e);
                        new XPopup.Builder(WJDeviceManageActivity.this).dismissOnTouchOutside(false).dismissOnBackPressed(false).asConfirm("设备未就绪", "是否直接跳转配网?", "否", "是", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                      /*          IntentIntegrator integrator = new IntentIntegrator(WJDeviceManageActivity.this);
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                                integrator.setCaptureActivity(WJCaptureActivity.class);
                                integrator.initiateScan();*/

                                Intent intent = new Intent(WJDeviceManageActivity.this, WJSettingModeActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }, null, false, 0).show();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Log.i(TAG, "onComplete: ");
                        mAdapter.notifyDataSetChanged();

                        if (mAdapter.getItemCount() == 0) {
                            mWj_device_default.setVisibility(View.VISIBLE);
                        } else {
                            mWj_device_default.setVisibility(View.GONE);
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.device_add) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setCaptureActivity(WJCaptureActivity.class);
            integrator.initiateScan();
        } else if (id == R.id.back_iv) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
