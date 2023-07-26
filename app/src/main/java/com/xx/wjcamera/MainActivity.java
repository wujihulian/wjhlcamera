package com.xx.wjcamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.WJDeviceDebugNewActivity;
import com.wj.uikit.WJDeviceManageActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(v -> {
            Intent intent = new Intent(this, WJDeviceDebugNewActivity.class);
            Bundle extras = new Bundle();
            com.wj.uikit.db.DeviceInfo deviceInfo = new com.wj.uikit.db.DeviceInfo();
            deviceInfo.device_serial = "F87307358";
            deviceInfo.device_code = "UOTJUZ";
            extras.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
            intent.putExtras(extras);
            startActivity(intent);
        });


    }
}