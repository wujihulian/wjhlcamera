package com.wj.uikit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ap.ezviz.pub.ap.ApWifiConfigInfo;
import com.ap.ezviz.pub.ap.FIXED_IP;
import com.ap.ezviz.pub.http.APHttpClient;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.pop.EditPop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * FileName: WJLogQueryActivity
 * Author: xiongxiang
 * Date: 2022/4/27
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJLogQueryActivity extends BaseUikitActivity {
    private static final String TAG = "WJLogQueryActivity";
    private WifiManager mWifiMgr;
    //设备信息
    private DeviceInfo mDeviceInfo;
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_log_query);
        mTextView = findViewById(R.id.tv);
        mWifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        getData();
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(day);
        getAppConfigLog(format);
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = appendLog(mTextView.getText() + "");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (file == null) {
                                    Toast.makeText(v.getContext(), "保存失败:" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(v.getContext(), "保存至:" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPop editPop = new EditPop(v.getContext(), "请输入查询时间:" + format);
                editPop.setOnConfirmListener(new OnItemClickListener<String>() {
                    @Override
                    public void onClick(String s, int position) {
                        if (TextUtils.isEmpty(s)) {
                            getAppConfigLog(format);
                        } else {
                            getAppConfigLog(s);
                        }
                    }
                });
                new XPopup.Builder(v.getContext()).asCustom(editPop).show();
            }
        });
    }

    public static String getDiskCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
    }
    public boolean removeWifiConfig(String SSID) {
        WifiManager wifiManager = mWifiMgr;
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        SSID = "\"" + SSID + "\"";
        for (WifiConfiguration config : configs) {
            if (SSID.equals(config.SSID)) {
                boolean result = wifiManager.removeNetwork(config.networkId);
                wifiManager.saveConfiguration();
                return result;
            }
        }
        return false;
    }

    @SuppressLint("CheckResult")
    private void getAppConfigLog(String time) {
        Log.i(TAG, "getAppConfigLog: "+time);
        String password = "AP" + mDeviceInfo.device_code;
        //"EZVIZ_"+设备序列号
        String ssid = "HAP_" + mDeviceInfo.device_serial;
        removeWifiConfig(ssid);
        WifiUtils.withContext(getApplicationContext())
                .connectWith(ssid, password)
                .setTimeout(15000)
                .onConnectionResult(new ConnectionSuccessListener() {
                    @Override
                    public void success() {
                        getLog(time);
                    }
                    @Override
                    public void failed(@NonNull ConnectionErrorCode errorCode) {

                    }
                }).start();
    }

    public void getLog(String time) {
        ApWifiConfigInfo apConfigInfo = new ApWifiConfigInfo.Builder()
                .deviceSN(mDeviceInfo.device_serial)  // 设备序列号，非必要
                .activatePwd("Hik" + mDeviceInfo.device_code)  // 激活密码，萤石接入的海康设备规则为 Hik+验证码。这里不传会使用默认 Hik+验证码
                .verifyCode(mDeviceInfo.device_code)  // 设备验证码，必要
                .withWiFi("1", "1") // wifi ssid 和 密码，必要
                .fixedIP(FIXED_IP.Companion.getWIRELESS_IPC_YS())  // 设备固定IP。这里针对萤石设备有  192.168.8.1 和 192.168.8.253 两款兼容，可扩展更多的ip
                .build();
        LoadingPopupView loadingPopupView = new XPopup.Builder(WJLogQueryActivity.this).asLoading();

        loadingPopupView.show();
        Observable.fromCallable(() -> {
            Response response = APHttpClient.INSTANCE.getApConfigLog(apConfigInfo.getIpPort(), time);
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("日志------------------");
                        stringBuffer.append(response);
                        mTextView.setText(stringBuffer.toString());
                        loadingPopupView.dismiss();
                    }
                });
    }

    public File appendLog(String text) {
        String diskCachePath = getDiskCachePath(this);
        File logFile = new File(diskCachePath, "camera.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.append(text);
            buf.newLine();
            buf.close();
            return logFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
