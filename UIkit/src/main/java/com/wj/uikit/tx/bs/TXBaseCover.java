package com.wj.uikit.tx.bs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;

/**
 * FileName: TXBaseCover
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class TXBaseCover extends TXSingleLivePlayerObserver implements TXIReceiver,TXReceiverEventListener {
    private View mCoverView;
    private TXReceiverEventListener mEvent;

    public TXBaseCover(Context context) {
        mCoverView = onCreateCoverView(context);
        onReceiverBind();
    }

    protected abstract View onCreateCoverView(Context context);

    @Override
    public void bindReceiverEventListener(TXReceiverEventListener event) {
        this.mEvent = event;
    }

    public View getCoverView() {
        return mCoverView;
    }

    public TXReceiverEventListener getEvent() {
        return mEvent;
    }

    @Override
    public void onReceiverBind() {

    }
    protected final <T extends View> T findViewById(int id){
        return mCoverView.findViewById(id);
    }

    @Override
    public int startPlay() {
        return mEvent.startPlay();
    }

    @Override
    public int startPlay(String var1) {
        return mEvent.startPlay(var1);
    }

    @Override
    public int stopPlay() {
        return mEvent.stopPlay();
    }

    @Override
    public int isPlaying() {
        return mEvent.isPlaying();
    }

    @Override
    public int pauseAudio() {
        return mEvent.pauseAudio();
    }

    @Override
    public int resumeAudio() {
        return mEvent.resumeAudio();
    }

    @Override
    public int pauseVideo() {
        return mEvent.pauseVideo();
    }

    @Override
    public int resumeVideo() {
        return mEvent.resumeVideo();
    }

    @Override
    public int setPlayoutVolume(int var1) {
        return mEvent.setPlayoutVolume(var1);
    }
}
