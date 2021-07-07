package com.wj.uikit.player;

import android.content.Context;
import android.view.ViewGroup;

import com.kk.taurus.playerbase.assist.AssistPlay;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.render.AspectRatio;


/**
 * FileName: WJPlayer
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class WJBasePlayer implements AssistPlay {
    private Context mContext;
    WJRelationAssist mAssist;

    public WJBasePlayer(Context context) {
        this.mContext = context;
        mAssist = new WJRelationAssist(mContext);
    }

    public abstract void init();

    public Context getContext() {
        return mContext;
    }

    public WJRelationAssist getAssist() {
        return mAssist;
    }

    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        mAssist.setOnPlayerEventListener(onPlayerEventListener);
    }

    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        mAssist.setOnErrorEventListener(onErrorEventListener);
    }

    @Override
    public void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        mAssist.setOnReceiverEventListener(onReceiverEventListener);
    }

    @Override
    public void setOnProviderListener(IDataProvider.OnProviderListener onProviderListener) {
        mAssist.setOnProviderListener(onProviderListener);
    }

    @Override
    public void setDataProvider(IDataProvider dataProvider) {
        mAssist.setDataProvider(dataProvider);
    }

    @Override
    public boolean switchDecoder(int decoderPlanId) {
        return mAssist.switchDecoder(decoderPlanId);
    }

    @Override
    public void setRenderType(int renderType) {
        mAssist.setRenderType(renderType);
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        mAssist.setAspectRatio(aspectRatio);
    }

    @Override
    public void setVolume(float left, float right) {
        mAssist.setVolume(left, right);
    }

    @Override
    public void setSpeed(float speed) {
        mAssist.setSpeed(speed);
    }

    @Override
    public void setLooping(boolean looping) {
        mAssist.setLooping(looping);
    }

    @Override
    public void setReceiverGroup(IReceiverGroup receiverGroup) {
        mAssist.setReceiverGroup(receiverGroup);
    }

    @Override
    public void attachContainer(ViewGroup userContainer) {
        mAssist.attachContainer(userContainer);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        mAssist.setDataSource(dataSource);
    }

    @Override
    public void play() {
        mAssist.play();
    }

    @Override
    public void play(boolean updateRender) {
        mAssist.play(updateRender);
    }

    @Override
    public boolean isInPlaybackState() {
        return mAssist.isInPlaybackState();
    }

    @Override
    public boolean isPlaying() {
        return mAssist.isPlaying();
    }

    @Override
    public int getCurrentPosition() {
        return mAssist.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mAssist.getDuration();
    }

    @Override
    public int getAudioSessionId() {
        return mAssist.getAudioSessionId();
    }

    @Override
    public int getBufferPercentage() {
        return mAssist.getBufferPercentage();
    }

    @Override
    public int getState() {
        return mAssist.getState();
    }

    @Override
    public void rePlay(int msc) {
        mAssist.rePlay(msc);
    }

    @Override
    public void pause() {
        mAssist.pause();
    }

    @Override
    public void resume() {
        mAssist.resume();
    }

    @Override
    public void seekTo(int msc) {
        mAssist.seekTo(msc);
    }

    @Override
    public void stop() {
        mAssist.stop();
    }

    @Override
    public void reset() {
        mAssist.reset();
    }

    @Override
    public void destroy() {
        mAssist.destroy();
        mAssist=null;
    }
}
