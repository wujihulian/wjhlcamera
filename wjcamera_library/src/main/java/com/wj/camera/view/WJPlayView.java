package com.wj.camera.view;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.videogo.openapi.EZPlayer;
import com.wj.camera.R;
import com.wj.camera.view.control.WJPlayControl;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: WJPlayView
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJPlayView extends FrameLayout implements OnPlayStateListener {
    private View mInflate;
    private WJSurfaceView mWjSurfaceView;
    private EZPlayer mEZPlayer;
    private List<WJPlayControl> mControlList = new ArrayList<>();

    public WJPlayView(@NonNull Context context) {
        super(context);
        init();
    }


    public WJPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mInflate = LayoutInflater.from(getContext()).inflate(R.layout.wj_play_view, this);
        mWjSurfaceView = mInflate.findViewById(R.id.wj_surfaceview);
        mWjSurfaceView.setPlayStateListener(this);
    }

    public void setEZPlayer(EZPlayer EZPlayer) {
        mEZPlayer = EZPlayer;
        mWjSurfaceView.bindEZPlayer(mEZPlayer);
    }



    public void setSurfaceHolder(){
        mWjSurfaceView.bindEZPlayer(mEZPlayer);
    }

    public void addControl(WJPlayControl control) {
        control.setEZPlayer(mEZPlayer);
        addView(control);
        mControlList.add(control);
    }


    /**
     * 删除所有控制器
     */
    public void removeAllControl() {
        for (int i = mControlList.size() - 1; i >= 0; i--) {
            removeView(mControlList.get(i));
        }
        mControlList.clear();
    }

    @Override
    public void playState(int state, Message data) {
        for (WJPlayControl wjPlayControl : mControlList) {
            wjPlayControl.playState(state,data);
        }

    }


    public void startRealPlay(){
        if (mEZPlayer!=null){
            playState(10001,null);
            mEZPlayer.startRealPlay();

        }
    }
    public void stopRealPlay(){
        if (mEZPlayer!=null){
            mEZPlayer.stopRealPlay();
        }
    }
    public void release(){
        if (mEZPlayer!=null){
            mEZPlayer.release();
            mEZPlayer=null;
        }
    }

}
