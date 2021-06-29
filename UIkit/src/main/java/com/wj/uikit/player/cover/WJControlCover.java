package com.wj.uikit.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.receiver.PlayerStateGetter;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.R;
import com.wj.uikit.player.event.WJInterEvent;

/**
 * FileName: WJControlCover
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJControlCover extends BaseCover {

    private ImageView mWj_player_iv;
    private ImageView mWj_mic_iv;
    private boolean audio = false;//默认静音
    private ImageView mWj_full_iv;

    public WJControlCover(Context context) {
        super(context);
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
                PlayerStateGetter playerStateGetter = getPlayerStateGetter();
                if (playerStateGetter.getState() == IPlayer.STATE_PAUSED) {
                    requestResume(null);
                } else if (playerStateGetter.getState() == IPlayer.STATE_IDLE) {
                    notifyReceiverEvent(WJInterEvent.CODE_PLAY, null);
                } else {
                    requestPause(null);
                }
            }
        });
        mWj_mic_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio) {
                    notifyReceiverEvent(WJInterEvent.CODE_SET_VOLUME_0, null);
                    mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_close);
                } else {
                    notifyReceiverEvent(WJInterEvent.CODE_SET_VOLUME_1, null);
                    mWj_mic_iv.setImageResource(R.mipmap.wj_device_mic_open);
                }
                audio = !audio;

            }
        });
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.wj_layout_control_cover, null);
    }

    public ImageView getWj_full_iv() {
        return mWj_full_iv;
    }

    private static final String TAG = "WJControlCover";

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

        WJLogUitl.i("onPlayerEvent: " + eventCode);
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                int status = bundle.getInt(EventKey.INT_DATA);
                if (status == IPlayer.STATE_PAUSED) {
                    mWj_player_iv.setImageResource(R.mipmap.wj_device_start);
                } else if (status == IPlayer.STATE_STARTED) {
                    mWj_player_iv.setImageResource(R.mipmap.wj_device_stop);
                }
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        WJLogUitl.i("onErrorEvent: " + eventCode);
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }
}
