package com.wj.uikit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZProbeDeviceInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.RxConsumer;
import com.wj.camera.net.SafeGuardInterceptor;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.NetworkInterface;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.subscribe.LoadingObserver;
import com.wj.uikit.uitl.OnContinuousClick;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
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
 * FileName: WJCaptureActivity
 * Author: xiongxiang
 * Date: 2021/1/14
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJCaptureActivity extends AppCompatActivity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private static final String TAG = "WJCaptureActivity";
    private TextView mTv_no_qr_code;
    private LoadingPopupView mLoadingPopupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeScannerView = initializeContent();
        capture = new CaptureManager(this, barcodeScannerView) {
            @Override
            protected void returnResult(BarcodeResult rawResult) {
                Intent intent = capture.resultIntent(rawResult, null);
                IntentResult result = IntentIntegrator.parseActivityResult(RESULT_OK, intent);
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(WJCaptureActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                        closeAndFinish();

                    } else {
                        if ("QR_CODE".equals(result.getFormatName())) {
                            //  Toast.makeText(WJCaptureActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                            checkDevice(result.getContents());
                        } else {
                            Toast.makeText(WJCaptureActivity.this, "请扫描正确的二维码", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        };

        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
  /*      mTv_no_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WJCaptureActivity.this, WJNoQRCodeActivity.class));
                finish();
            }
        });*/
        mTv_no_qr_code.setOnClickListener(new OnContinuousClick() {
            @Override
            public void click(View view) {
                startActivity(new Intent(WJCaptureActivity.this, WJNoQRCodeActivity.class));
                finish();
            }
        });
    }

    @SuppressLint("CheckResult")
    public void checkDevice(String content) {
        WJLogUitl.i(  "checkDevice: " + content);
        DeviceInfo deviceInfo = parse(content);
        if (deviceInfo != null) {
            mLoadingPopupView = new XPopup.Builder(this).asLoading();

            mLoadingPopupView.show();
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
                            mLoadingPopupView.dismiss();
                            finish();
                        }
                    }).doOnSubscribe(new RxConsumer(this))
                    .subscribe(new Consumer<EZProbeDeviceInfoResult>() {
                        @Override
                        public void accept(EZProbeDeviceInfoResult result) throws Exception {
                            if (result.getBaseException() == null) {
                                WJLogUitl.i(  "add device");
                                DeviceApi.getInstance().addDevie(deviceInfo.device_serial, deviceInfo.device_code, new JsonCallback<BaseDeviceResponse>() {
                                    @Override
                                    public void onSuccess(BaseDeviceResponse data) {
                                        WJLogUitl.i(  "onSuccess: add device");
                                        mLoadingPopupView.dismiss();
                                        post(deviceInfo);
                                        //EventBus.getDefault().post(deviceInfo);
                                        //finish();

                                    }

                                    @Override
                                    public void onError(int code, String msg) {
                                        super.onError(code, msg);
                                        mLoadingPopupView.dismiss();
                                        WJLogUitl.i(  "onError: "+msg + "-" + code);
                                        Toast.makeText(WJCaptureActivity.this, msg + "-" + code, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                mLoadingPopupView.dismiss();
                                //Toast.makeText(WJCaptureActivity.this, result.getBaseException().getErrorCode()+"", Toast.LENGTH_LONG).show();
                                WJLogUitl.i(  "accept: "+ result.getBaseException().getErrorCode());
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
                                            Toast.makeText(WJCaptureActivity.this, "该设备不支持配网", Toast.LENGTH_LONG).show();
                                            finish();

                                        } else {

                                            EZProbeDeviceInfo probeDeviceInfo = result.getEZProbeDeviceInfo();
                                            Intent intent = new Intent(WJCaptureActivity.this, WJSettingModeActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
                                            bundle.putInt(WJDeviceConfig.SUPPORT_APP_MODE, probeDeviceInfo.getSupportAP());
                                            if (result.getBaseException().getErrorCode() == 120029) {
                                                bundle.putInt(WJDeviceConfig.DEVICE_CODE, 120020);
                                            }
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            finish();

                                        }
                                        break;
                                    case 120020:
                                        // TODO: 2018/6/25 设备在线，已经被自己添加 (给出提示)
                                        // EventBus.getDefault().post(deviceInfo);
                                        checkIsApi(deviceInfo, 0);
                                        break;
                                    case 120022:
                                        // TODO: 2018/6/25  设备在线，已经被别的用户添加 (给出提示)
                                        Toast.makeText(WJCaptureActivity.this, "设备在线，已经被别的用户添加", Toast.LENGTH_LONG).show();
                                        finish();

                                        break;
                                    case 120024:
                                        // TODO: 2018/6/25  设备不在线，已经被别的用户添加 (给出提示)
                                        Toast.makeText(WJCaptureActivity.this, "设备不在线，已经被别的用户添加", Toast.LENGTH_LONG).show();
                                        finish();

                                        break;
                                    default:
                                        WJLogUitl.i(  "accept: " + result.getBaseException().getErrorCode());
                             /*           Toast.makeText(WJCaptureActivity.this, "Request failed = "
                                                + result.getBaseException().getErrorCode() + " msg=" + result.getBaseException().getErrorInfo().description, Toast
                                                .LENGTH_LONG).show();*/
                                        Toast.makeText(WJCaptureActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                                        finish();

                                        break;
                                }

                            }
                        }
                    });

        } else {
            Toast.makeText(WJCaptureActivity.this, "解析失败 ", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void checkIsApi(DeviceInfo deviceInfo, int code) {
        Observable.just(deviceInfo)
                .map(new Function<DeviceInfo, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull DeviceInfo deviceInfo) throws Exception {
                        boolean isApi = false;
                        for (int i = 0; i < 2; i++) {
                            RtmpConfig rtmp = ISAPI.getInstance().getRTMP(deviceInfo.device_serial);
                            if (rtmp == null || rtmp.getRTMP() == null) {
                            } else {
                                isApi = true;
                                break;
                            }
                        }
                        return isApi;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new RxConsumer(this))
                .subscribe(new LoadingObserver<Boolean>(this) {
                    @Override
                    public void dispose() {
                        super.dispose();
                        WJLogUitl.i(  "dispose: ");
                        //capture.onResume();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        super.onError(e);
                        WJLogUitl.i(  "onError: "+e.getMessage());
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Boolean o) {
                        super.onNext(o);
                        WJLogUitl.i(  "onNext: ");

                        if (o == true) {
                            post(deviceInfo);
                            /*EventBus.getDefault().post(deviceInfo);
                            finish();*/
                        } else {
                            Intent intent = new Intent(WJCaptureActivity.this, WJSettingModeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
                            bundle.putInt(WJDeviceConfig.DEVICE_CODE, 120020);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
    }


    protected DeviceInfo parse(String content) {
        WJLogUitl.i(  "parse: " + content);
        byte[] byteArray = content.getBytes();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            try {
                String res = new String(new byte[]{byteArray[i]}, "UTF-8");
                WJLogUitl.i(  "parse: " + res);
                if (byteArray[i] == 13) {
                    stringBuffer.append("&");
                } else {
                    stringBuffer.append(res);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String[] split = stringBuffer.toString().split("&");
        WJLogUitl.i(  "parse: " + new Gson().toJson(split));
        if (split != null && split.length >= 4) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.device_factory = split[0];
            deviceInfo.device_serial = split[1];
            deviceInfo.device_code = split[2];
            deviceInfo.device_type = split[3];
            String[] arr2 = deviceInfo.device_type.split(" ");
            deviceInfo.device_type = arr2[0];
            WJLogUitl.i(  "parse: " + new Gson().toJson(deviceInfo));
            return deviceInfo;
        }
        Toast.makeText(WJCaptureActivity.this, "parse: 解析失败", Toast.LENGTH_LONG).show();
        return null;
    }

    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.wj_activity_zxing_capture_);
        DecoratedBarcodeView decoratedBarcodeView = findViewById(R.id.zxing_barcode_scanner);
        mTv_no_qr_code = findViewById(R.id.tv_no_qr_code);
        decoratedBarcodeView.setStatusText("");
        return decoratedBarcodeView;
    }
    public void post(DeviceInfo deviceInfo){
        ISAPI.getInstance().getNetworkInterface(deviceInfo.device_serial, new JsonCallback<NetworkInterface>() {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                EventBus.getDefault().post(deviceInfo);
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
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
