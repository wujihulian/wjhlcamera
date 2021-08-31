package com.wj.uikit.tx.bs;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.wj.camera.uitl.WJLogUitl;


/**
 * FileName: TXBasePlay
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class TXBasePlay {
    private Context mContext;
    private V2TXLivePlayerImpl mV2TXLivePlayer;
    private ViewGroup mContainer;
    public TXReceiverGroup mReceiverGroup;


    private TXReceiverEventListener mReceiverEventListener;
    private TXSuperContainer mSuperContainer;

    public TXBasePlay(Context context) {
        this.mContext = context;
        init();
    }

    public Context getContext() {
        return mContext;
    }


    public V2TXLivePlayerImpl getV2TXLivePlayer() {
        return mV2TXLivePlayer;
    }

    public abstract void onCreate();

    public void init() {
        mSuperContainer = new TXSuperContainer(mContext);
        mV2TXLivePlayer = new V2TXLivePlayerImpl(mContext);
        mV2TXLivePlayer.enableVolumeEvaluation(300);
        mV2TXLivePlayer.setRenderFillMode(V2TXLiveDef.V2TXLiveFillMode.V2TXLiveFillModeFill);
        mReceiverEventListener = new TXReceiverEventListenerImpl(mV2TXLivePlayer);
        mReceiverGroup = new TXReceiverGroup();
        mSuperContainer.setReceiverGroup(mReceiverGroup);
        mV2TXLivePlayer.setObserver(new V2TXLivePlayerObserverDispatcher(mReceiverGroup));
        onCreate();
    }

    public TXReceiverGroup getReceiverGroup() {
        return mReceiverGroup;
    }

    public void addReceiver(String key, TXIReceiver receiver) {
        mReceiverGroup.addReceiver(key, receiver);
    }

    public TXIReceiver getReceiver(String key) {
        return mReceiverGroup.getTXIReceiver(key);
    }

    public TXReceiverEventListener getReceiverEventListener() {
        return mReceiverEventListener;
    }

    public void attachContainer(ViewGroup container) {
        if (mContainer!=null){
            mSuperContainer.removeAllViews();
            mContainer.removeView(mSuperContainer);
        }
        this.mContainer = container;
        TextureView textureView = new TextureView(getContext());
        mSuperContainer.addView(textureView);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver txiReceiver) {
                if (txiReceiver instanceof TXBaseCover) {
                    View coverView = ((TXBaseCover) txiReceiver).getCoverView();
                    if (coverView != null) {
                        mSuperContainer.addView(coverView);
                    }
                    txiReceiver.bindReceiverEventListener(mReceiverEventListener);
                }
            }
        });
        mV2TXLivePlayer.setRenderView(textureView);
        container.addView(mSuperContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void startPlay() {
        mReceiverEventListener.startPlay();
    }

    public void startPlay(String playUrl) {
        mReceiverEventListener.startPlay(playUrl);
    }

    public void stop() {
        mReceiverEventListener.stopPlay();
    }

    public void destroy() {
        if (mV2TXLivePlayer != null) {
            mV2TXLivePlayer.stopPlay();
            mSuperContainer.removeAllViews();
            mContainer.removeView(mSuperContainer);
            mV2TXLivePlayer = null;
        }
    }





}
