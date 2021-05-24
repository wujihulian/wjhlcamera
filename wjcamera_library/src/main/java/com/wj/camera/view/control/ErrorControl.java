package com.wj.camera.view.control;

import android.content.Context;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.openapi.EZConstants;
import com.wj.camera.R;

/**
 * FileName: ErrorControl
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class ErrorControl extends WJPlayControl {

    private TextView mWj_error_tv;

    public ErrorControl(@NonNull Context context) {
        super(context);
    }

    @Override
    public int layoutId() {
        return R.layout.wj_error_control_view;
    }

    @Override
    public void playState(int state, Message data) {
        switch (state) {

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:

                ErrorInfo errorinfo = (ErrorInfo) data.obj;
                //得到播放失败错误码
                int code = errorinfo.errorCode;
                //得到播放失败模块错误码
                String codeStr = errorinfo.moduleCode;
                //得到播放失败描述
                String description = errorinfo.description;
                String sulution = errorinfo.sulution;
                //  mWj_error_tv.setText(code +   "-" + description);
                mWj_error_tv.setText("连接设备失败");
                setVisibility(VISIBLE);
                break;


            default:
                setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWj_error_tv = findViewById(R.id.wj_error_tv);
    }
}
