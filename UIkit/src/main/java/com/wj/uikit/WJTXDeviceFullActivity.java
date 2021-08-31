package com.wj.uikit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.ISAPI;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.player.event.WJReconnectEventConfig;
import com.wj.uikit.status.StatusBarUtil;
import com.wj.uikit.tx.TXVideoPlayer;
import com.wj.uikit.tx.cover.TXControlCover;

/**
 * FileName: WJTXDeviceFullActivity
 * Author: xiongxiang
 * Date: 2021/8/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJTXDeviceFullActivity extends BaseUikitActivity {

    private FrameLayout mFrameLayout;
    private TXVideoPlayer mTxVideoPlayer;
    private DeviceInfo mDeviceInfo;

    public static void start(Context context, DeviceInfo deviceInfo) {

        Intent intent = new Intent(context, WJTXDeviceFullActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WJDeviceConfig.DEVICE_INFO, deviceInfo);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_tx_device_full);
        initView();
        getDeviceInfo();
        initWebrtc();
    }

    /**
     * 获取设备信息
     */
    private void getDeviceInfo() {
        Bundle extras = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) extras.getSerializable(WJDeviceConfig.DEVICE_INFO);
    }

    private void initWebrtc() {
        FrameLayout frameLayout = findViewById(R.id.fl);
        mTxVideoPlayer = new TXVideoPlayer(this);
        mTxVideoPlayer.getReconnectCover().setDeviceSerial(mDeviceInfo.device_serial);
        mTxVideoPlayer.attachContainer(frameLayout);
        ISAPI.getInstance().getRTMP(mDeviceInfo.device_serial, new JsonCallback<RtmpConfig>() {
            @Override
            public void onSuccess(RtmpConfig data) {
                if (data != null) {
                    RtmpConfig.RTMPDTO rtmp = data.getRTMP();
                    if (rtmp != null) {
                        String url;
                        if ("true".equals(rtmp.getPrivatelyEnabled())) {
                            url = rtmp.getPlayURL2();
                        } else {
                            url = rtmp.getPlayURL1();
                        }
                        url = WJReconnectEventConfig.transformUrl(url);
                        WJLogUitl.d(url);
                        mTxVideoPlayer.startPlay(url);
                    }
                }
            }
        });
        TXControlCover txControlCover = (TXControlCover) mTxVideoPlayer.getReceiver("TXControlCover");
        txControlCover.getWj_full_iv().setVisibility(View.GONE);
    }

    private void initView() {
        mFrameLayout = findViewById(R.id.fl);
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTxVideoPlayer != null) {
            mTxVideoPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTxVideoPlayer != null) {
            mTxVideoPlayer.destroy();
        }
    }
}
