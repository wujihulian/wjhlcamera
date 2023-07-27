package com.xx.wjcamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.net.ISAPI;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.WJDeviceDebugNewActivity;
import com.wj.uikit.WJDeviceManageActivity;
import com.wj.uikit.player.event.WJReconnectEventConfig;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TXCloudVideoView cloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        //创建 player 对象
        V2TXLivePlayer mLivePlayer = new V2TXLivePlayerImpl(this);
//关键 player 对象与界面 view
        mLivePlayer.setRenderView(cloudVideoView);

        findViewById(R.id.tv).setOnClickListener(v -> {
//            mLivePlayer.startPlay("https://liveplay.wxbig.cn/xxt/I_F87307358.flv?auth_key=1690509723-0-0-286de7d8d6c194b98139847941fac533&txTime=64c3219b");

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