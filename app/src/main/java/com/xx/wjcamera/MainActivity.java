package com.xx.wjcamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.wj.camera.net.ApiNew;
import com.wj.camera.net.ISAPI;
import com.wj.camera.net.OkHttpUtils;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.WJCaptureActivity;
import com.wj.uikit.WJDeviceDebugNewActivity;
import com.wj.uikit.WJDeviceManageActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(v -> {


//getHik();
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(WJCaptureActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            Intent intent = new Intent(this, WJDeviceDebugNewActivity.class);
            Bundle extras = new Bundle();
            com.wj.uikit.db.DeviceInfo deviceInfo = new com.wj.uikit.db.DeviceInfo();
//            deviceInfo.device_serial = "F87307358";
//            deviceInfo.device_code = "UOTJUZ";
            deviceInfo.device_serial = "F87307329";
            deviceInfo.device_code = "NTODPF";
            extras.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
            intent.putExtras(extras);


//            integrator.initiateScan();
            startActivity(intent);




        });


    }
}