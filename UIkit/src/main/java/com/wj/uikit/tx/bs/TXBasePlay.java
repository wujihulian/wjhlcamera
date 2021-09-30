package com.wj.uikit.tx.bs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.ap.ezviz.pub.ap.ApWifiConfigInfo;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.impl.V2TXLivePlayerImpl;


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
    private TextureView mTextureView;

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
        if (mContainer != null) {
            mSuperContainer.removeAllViews();
            mContainer.removeView(mSuperContainer);
        }
        this.mContainer = container;
        mTextureView = new TextureView(getContext());
        mSuperContainer.addView(mTextureView);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver txiReceiver) {
                if (txiReceiver instanceof TXBaseCover) {
                    View coverView = ((TXBaseCover) txiReceiver).getCoverView();
                    if (coverView != null) {
                        mSuperContainer.addView(coverView);
                    }
                    txiReceiver.bindTXReceiverGroup(mReceiverGroup);
                    txiReceiver.bindReceiverEventListener(mReceiverEventListener);
                }
            }
        });
        mV2TXLivePlayer.setRenderView(mTextureView);
        container.addView(mSuperContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void startPlay() {
        mReceiverEventListener.startPlay();
    }

    public int startPlay(String playUrl) {
        return mReceiverEventListener.startPlay(playUrl);
    }

    public int stop() {
        return mReceiverEventListener.stopPlay();
    }

    public void destroy() {
        if (mV2TXLivePlayer != null) {
            mV2TXLivePlayer.stopPlay();
            mV2TXLivePlayer.setObserver(null);
            mSuperContainer.removeAllViews();
            mContainer.removeView(mSuperContainer);
            mReceiverGroup.clear();
            mSuperContainer = null;
            mV2TXLivePlayer = null;
            mReceiverGroup=null;
            mContainer=null;
            mTextureView=null;
        }
    }
}
