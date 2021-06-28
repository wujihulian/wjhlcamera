package com.wj.uikit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.wj.uikit.player.WJRelationAssistUtil;
import com.wj.uikit.player.WJVideoPlayer;
import com.wj.uikit.status.StatusBarUtil;

/**
 * FileName: WJDeviceFullNewActivivty
 * Author: xiongxiang
 * Date: 2021/6/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceFullNewActivivty extends BaseUikitActivity {

    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameLayout = new FrameLayout(this);
        setContentView(mFrameLayout);
        WJVideoPlayer wjVideoPlayer = WJRelationAssistUtil.getInstance().getWJVideoPlayer();
        wjVideoPlayer.getWjControlCover().getWj_full_iv().setVisibility(View.GONE);
        WJRelationAssistUtil.getInstance().getWJVideoPlayer().attachContainer(mFrameLayout);
    }

    @Override
    public void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
    }
    @Override
    protected void onPause() {
        super.onPause();
        WJRelationAssistUtil.getInstance().getWJVideoPlayer().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WJRelationAssistUtil instance = WJRelationAssistUtil.getInstance();
        instance.getWJVideoPlayer().getWjControlCover().getWj_full_iv().setVisibility(View.VISIBLE);
        FrameLayout container = instance.getContainer();
        instance.getWJVideoPlayer().attachContainer(container);
    }
}
