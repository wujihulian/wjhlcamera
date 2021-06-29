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
        WJRelationAssistUtil.getInstance().getFullLifecycleCallBack().onCrete(savedInstanceState,mFrameLayout,this);
    }

    @Override
    public void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WJRelationAssistUtil.getInstance().getFullLifecycleCallBack().onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        WJRelationAssistUtil.getInstance().getFullLifecycleCallBack().onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        WJRelationAssistUtil.getInstance().getFullLifecycleCallBack().onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WJRelationAssistUtil.getInstance().getFullLifecycleCallBack().onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WJRelationAssistUtil.getInstance().getFullLifecycleCallBack().onDestroy();

    }
}
