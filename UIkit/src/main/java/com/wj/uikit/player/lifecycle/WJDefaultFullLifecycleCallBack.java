package com.wj.uikit.player.lifecycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wj.uikit.R;
import com.wj.uikit.player.WJRelationAssistUtil;
import com.wj.uikit.player.WJVideoPlayer;

/**
 * FileName: WJDefaultFullLifecycleCallBack
 * Author: xiongxiang
 * Date: 2021/6/29
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDefaultFullLifecycleCallBack implements WJActivityFullLifecycleCallBack {
    private WJVideoPlayer mWJVideoPlayer;
    private FrameLayout mContainer;

    public WJDefaultFullLifecycleCallBack(WJVideoPlayer wjVideoPlayer, FrameLayout container) {
        this.mWJVideoPlayer = wjVideoPlayer;
        this.mContainer = container;
    }

    @Override
    public void onCrete(@Nullable Bundle savedInstanceState, FrameLayout frameLayout, AppCompatActivity context) {
        if (mWJVideoPlayer == null) {
            return;
        }
        if (mWJVideoPlayer.getWjControlCover() != null) {
            mWJVideoPlayer.getWjControlCover().getWj_full_iv().setVisibility(View.GONE);
        }
        WJRelationAssistUtil.getInstance().getWJVideoPlayer().attachContainer(frameLayout);

        View backView = LayoutInflater.from(frameLayout.getContext()).inflate(R.layout.wj_default_full_back, null);
        frameLayout.addView(backView);
        backView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {
        if (mWJVideoPlayer == null) {
            return;
        }
        mWJVideoPlayer.pause();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        if (mWJVideoPlayer == null) {
            return;
        }
        if (mWJVideoPlayer.getWjControlCover() == null) {
            return;
        }
        mWJVideoPlayer.getWjControlCover().getWj_full_iv().setVisibility(View.VISIBLE);
        mWJVideoPlayer.attachContainer(mContainer);
        WJRelationAssistUtil.getInstance().destroy();
    }

    @Override
    public void onStop() {

    }
}
