package com.wj.uikit.player;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;

import com.wj.uikit.WJDeviceFullNewActivivty;
import com.wj.uikit.player.lifecycle.WJActivityFullLifecycleCallBack;
import com.wj.uikit.player.lifecycle.WJDefaultFullLifecycleCallBack;

/**
 * FileName: WJRelationAssistUtil
 * Author: xiongxiang
 * Date: 2021/6/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJRelationAssistUtil {
    public static WJRelationAssistUtil mWJRelationAssistUtil;
    private static WJVideoPlayer mWJVideoPlayer;
    private static FrameLayout mContainer;
    private static WJActivityFullLifecycleCallBack fullLifecycleCallBack;

    public static WJRelationAssistUtil getInstance() {
        if (mWJRelationAssistUtil == null) {
            return new WJRelationAssistUtil();
        }
        return mWJRelationAssistUtil;
    }

    public WJVideoPlayer getWJVideoPlayer() {
        return mWJVideoPlayer;
    }

    public void setWJVideoPlayer(WJVideoPlayer WJVideoPlayer) {
        mWJVideoPlayer = WJVideoPlayer;
    }

    public void setContainer(FrameLayout container) {
        mContainer = container;
    }

    public FrameLayout getContainer() {
        return mContainer;
    }

    public void switchFull(Context context, WJVideoPlayer wjVideoPlayer, FrameLayout container) {
        this.mWJVideoPlayer = wjVideoPlayer;
        this.mContainer = container;
        context.startActivity(new Intent(context, WJDeviceFullNewActivivty.class));
    }

    public WJActivityFullLifecycleCallBack getFullLifecycleCallBack() {
        if (fullLifecycleCallBack == null) {
            return fullLifecycleCallBack = new WJDefaultFullLifecycleCallBack(mWJVideoPlayer, mContainer);
        }
        return fullLifecycleCallBack;
    }

    public void destroy() {
        mWJVideoPlayer = null;
        mContainer = null;
        fullLifecycleCallBack = null;
    }
}
