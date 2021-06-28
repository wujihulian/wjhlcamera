package com.wj.uikit.player;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.wj.uikit.player.cover.WJControlCover;
import com.wj.uikit.player.cover.WJLoadingCover;
import com.wj.uikit.player.event.WJReconnectEvent;
import com.wj.uikit.player.handler.WJOnAssistPlayEventHandler;

/**
 * FileName: WJVideoPlayer
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJVideoPlayer extends WJBasePlayer {
    public WJVideoPlayer(Context context) {
        super(context);
    }

    private WJReconnectEvent mWJReconnectEvent;



    @Override
    public void init() {
        mAssist.getSuperContainer().setBackgroundColor(Color.BLACK);
        mAssist.setEventAssistHandler(new WJOnAssistPlayEventHandler());
        ReceiverGroup receiverGroup = new ReceiverGroup();
        receiverGroup.addReceiver("WJLoadingCover", new WJLoadingCover(getContext()));
        receiverGroup.addReceiver("WJControlCover", new WJControlCover(getContext()));
        mAssist.setVolume(0, 0);
        mAssist.setAspectRatio(AspectRatio.AspectRatio_MATCH_PARENT);
        mAssist.setReceiverGroup(receiverGroup);
    }


    public void registerReconnect(WJReconnectEvent wjOnReconnectEvent) {
        wjOnReconnectEvent.setAssistPlay(mAssist);
        this.mWJReconnectEvent = wjOnReconnectEvent;

        mAssist.setOnErrorEventListener(new OnErrorEventListener() {
            @Override
            public void onErrorEvent(int eventCode, Bundle bundle) {
                if (mWJReconnectEvent!=null){
                    mWJReconnectEvent.onErrorEvent(eventCode,bundle);
                }
            }
        });
        mAssist.setOnPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onPlayerEvent(int eventCode, Bundle bundle) {
                if (mWJReconnectEvent!=null){
                    mWJReconnectEvent.onPlayerEvent(eventCode,bundle);
                }
            }
        });
    }

    public void setData(String url) {
        DataSource dataSource = new DataSource(url);
        dataSource.setData(url);
        setDataSource(dataSource);
    }

    public WJControlCover getWjControlCover() {
        return mAssist.getReceiverGroup().getReceiver("WJControlCover");
    }

    @Override
    public void destroy() {
        super.destroy();

    }
}
