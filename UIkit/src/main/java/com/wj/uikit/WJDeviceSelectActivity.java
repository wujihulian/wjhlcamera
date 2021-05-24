package com.wj.uikit;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.DeviceCameraData;
import com.wj.camera.uitl.DPUtil;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.adapter.WJDeviceSelectAdapter;
import com.wj.uikit.db.DBHelper;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.subscribe.LoadingObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: DeviceSelectActivity
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceSelectActivity extends BaseUikitActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private WJDeviceSelectAdapter mAdapter;
    private int selectMax;
    private View mWj_device_default;
    private ArrayList<String> mSelectDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.wj_activity_device_select);
        selectMax = getIntent().getIntExtra("selectMax", 4);
        mSelectDevice = getIntent().getStringArrayListExtra("selectDevice");
        initView();
        getData();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mWj_device_default = findViewById(R.id.wj_device_default);
        findViewById(R.id.device_add).setOnClickListener(this);
        findViewById(R.id.confirm_tv).setOnClickListener(this);
        findViewById(R.id.back_iv).setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WJDeviceSelectAdapter(selectMax);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition == 0) {
                    outRect.set(0, DPUtil.dip2px(WJDeviceSelectActivity.this, 14), 0, DPUtil.dip2px(WJDeviceSelectActivity.this, 14));
                } else if (childAdapterPosition == mAdapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, DPUtil.dip2px(WJDeviceSelectActivity.this, 80));
                } else {
                    outRect.set(0, 0, 0, DPUtil.dip2px(WJDeviceSelectActivity.this, 14));
                }
            }
        });

        mAdapter.setOnRemoveItemListener(new OnItemClickListener<DeviceInfo>() {
            @Override
            public void onClick(DeviceInfo deviceInfo, int position) {
                new XPopup.Builder(WJDeviceSelectActivity.this).asConfirm("删除设备", "你确定要删除当前设备？", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        DBHelper.getInstance().delete(deviceInfo.device_serial);
                        mAdapter.remove(position);
                        mAdapter.notifyDataSetChanged();

                        if (mAdapter.getItemCount()==0){
                            mWj_device_default.setVisibility(View.VISIBLE);
                        }else {
                            mWj_device_default.setVisibility(View.GONE);
                        }
                    }
                }).show();
            }
        });
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

    private static final String TAG = "WJDeviceSelectActivity";

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
                .subscribe(new LoadingObserver<DeviceInfo>(this) {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) {
                        for (DeviceInfo allDatum : mAdapter.getAllData()) {
                            if (allDatum.device_serial.equals(deviceInfo.device_serial)) {
                                allDatum=deviceInfo;
                                return;
                            }
                        }
                        mWj_device_default.setVisibility(View.GONE);
                        mAdapter.addData(deviceInfo);
                        mAdapter.notifyItemChanged(mAdapter.getItemCount()-1);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();

                        if (mSelectDevice != null) {
                            HashMap<String, Boolean> hashMap = new HashMap<>();
                            for (String s : mSelectDevice) {
                                hashMap.put(s, true);
                            }
                            mAdapter.setSelectHashMap(hashMap);
                            mSelectDevice = null;
                        }

                        mAdapter.notifyDataSetChanged();

                        if (mAdapter.getItemCount() == 0) {
                            mWj_device_default.setVisibility(View.VISIBLE);
                        } else {
                            mWj_device_default.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDevice(DeviceInfo deviceInfo) {
        boolean query = DBHelper.getInstance().query(deviceInfo.device_serial);
        DeviceInfo[] deviceInfos = {deviceInfo};

        if (query == false) {
            DBHelper.getInstance().insert(deviceInfo);
        }
        getDeviceData(deviceInfos);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.device_add) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(WJCaptureActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.initiateScan();
        } else if (id == R.id.back_iv) {
            finish();
        } else if (id == R.id.confirm_tv) {
            List<DeviceInfo> allData = mAdapter.getAllSelect();
            EventBus.getDefault().post(allData);
            finish();

        } else if (id == R.id.back_iv) {
            finish();
        }
    }


}
