package com.wj.uikit.tx;

import android.content.Context;

import com.wj.uikit.tx.bs.TXBasePlay;

import com.wj.uikit.tx.cover.TXControlCover;
import com.wj.uikit.tx.cover.TXGestureCover;
import com.wj.uikit.tx.cover.TXLoadingCover;
import com.wj.uikit.tx.cover.TXReconnectCover;

/**
 * FileName: WJVideoPlayer
 * Author: xiongxiang
 * Date: 2021/8/24
 * Description: ^(?!.*(libc)).*$
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXVideoPlayer extends TXBasePlay {

    private TXReconnectCover mReconnectCover;

    public TXVideoPlayer(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        addReceiver("TXLoadingCover", new TXLoadingCover(getContext()));
        addReceiver("TXControlCover", new TXControlCover(getContext()));
        addReceiver("TXGestureCover", new TXGestureCover(getContext()));
        mReconnectCover = new TXReconnectCover(getContext());
        addReceiver("TXReconnectCover", mReconnectCover);
    }

    public TXReconnectCover getReconnectCover() {
        return mReconnectCover;
    }

}
