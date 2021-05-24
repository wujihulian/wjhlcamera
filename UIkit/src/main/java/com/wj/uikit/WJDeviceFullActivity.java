package com.wj.uikit;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.videogo.openapi.EZPlayer;
import com.wj.camera.WJCamera;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.camera.view.WJPlayView;
import com.wj.camera.view.control.ErrorControl;
import com.wj.camera.view.control.LoadingControl;
import com.wj.uikit.control.DeviceFullControl;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.status.StatusBarUtil;

/**
 * FileName: DeviceFullActivity
 * Author: xiongxiang
 * Date: 2021/1/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceFullActivity extends BaseUikitActivity {


    private WJPlayView mWj_playview;
    private EZPlayer mPlayer;
    private DeviceInfo mDeviceInfo;
    private  int audio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_device_full);
        Bundle extras = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
        audio=extras.getInt("audio",1);
        initView();
    }

    private void initView() {
        mPlayer= WJCamera.getInstance().createPlayer(mDeviceInfo.device_serial, 1);
        mPlayer.setPlayVerifyCode(mDeviceInfo.device_code);
        mWj_playview = findViewById(R.id.wj_playview);
        mWj_playview.setEZPlayer(mPlayer);
        mWj_playview.addControl(new LoadingControl(this));
        mWj_playview.addControl(new ErrorControl(this));
        mWj_playview.addControl(new DeviceFullControl(this,audio));

        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer!=null){
            mPlayer.startRealPlay();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer!=null){
            mPlayer.stopRealPlay();
        }
    }
    @Override
    public void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer!=null){
            mPlayer.release();
            mPlayer=null;
        }
    }
}
