package com.wj.camera.view.control;

import android.content.Context;
import android.os.Message;

import androidx.annotation.NonNull;

import com.videogo.openapi.EZConstants;
import com.wj.camera.R;

/**
 * FileName: LoadingControl
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class LoadingControl extends WJPlayControl {
    public LoadingControl(@NonNull Context context) {
        super(context);
    }

    @Override
    public int layoutId() {
        return R.layout.wj_loading_control_view;
    }

    @Override
    public void playState(int state, Message data) {
        switch (state) {
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                //播放成功
                getView().setVisibility(GONE);
                break;
            case 10001:
            case EZConstants.MSG_GOT_STREAM_TYPE:
                getView().setVisibility(VISIBLE);
                break;
            default:
        }
    }
}
