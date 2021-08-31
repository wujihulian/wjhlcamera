package com.wj.uikit.tx.cover;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.R;
import com.wj.uikit.tx.bs.TXBaseCover;
import com.wj.uikit.tx.bs.TXReceiverEventListener;

/**
 * FileName: TXControlCover
 * Author: xiongxiang
 * Date: 2021/8/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXControlCover extends TXBaseCover {

    private ImageView mWj_player_iv;
    private ImageView mWj_mic_iv;
    private boolean audio = false;//默认静音
    private ImageView mWj_full_iv;
    private int volume = 50;

    public TXControlCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.wj_layout_control_cover, null);
    }
    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        mWj_player_iv = findViewById(R.id.wj_player_iv);
        mWj_mic_iv = findViewById(R.id.wj_mic_iv);
        mWj_full_iv = findViewById(R.id.wj_full_iv);
        mWj_player_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying() == 1) {
                    //正在播放
                    stopPlay();
                } else {
                    //已经停止播放
                    startPlay();
                }
            }
        });
        mWj_mic_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume <= 0) {
                    WJLogUitl.d("打开 音量");
                    setPlayoutVolume(100);
                    mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_open);
                } else {
                    mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_close);
                    setPlayoutVolume(0);
                }
            }
        });

    }

    public ImageView getWj_full_iv() {
        return mWj_full_iv;
    }

    @Override
    public void onVideoPlayStatusUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayStatus status, V2TXLiveDef.V2TXLiveStatusChangeReason reason, Bundle extraInfo) {
        super.onVideoPlayStatusUpdate(player, status, reason, extraInfo);
        switch (status) {
            case V2TXLivePlayStatusStopped:
                mWj_player_iv.setImageResource(R.mipmap.wj_device_start);
                break;
            case V2TXLivePlayStatusLoading:
            case V2TXLivePlayStatusPlaying:
                mWj_player_iv.setImageResource(R.mipmap.wj_device_stop);
                break;
        }
    }

    @Override
    public void onPlayoutVolumeUpdate(V2TXLivePlayer player, int volume) {
        super.onPlayoutVolumeUpdate(player, volume);
        this.volume = volume;
        if (volume <= 0) {
            mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_close);
        } else {
            mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_open);
        }
    }
}
