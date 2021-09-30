package com.wj.uikit.tx.cover;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ap.ezviz.pub.utils.LogUtil;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.R;
import com.wj.uikit.tx.bs.TXBaseCover;

/**
 * FileName: TXLoadingCover
 * Author: xiongxiang
 * Date: 2021/8/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXLoadingCover extends TXBaseCover {

    private View mView;

    public TXLoadingCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.wj_layout_loading_cover, null);
    }


    @Override
    public void onReceiverBind() {
        mView = getCoverView().findViewById(R.id.room);
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideoPlayStatusUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayStatus status, V2TXLiveDef.V2TXLiveStatusChangeReason reason, Bundle extraInfo) {
        super.onVideoPlayStatusUpdate(player, status, reason, extraInfo);
        WJLogUitl.d("onVideoPlayStatusUpdate" +fps);
        switch (status) {
            case V2TXLivePlayStatusLoading:
                mView.setVisibility(View.VISIBLE);
                break;
            case V2TXLivePlayStatusPlaying:
                mView.setVisibility(View.GONE);
                break;
            case V2TXLivePlayStatusStopped:
                mView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
        super.onError(player, code, msg, extraInfo);
        mView.setVisibility(View.VISIBLE);
    }

    private int fps;
    private static final String TAG = "TXLoadingCover";

    @Override
    public void onStatisticsUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayerStatistics statistics) {
        super.onStatisticsUpdate(player, statistics);
        fps = statistics.fps;
       WJLogUitl.d("onStatisticsUpdate" +fps);
    }

}
