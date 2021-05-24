package com.wj.camera.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZPlayer;


/**
 * FileName: WJSurfaceView
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class WJSurfaceView extends SurfaceView {
    private SurfaceHolder mSurfaceHolder;
    private EZPlayer mEZPlayer;
    private OnPlayStateListener mPlayStateListener;
    //视频的原始尺寸
    private int mVideoHeight;
    private int mVideoWidth;

    public WJSurfaceView(Context context) {
        super(context);
        init();
    }

    public WJSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(callback);
    }


    public void setSurfaceSize(Ratio ratio) {
        switch (ratio) {
            case Ratio_4_3:
                setSurfaceSize(4, 3);
                break;
            case Ratio_16_9:
                setSurfaceSize(16, 9);
                break;
            case Ratio_match_parent:
                setSurfaceSize(-1, -1);
                break;
            case Ratio_wrap_content:
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = mVideoWidth;
                layoutParams.height = mVideoHeight;
                setLayoutParams(layoutParams);
                break;
        }
    }


    public void setSurfaceSize(int width, int height) {
        ViewGroup parent = (ViewGroup) getParent();
        int measuredHeight = parent.getMeasuredHeight();
        int measuredWidth = parent.getMeasuredWidth();
        int w = measuredWidth / width;
        int h = measuredHeight / height;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (w < h) {
            layoutParams.width = measuredWidth;
            layoutParams.height = w * height;
        } else {
            layoutParams.height = measuredHeight;
            layoutParams.width = h * width;
        }
        if (width <= 0 || height <= 0) {
            layoutParams.height = measuredHeight;
            layoutParams.width = measuredWidth;
        }
        setLayoutParams(layoutParams);
    }

    public void bindEZPlayer(EZPlayer ezPlayer) {
        this.mEZPlayer = ezPlayer;
        mEZPlayer.setHandler(mHandler);
        if (mSurfaceHolder != null) {
            mEZPlayer.setSurfaceHold(mSurfaceHolder);
        }
    }

    public void setPlayStateListener(OnPlayStateListener playStateListener) {
        mPlayStateListener = playStateListener;
    }

    public OnPlayStateListener getPlayStateListener() {
        return mPlayStateListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mPlayStateListener!=null){
                mPlayStateListener.playState(msg.what, msg);
            }
            Log.i(TAG, "handleMessage: state "+msg.what);
            switch (msg.what) {

                case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                    //播放成功
                    break;
                case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                    //播放失败,得到失败信息
                    ErrorInfo errorinfo = (ErrorInfo) msg.obj;
                    //得到播放失败错误码
                    int code = errorinfo.errorCode;
                    //得到播放失败模块错误码
                    String codeStr = errorinfo.moduleCode;
                    //得到播放失败描述
                    String description = errorinfo.description;
                    //得到播放失败解决方方案
                    //   String description = errorinfo.sulution
                    break;
                case EZConstants.MSG_VIDEO_SIZE_CHANGED:
                    //解析出视频画面分辨率回调
                    try {
                        String temp = (String) msg.obj;
                        String[] strings = temp.split(":");
                        mVideoWidth = Integer.parseInt(strings[0]);
                        mVideoHeight = Integer.parseInt(strings[1]);
                        Log.i(TAG, "handleMessage: " + mVideoWidth);
                        Log.i(TAG, "handleMessage: " + mVideoHeight);
                        //解析出视频分辨率
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private static final String TAG = "WJSurfaceView";

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
            if (mEZPlayer != null) {
                mEZPlayer.setSurfaceHold(mSurfaceHolder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;

        Log.i(TAG, "onDetachedFromWindow: ");
    }
}
