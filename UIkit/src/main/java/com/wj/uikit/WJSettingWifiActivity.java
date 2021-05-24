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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.ezviz.pub.YsApManager;
import com.ap.ezviz.pub.ap.ApWifiConfigInfo;
import com.ap.ezviz.pub.ap.FIXED_IP;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.net.SafeGuardInterceptor;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.adapter.WifiListAdapter;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.uitl.WJActivityControl;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * FileName: SettingWifiActivity
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description:给设备配置wifi
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJSettingWifiActivity extends BaseUikitActivity implements OnItemClickListener<String> {
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
    private int mDeviceCode;
    private TextView mTv_to_wifi;
    private LinearLayout mLl_to_wifi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_setting_wifi);
        mWifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
        mDeviceCode = extras.getInt(WJDeviceConfig.DEVICE_CODE);

        mMode = extras.getInt(WJDeviceConfig.SUPPORT_APP_MODE);

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTv_to_wifi = findViewById(R.id.tv_to_wifi);
        mLl_to_wifi = findViewById(R.id.ll_to_wifi);
        boolean wifiEnabled = mWifiMgr.isWifiEnabled();

        if (wifiEnabled) {
            mLl_to_wifi.setVisibility(View.GONE);
        }
        mTv_to_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivityForResult(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY),200);
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200){
            boolean wifiEnabled = mWifiMgr.isWifiEnabled();
            if (wifiEnabled) {
                mLl_to_wifi.setVisibility(View.GONE);
            }
        }
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


        mCountDownTimer = new CountDownTimer(15000 * 4, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                List<ScanResult> scanResults = mWifiMgr.getScanResults();
                if (scanResults != null && scanResults.size() >= 1) {
                    mCountDownTimer.cancel();
                    mLl_to_wifi.setVisibility(View.GONE);
                    mWifiListAdapter = new WifiListAdapter();
                    mWifiListAdapter.setOnItemClickListener(WJSettingWifiActivity.this);
                    List<ScanResult> scanList = getScanList();
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
        clear();
    }

    @SuppressLint("CheckResult")
    public void checkDevice() {
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
                                        clear();
                                        EventBus.getDefault().post(mDeviceInfo);
                                        Toast.makeText(WJSettingWifiActivity.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                                        WJActivityControl.getInstance().finishActivity(WJSettingModeActivity.class);

                                        finish();
                                    }

                                    @Override
                                    public void onError(int code, String msg) {
                                        super.onError(code, msg);
                                        if (mLoadingPopupView != null) {
                                            mLoadingPopupView.dismiss();
                                        }
                                        clear();
                                        Toast.makeText(WJSettingWifiActivity.this, "注册平台失败", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {

                                int errorCode = result.getBaseException().getErrorCode();
                                if (errorCode == 120020) {
                                    clear();
                                    //设备已在线
                                    EventBus.getDefault().post(mDeviceInfo);
                                    Toast.makeText(WJSettingWifiActivity.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                                    WJActivityControl.getInstance().finishActivity(WJSettingModeActivity.class);
                                    finish();
                                } else {
                                    if (startApTime + 1000 * 100 <= System.currentTimeMillis()) {
                                        if (mLoadingPopupView != null) {
                                            mLoadingPopupView.dismiss();
                                        }
                                        showWiredHint();
                                    } else {
                                        handler.sendEmptyMessageDelayed(SEND_CHECK_DEVICE_MSG, 2000L);
                                    }

                                }
                            }
                        }
                    });
        }
    }

    private void clear() {
        if (handler != null) {
            handler.removeMessages(SEND_CHECK_DEVICE_MSG);
            handler.removeMessages(SEND_CHECK_ISAPI);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    //检查设备消息
    private final int SEND_CHECK_DEVICE_MSG = 1001;
    private final int SEND_CHECK_ISAPI = 1002;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_CHECK_DEVICE_MSG:
                    logPrint("正在注册设备到平台....");
                    checkDevice();
                    break;

                case SEND_CHECK_ISAPI:
                    logPrint("正在注册设备到平台....");
                    checkIsApi();
                    break;

            }
        }
    };

    @SuppressLint("CheckResult")
    private void checkIsApi() {
        Observable.just(mDeviceInfo).map(new Function<DeviceInfo, DeviceInfo>() {
            @Override
            public DeviceInfo apply(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) throws Exception {
                OkHttpClient mClient = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(new SafeGuardInterceptor())
                        .writeTimeout(2, TimeUnit.SECONDS)
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .readTimeout(2, TimeUnit.SECONDS).build();

                RtmpConfig rtmp = ISAPI.getInstance().getRTMP(mDeviceInfo.device_serial, mClient);
                Log.i(TAG, "apply: " + System.currentTimeMillis() + "    rtmp= " + new Gson().toJson(rtmp));
                if (rtmp == null || rtmp.getRTMP() == null) {
                    return deviceInfo;
                }
                deviceInfo.rtmpConfig = rtmp;
                return deviceInfo;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mLoadingPopupView.dismiss();
                        finish();
                    }
                }).doOnSubscribe(new RxConsumer(this))
                .subscribe(new Consumer<DeviceInfo>() {
                    @Override
                    public void accept(DeviceInfo deviceInfo) throws Exception {
                        if (deviceInfo.rtmpConfig == null || deviceInfo.rtmpConfig.getRTMP() == null) {
                            if (startApTime + 1000 * 100 <= System.currentTimeMillis()) {
                                if (mLoadingPopupView != null) {
                                    mLoadingPopupView.dismiss();
                                }
                                showWiredHint();
                            } else {
                                handler.sendEmptyMessageDelayed(SEND_CHECK_ISAPI, 1000L);
                            }
                        } else {
                            clear();
                            EventBus.getDefault().post(mDeviceInfo);
                            Toast.makeText(WJSettingWifiActivity.this, "注册平台成功", Toast.LENGTH_SHORT).show();
                            WJActivityControl.getInstance().finishActivity(WJSettingModeActivity.class);
                            finish();
                        }
                    }
                });
    }

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
        List<ScanResult> allData = mWifiListAdapter.getAllData();
        String  bssid ="";

        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).SSID.equals("HAP_"+ mDeviceInfo.device_serial)) {
                bssid=allData.get(i).BSSID;
                break;
            };
        }
        Log.i(TAG, "onClick: "+bssid);

        startAp(data.SSID, wifiPssword,bssid);


    }


    private long startApTime;

    public void startAp(String wifiSsid, String wifiPassword,String  bssid) {
        String password = "AP" + mDeviceInfo.device_code;
        //"EZVIZ_"+设备序列号
        String ssid = "HAP_" + mDeviceInfo.device_serial;

        WifiUtils.withContext(getApplicationContext())
                .connectWith(ssid,password)
                .setTimeout(15000)
                .onConnectionResult(new ConnectionSuccessListener() {
                    @Override
                    public void success() {
                        mLoadingPopupView = new XPopup.Builder(WJSettingWifiActivity.this).dismissOnTouchOutside(false).setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onDismiss(BasePopupView popupView) {
                                if (handler!=null) {
                                    handler.removeMessages(SEND_CHECK_DEVICE_MSG);
                                    handler.removeMessages(SEND_CHECK_ISAPI);
                                }
                            }
                        }).asLoading();
                        mLoadingPopupView.show();


                        ApWifiConfigInfo apConfigInfo = new ApWifiConfigInfo.Builder()
                                .deviceSN(mDeviceInfo.device_serial)  // 设备序列号，非必要
                                .activatePwd("Hik" + mDeviceInfo.device_code)  // 激活密码，萤石接入的海康设备规则为 Hik+验证码。这里不传会使用默认 Hik+验证码
                                .verifyCode(mDeviceInfo.device_code)  // 设备验证码，必要
                                .withWiFi(wifiSsid, wifiPassword) // wifi ssid 和 密码，必要
                                .fixedIP(FIXED_IP.Companion.getWIRELESS_IPC_YS())  // 设备固定IP。这里针对萤石设备有  192.168.8.1 和 192.168.8.253 两款兼容，可扩展更多的ip
                                .build();

                        Log.i(TAG, "success: "+wifiSsid +" ----- "+wifiPassword);
                        YsApManager.INSTANCE.activateWifi(apConfigInfo, new YsApManager.ApActivateCallback() {
                            @Override
                            public void onStartSearch() {
                                logPrint("第一步：正在搜索并获取设备信息");
                            }

                            @Override
                            public void onStartActivate() {
                                logPrint("第二步：开始激活");
                            }

                            @Override
                            public void onStartConfigWifi() {
                                logPrint("第三步：开始配置wifi");
                            }

                            @Override
                            public void onSuccess() {
                                logPrint("wifi 配置成功！！");
                                if (handler!=null) {
                                    startApTime = System.currentTimeMillis();
                                    if (mDeviceCode == 120020) {
                                        //已经添加过设备
                                        handler.sendEmptyMessageDelayed(SEND_CHECK_ISAPI, 2000L);
                                    } else {
                                        handler.sendEmptyMessageDelayed(SEND_CHECK_DEVICE_MSG, 2000L);
                                    }
                                }
                            }

                            @Override
                            public void onFailed(int code, @NotNull String msg, @org.jetbrains.annotations.Nullable Throwable exception) {
                                // logPrint(format);
                                if (mLoadingPopupView != null) {
                                    mLoadingPopupView.dismiss();
                                }
                                showWiredHint();

                            }
                        });

                    }

                    @Override
                    public void failed(@NonNull ConnectionErrorCode errorCode) {
                        Log.i(TAG, "failed: " + errorCode.name());
                        if (errorCode == ConnectionErrorCode.USER_CANCELLED) {

                        } else {
                            new XPopup.Builder(WJSettingWifiActivity.this)
                                    .asConfirm("设备无法正常进入配网模式",
                                            "1.请检查设备是否通电\n2.通电后设备指示灯是否进入白灯闪烁状态",
                                            "取消",
                                            "重试",
                                            new OnConfirmListener() {
                                                @Override
                                                public void onConfirm() {
                                                    startAp(wifiSsid, wifiPassword,bssid);
                                                }
                                            },
                                            null,
                                            false,
                                            0
                                    ).show();
                        }
                        //  Toast.makeText(WJSettingWifiActivity.this, "找不到设备", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();

    }

    public void logPrint(String log) {
        if (mLoadingPopupView != null) {
            mLoadingPopupView.setTitle(log);
        }
        Log.i(TAG, "logPrint: " + log);
    }


    @SuppressLint("CheckResult")
    protected void toast(String text) {
        Observable.just(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(WJSettingWifiActivity.this, s, Toast.LENGTH_LONG).show();

                    }
                });

    }

    public void showWiredHint() {
        new XPopup.Builder(this).asConfirm("无线网络连接失败", "1.关闭无线网络重新搜索尝试 \n2.检查输入的无线网络密码是否正确。", new OnConfirmListener() {
            @Override
            public void onConfirm() {

            }
        }).show();
    }

}
