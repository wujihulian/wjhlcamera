package com.wj.uikit.tx.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.R;
import com.wj.uikit.tx.bs.TXBaseCover;
import com.wj.uikit.tx.bs.TXIReceiver;
import com.wj.uikit.tx.bs.TXIReceiverGroup;
import com.wj.uikit.tx.bs.TXReceiverGroup;

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
    private int volume = 100;

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
                    mWj_player_iv.setImageResource(R.mipmap.wj_device_start);
                } else {
                    //已经停止播放
                    startPlay();
                    mWj_player_iv.setImageResource(R.mipmap.wj_device_stop);

                }
            }
        });
        mWj_mic_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume <= 0) {
                    WJLogUitl.d("打开 音量");
                    setVolume(100);
                    mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_open);
                } else {
                    mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_close);
                    setVolume(0);
                }
            }
        });

    }

    public void setVolume(int volume) {
        setPlayoutVolume(volume);
        TXReceiverGroup txReceiverGroup = getTXReceiverGroup();
        txReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                Bundle bundle = new Bundle();
                bundle.putInt("volume", volume);
                receiver.event(bundle);
            }
        });
    }

    public ImageView getWj_full_iv() {
        return mWj_full_iv;
    }

    @Override
    public void onVideoPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
        super.onVideoPlaying(player, firstPlay, extraInfo);
        mWj_player_iv.setImageResource(R.mipmap.wj_device_stop);

    }

    @Override
    public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
        super.onError(player, code, msg, extraInfo);
        mWj_player_iv.setImageResource(R.mipmap.wj_device_start);
    }

    @Override
    public void onVideoLoading(V2TXLivePlayer player, Bundle extraInfo) {
        super.onVideoLoading(player, extraInfo);
        mWj_player_iv.setImageResource(R.mipmap.wj_device_start);
    }

/*    @Override
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
    }*/



    @Override
    public void event(Bundle build) {
        super.event(build);
        int volume = build.getInt("volume", -1);
        if (volume != -1) {
            this.volume = volume;
            if (this.volume <= 0) {
                mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_close);
            } else {
                mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_open);
            }
        }

    }
}
