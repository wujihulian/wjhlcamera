package com.wj.uikit.tx.bs;

import android.text.TextUtils;

import com.tencent.live2.impl.V2TXLivePlayerImpl;

/**
 * FileName: TXReceiverEventListenerImpl
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXReceiverEventListenerImpl implements TXReceiverEventListener {
    public V2TXLivePlayerImpl mV2TXLivePlayer;
    private String playUrl;

    public TXReceiverEventListenerImpl(V2TXLivePlayerImpl v2TXLivePlayer) {
        mV2TXLivePlayer = v2TXLivePlayer;
    }

    @Override
    public int startPlay() {
        if (!TextUtils.isEmpty(playUrl)) {
            return mV2TXLivePlayer.startPlay(playUrl);
        }
        return 0;
    }

    @Override
    public int startPlay(String var1) {
        this.playUrl = var1;
        return mV2TXLivePlayer.startPlay(var1);
    }

    @Override
    public int stopPlay() {
        return mV2TXLivePlayer.stopPlay();
    }

    @Override
    public int isPlaying() {
        return mV2TXLivePlayer.isPlaying();
    }

    @Override
    public int pauseAudio() {
        return mV2TXLivePlayer.pauseAudio();
    }

    @Override
    public int resumeAudio() {
        return mV2TXLivePlayer.resumeAudio();
    }

    @Override
    public int pauseVideo() {
        return mV2TXLivePlayer.pauseVideo();
    }

    @Override
    public int resumeVideo() {
        return mV2TXLivePlayer.resumeVideo();
    }


    @Override
    public int setPlayoutVolume(int var1) {
        return mV2TXLivePlayer.setPlayoutVolume(var1);
    }
}
