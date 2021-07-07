package com.wj.uikit.uitl;

import android.view.View;

/**
 * FileName: ControlClickListener
 * Author: xiongxiang
 * Date: 2021/6/15
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class OnControlClickListener implements View.OnClickListener {
    private long lastTime = 0;
    private long controlTime = 1000;

    public OnControlClickListener() {

    }
    public OnControlClickListener(long controlTime) {
        this.controlTime = controlTime;
    }

    @Override
    public void onClick(View v) {
        long currTime = System.currentTimeMillis();
        if (currTime - lastTime > controlTime) {
            onViewClick(v);
            lastTime = System.currentTimeMillis();
        }

    }

    public abstract void onViewClick(View view);
}
