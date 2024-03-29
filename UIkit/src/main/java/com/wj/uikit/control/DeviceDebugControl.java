package com.wj.uikit.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.videogo.openapi.EZConstants;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.camera.view.control.WJPlayControl;
import com.wj.uikit.R;
import com.wj.uikit.WJDeviceFullActivity;
import com.wj.uikit.db.DeviceInfo;

/**
 * FileName: DeviceDebugControl
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class DeviceDebugControl extends WJPlayControl {
    private ImageView mPlayer_iv;
    private ImageView mMic_iv;
    private ImageView mFull_iv;
    private int video = 1;
    private int audio = 1;
    private DeviceInfo mDeviceInfo;
    private  boolean init =false;
    public DeviceDebugControl(@NonNull Context context, DeviceInfo deviceInfo) {
        super(context);
        mDeviceInfo = deviceInfo;
    }


    @Override
    public int layoutId() {
        return R.layout.wj_device_debug_control_view;
    }

    @Override
    public void playState(int state, Message data) {
        switch (state) {
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                //播放成功:
                mPlayer_iv.setImageResource(R.mipmap.wj_device_stop);
                video = 1;
                if (init==false){
                    init=true;
                    if (getEZPlayer()!=null && audio==1) {
                        getEZPlayer().openSound();
                    }
                }
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_STOP_SUCCESS:
                mPlayer_iv.setImageResource(R.mipmap.wj_device_start);
                video = 0;
                break;
        }
    }

    private static final String TAG = "DeviceDebugControl";

    @Override
    public void onCreate() {
        mPlayer_iv = findViewById(R.id.player_iv);
        mMic_iv = findViewById(R.id.mic_iv);
        mFull_iv = findViewById(R.id.full_iv);
        mPlayer_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WJLogUitl.i(  "onClick: " + video);
                if (video == 1) {
                    mPlayer_iv.setImageResource(R.mipmap.wj_device_start);
                    video = 0;
                    getEZPlayer().stopRealPlay();
                } else {
                    getEZPlayer().startRealPlay();
                    mPlayer_iv.setImageResource(R.mipmap.wj_device_stop);
                    video = 1;
                }

            }
        });

        mMic_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio == 1) {
                    getEZPlayer().closeSound();
                    audio = 0;
                    mMic_iv.setImageResource(R.mipmap.wj_device_mic_close);
                } else {
                    getEZPlayer().openSound();
                    audio = 1;
                    mMic_iv.setImageResource(R.mipmap.wj_device_mic_open);
                }
            }
        });

        mFull_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WJDeviceFullActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(WJDeviceConfig.DEVICE_INFO, mDeviceInfo);
                extras.putInt("audio",audio);
                intent.putExtras(extras);
                getContext().startActivity(intent);
            }
        });
    }


}
