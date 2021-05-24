package com.wj.uikit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZProbeDeviceInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.db.DeviceInfo;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: WJNoQRCodeActivity
 * Author: xiongxiang
 * Date: 2021/3/9
 * Description:找不到验证码
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJNoQRCodeActivity extends BaseUikitActivity {

    private static final String TAG = "WJNoQRCodeActivity";
    private EditText mEt_serial;
    private EditText mEt_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_no_qr_code);
        initView();
    }

    private void initView() {
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEt_serial = findViewById(R.id.et_serial);
        mEt_code = findViewById(R.id.et_code);

        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String serial = mEt_serial.getText().toString().trim();
                String code = mEt_code.getText().toString().trim();
                if (TextUtils.isEmpty(serial)) {
                    Toast.makeText(WJNoQRCodeActivity.this, "序列号空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serial.length()!=9) {
                    Toast.makeText(WJNoQRCodeActivity.this, "请输入正确序列号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(WJNoQRCodeActivity.this, "验证码空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (code.length() != 6) {
                    Toast.makeText(WJNoQRCodeActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.device_serial = serial;
                deviceInfo.device_code = code;
                checkDevice(deviceInfo);
            }
        });

    }

    @SuppressLint("CheckResult")
    public void checkDevice(DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            LoadingPopupView loadingPopupView = new XPopup.Builder(this).asLoading();
            loadingPopupView.show();
            Observable.just(deviceInfo)
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
                            loadingPopupView.dismiss();
                            finish();
                        }
                    }).doOnSubscribe(new RxConsumer(this))
                    .subscribe(new Consumer<EZProbeDeviceInfoResult>() {
                        @Override
                        public void accept(EZProbeDeviceInfoResult result) throws Exception {
                            if (result.getBaseException() == null) {
                                Log.i(TAG, "onCreate: 查询设备成功 添加设备");
                                DeviceApi.getInstance().addDevie(deviceInfo.device_serial, deviceInfo.device_code, new JsonCallback<BaseDeviceResponse>() {
                                    @Override
                                    public void onSuccess(BaseDeviceResponse data) {
                                        loadingPopupView.dismiss();
                                        EventBus.getDefault().post(deviceInfo);
                                        finish();

                                    }

                                    @Override
                                    public void onError(int code, String msg) {
                                        super.onError(code, msg);
                                        loadingPopupView.dismiss();
                                        Toast.makeText(WJNoQRCodeActivity.this, msg + "", Toast.LENGTH_LONG).show();
                                        //finish();
                                    }
                                });
                            } else {
                                loadingPopupView.dismiss();
                                Log.i(TAG, "accept: " + result.getBaseException().getErrorCode());

                                switch (result.getBaseException().getErrorCode()) {
                                    case 120023:
                                        // TODO: 2018/6/25  设备不在线，未被用户添加 （这里需要网络配置）
                                    case 120002:
                                        // TODO: 2018/6/25  设备不存在，未被用户添加 （这里需要网络配置）
                                    case 120029:
                                        // TODO: 2018/6/25  设备不在线，已经被自己添加 (这里需要网络配置)
                                        if (result.getEZProbeDeviceInfo() == null) {
                                            // 未查询到设备信息，不确定设备支持的配网能力,需要用户根据指示灯判断
                                            //若设备指示灯红蓝闪烁，请选择smartconfig配网
                                            //若设备指示灯蓝色闪烁，请选择设备热点配网
                                            Toast.makeText(WJNoQRCodeActivity.this, "该设备不支持配网", Toast.LENGTH_LONG).show();
                                        } else {
                                            EZProbeDeviceInfo probeDeviceInfo = result.getEZProbeDeviceInfo();
                                            Intent intent = new Intent(WJNoQRCodeActivity.this, WJSettingModeActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
                                            bundle.putInt(WJDeviceConfig.SUPPORT_APP_MODE, probeDeviceInfo.getSupportAP());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                        break;
                                    case 120020:
                                        // TODO: 2018/6/25 设备在线，已经被自己添加 (给出提示)
                                        EventBus.getDefault().post(deviceInfo);
                                        break;
                                    case 120022:
                                        // TODO: 2018/6/25  设备在线，已经被别的用户添加 (给出提示)
                                        Toast.makeText(WJNoQRCodeActivity.this, "设备在线，已经被别的用户添加", Toast.LENGTH_LONG).show();
                                        break;
                                    case 120024:
                                        // TODO: 2018/6/25  设备不在线，已经被别的用户添加 (给出提示)
                                        Toast.makeText(WJNoQRCodeActivity.this, "设备不在线，已经被别的用户添加", Toast.LENGTH_LONG).show();
                                        break;
                                    default:

                                        Toast.makeText(WJNoQRCodeActivity.this, "Request failed = "
                                                + result.getBaseException().getErrorCode(), Toast
                                                .LENGTH_LONG).show();
                                        break;
                                }
                                finish();

                            }
                        }
                    });

        } else {
            Toast.makeText(WJNoQRCodeActivity.this, "解析失败 ", Toast.LENGTH_LONG).show();
            finish();
        }

    }

}
