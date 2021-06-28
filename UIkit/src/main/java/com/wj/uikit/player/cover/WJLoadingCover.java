package com.wj.uikit.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.receiver.PlayerStateGetter;
import com.wj.uikit.R;

/**
 * FileName: WJLoadingCover
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJLoadingCover extends BaseCover {
    public WJLoadingCover(Context context) {
        super(context);
    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        if(playerStateGetter!=null && isInPlaybackState(playerStateGetter)){
            setLoadingState(playerStateGetter.isBuffering());
        }
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.wj_layout_loading_cover, null);
    }
    private boolean isInPlaybackState(PlayerStateGetter playerStateGetter){
        int state = playerStateGetter.getState();
        return state!= IPlayer.STATE_END
                && state!= IPlayer.STATE_ERROR
                && state!= IPlayer.STATE_IDLE
                && state!= IPlayer.STATE_INITIALIZED
                && state!= IPlayer.STATE_STOPPED;
    }


    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO:
                setLoadingState(true);
                break;

            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
            case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                setLoadingState(false);
                break;
        }
    }
    private void setLoadingState(boolean show){
        setCoverVisibility(show?View.VISIBLE:View.GONE);
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
            setLoadingState(false);
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public int getCoverLevel() {
        return levelMedium(1);
    }
}
