package com.wj.uikit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.videogo.wificonfig.APWifiConfig;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.adapter.WifiListAdapter;
import com.wj.uikit.db.DeviceInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * FileName: SettingWifiActivity
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description:给设备配置wifi 老接口配网
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJSettingWifiActivityOld extends BaseUikitActivity implements OnItemClickListener<String> {
    private static final String TAG = "SettingWifiActivity";
    private CountDownTimer mCountDownTimer;
    private RecyclerView mRecyclerView;
    private WifiManager mWifiMgr;

    //配网模式
    private int mMode;

    //设备信息
    private DeviceInfo mDeviceInfo;
    private WifiListAdapter mWifiListAdapter;
    private LoadingPopupView mLoadingPopupView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_setting_wifi);

        getData();
        initView();
        registerPermission();
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
        mMode = extras.getInt(WJDeviceConfig.SUPPORT_APP_MODE);

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void registerPermission() {
        //动态获取定位权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                    100);

        } else {
            getWifiList();
        }
    }

    private void getWifiList() {


        mWifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mCountDownTimer = new CountDownTimer(15000 * 4, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                List<ScanResult> scanResults = mWifiMgr.getScanResults();
                if (scanResults != null && scanResults.size() >= 1) {
                    mCountDownTimer.cancel();
                    mWifiListAdapter = new WifiListAdapter();
                    mWifiListAdapter.setOnItemClickListener(WJSettingWifiActivityOld.this);
                    List<ScanResult> scanList = getScanList();
                    for (ScanResult scanResult : scanList) {
                        //   Log.i(TAG, "onTick: " + scanResult.toString());
                    }
                    mWifiListAdapter.setData(scanList);
                    mRecyclerView.setAdapter(mWifiListAdapter);
                }

            }

            @Override
            public void onFinish() {
            }
        };
        mCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (handler != null) {
            handler.removeMessages(SEND_CHECK_DEVICE_MSG);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        EZOpenSDK.getInstance().stopAPConfigWifiWithSsid();
    }

    @SuppressLint("CheckResult")
    public void checkDevice() {
        Log.i(TAG, "checkDevice: ");
        if (mDeviceInfo != null) {
            Observable.just(mDeviceInfo)
                    .map(new Function<DeviceInfo, EZProbeDeviceInfoResult>() {
                        @Override
                        public EZProbeDeviceInfoResult apply(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) throws Exception {
                            EZProbeDeviceInfoResult result = EZOpenSDK.getInstance().probeDeviceInfo(deviceInfo.device_serial, deviceInfo.device_type);
                            return result;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mLoadingPopupView.dismiss();
                            finish();
                        }
                    }).doOnSubscribe(new RxConsumer(this))
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            handler.sendEmptyMessageDelayed(SEND_CHECK_DEVICE_MSG, 2000L);
                        }
                    })
                    .subscribe(new Consumer<EZProbeDeviceInfoResult>() {
                        @Override
                        public void accept(EZProbeDeviceInfoResult result) throws Exception {
                            if (result.getBaseException() == null) {
                               // toast("开始添加设备");
                                DeviceApi.getInstance().addDevie(mDeviceInfo.device_serial, mDeviceInfo.device_code, new JsonCallback<BaseDeviceResponse>() {
                                    @Override
                                    public void onSuccess(BaseDeviceResponse data) {
                                        if (mLoadingPopupView != null) {
                                            mLoadingPopupView.dismiss();
                                        }
                                        EventBus.getDefault().post(mDeviceInfo);
                                        Toast.makeText(WJSettingWifiActivityOld.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(int code, String msg) {
                                        super.onError(code, msg);
                                        if (mLoadingPopupView != null) {
                                            mLoadingPopupView.dismiss();
                                        }
                                        Toast.makeText(WJSettingWifiActivityOld.this, "注册平台失败", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {

                                int errorCode = result.getBaseException().getErrorCode();
                                if (errorCode == 120020) {
                                    toast("注册平台成功");
                                    //设备已在线
                                    EventBus.getDefault().post(mDeviceInfo);
                                    Toast.makeText(WJSettingWifiActivityOld.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    handler.sendEmptyMessageDelayed(SEND_CHECK_DEVICE_MSG, 2000L);

                                    //toast(errorCode + "");
                                }
                            }
                        }
                    });
        }
    }

    //检查设备消息
    private final int SEND_CHECK_DEVICE_MSG = 1001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_CHECK_DEVICE_MSG:
                    checkDevice();
                    break;
            }
        }
    };

    public List<ScanResult> getScanList() {
        if (mWifiMgr != null) {
            List<ScanResult> olist = mWifiMgr.getScanResults();
            if (olist != null) {
                List<ScanResult> nlist = new ArrayList<>();
                for (int i = 0; i < olist.size(); i++) {

                    // 该热点SSID是否已在列表中
                    int position = getItemPosition(nlist, olist.get(i));
                    if (position != -1) { // 已在列表
                        // 相同SSID热点，取信号强的
                        if (nlist.get(position).level < olist.get(i).level) {
                            nlist.remove(position);
                            nlist.add(position, olist.get(i));
                        }
                    } else {
                        //过滤隐藏的网络
                        nlist.add(olist.get(i));
                    }
                }
                Collections.sort(nlist, new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult o1, ScanResult o2) {
                        return o2.level - o1.level;
                    }
                });
                return nlist;
            }
        }
        return null;
    }


    /**
     * 返回item在list中的坐标
     */
    private int getItemPosition(List<ScanResult> list, ScanResult item) {
        for (int i = 0; i < list.size(); i++) {
            if (item.SSID.equals(list.get(i).SSID)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Log.i(TAG, "onRequestPermissionsResult: ");
            getWifiList();
            //registerPermission();
        }
    }

    @Override
    public void onClick(String wifiPssword, int position) {
        ScanResult data = mWifiListAdapter.getData(position);
        Log.i(TAG, "onClick: ");
        if (true) {
            startAp1(data.SSID, wifiPssword);
            return;
        }

      /*  if (mMode == 2) {
            Log.i(TAG, "onClick: 开始ap配网");
            startAp(data.SSID, wifiPssword);
        } else if (mMode == 3) {
            Log.i(TAG, "onClick: 开始wifi配网");
            //  startWifi(data.SSID, wifiPssword);
            startAp(data.SSID, wifiPssword);
        } else if (mMode == 1) {
            //声波配网
            Toast.makeText(this, "该设备 暂不支持配网", Toast.LENGTH_LONG).show();
        }*/
    }


    public void startAp1(String wifiSsid, String wifiPassword) {
        EZOpenSDK.getInstance().stopAPConfigWifiWithSsid();

        Log.i(TAG, "startAp1: ");
        String password = "AP" + mDeviceInfo.device_code;
        //"EZVIZ_"+设备序列号
        String ssid = "HAP_" + mDeviceInfo.device_serial;
 /*       password = "EZVIZ_" + mDeviceInfo.device_code;
        //"EZVIZ_"+设备序列号
        ssid = "EZVIZ_" + mDeviceInfo.device_serial;*/

        //   Log.i(TAG, "startAp1:  " +String.format(" ssid= %s   password =%s ",));
        Log.i(TAG, "startAp1:" + mDeviceInfo.device_code);
        Log.i(TAG, "startAp1:" + mDeviceInfo.device_serial);
        String deviceCode = "Hik" + mDeviceInfo.device_code;
        // String deviceCode = mDeviceInfo.device_code;
        //  Log.i(TAG, "startAp1:" + deviceCode);

        EZOpenSDK.getInstance().startAPConfigWifiWithSsid(wifiSsid, wifiPassword, mDeviceInfo.device_serial, deviceCode, ssid, password, true, new APWifiConfig.APConfigCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: 成功配网");
                toast("配网成功");
                /*EventBus.getDefault().post(mDeviceInfo);
                finish();*/
                mLoadingPopupView = new XPopup.Builder(WJSettingWifiActivityOld.this).setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onDismiss(BasePopupView popupView) {
                        handler.removeMessages(SEND_CHECK_DEVICE_MSG);
                    }
                }).asLoading();
                mLoadingPopupView.setTitle("正在注册设备到平台....");
                mLoadingPopupView.show();

                handler.sendEmptyMessageDelayed(SEND_CHECK_DEVICE_MSG, 2000L);
                EZOpenSDK.getInstance().stopAPConfigWifiWithSsid();
            }

            @Override
            public void OnError(int code) {
                Log.i(TAG, "OnError:" + code);
                EZOpenSDK.getInstance().stopAPConfigWifiWithSsid();
                switch (code) {
                    case 15:
                        // TODO: 2018/7/24 超时
                       // toast("配网超时");
                        break;
                    case 1:
                        // TODO: 2018/7/24 参数错误
                        break;
                    case 2:
                        // TODO: 2018/7/24 设备ap热点密码错误
                        break;
                    case 3:
                        // TODO: 2018/7/24  连接ap热点异常
                        break;
                    case 4:
                        // TODO: 2018/7/24 搜索WiFi热点错误
                        break;
                    default:
                        // TODO: 2018/7/24 未知错误
                        // 更多错误码请见枚举类 EZConfigWifiErrorEnum 相关说明
                        break;
                }
            }
        });
    }


    @SuppressLint("CheckResult")
    protected void toast(String text) {
        Observable.just(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(WJSettingWifiActivityOld.this, s, Toast.LENGTH_LONG).show();

                    }
                });

    }

}
