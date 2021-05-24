package com.wj.camera.view.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.videogo.openapi.EZPlayer;
import com.wj.camera.view.OnPlayStateListener;

/**
 * FileName: DeviceDebugControl
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class WJPlayControl extends FrameLayout implements OnPlayStateListener {

    private View mView;
    public EZPlayer mEZPlayer;
    public abstract int layoutId();

    public void setEZPlayer(EZPlayer EZPlayer) {
        mEZPlayer = EZPlayer;
    }

    public EZPlayer getEZPlayer() {
        return mEZPlayer;
    }

    public void onCreate(){

    }

    public WJPlayControl(@NonNull Context context) {
        super(context);
        initView();
    }

    protected void initView() {
        mView = LayoutInflater.from(getContext()).inflate(layoutId(), null);
        addView(mView);
        onCreate();
    }



    public View getView() {
        return mView;
    }
}
