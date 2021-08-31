package com.wj.uikit.tx.cover;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.live2.V2TXLivePlayer;
import com.wj.camera.uitl.WJLogUitl;
import com.wj.uikit.R;
import com.wj.uikit.tx.bs.TXBaseCover;
import com.wj.uikit.tx.bs.TXOnGestureListener;
import com.wj.uikit.view.VolumeProgressView;

/**
 * FileName: TXGestureCover
 * Author: xiongxiang
 * Date: 2021/8/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXGestureCover extends TXBaseCover implements TXOnGestureListener {
    ImageView mVolumeIcon;
    TextView mVolumeText;
    View mVolumeBox;
    private VolumeProgressView mVp;

    public TXGestureCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.wj_layout_gesture_cover, null);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        getCoverView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = getCoverView().getWidth();
                mHeight = getCoverView().getHeight();
                getCoverView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mVolumeBox = findViewById(R.id.wj_cover_player_gesture_operation_volume_box);
        mVolumeIcon = findViewById(R.id.wj_cover_player_gesture_operation_volume_icon);
        mVolumeText = findViewById(R.id.wj_cover_player_gesture_operation_volume_text);
        mVp = findViewById(R.id.vp);
    }

    @Override
    public void onPlayoutVolumeUpdate(V2TXLivePlayer player, int volume) {
        super.onPlayoutVolumeUpdate(player, volume);

    }

    private boolean isGesture = false;
    private boolean mHorizontalSlide;
    private boolean firstTouch;
    private boolean horizontalSlide;
    private int mMaxVolume = 100;
    private int volume = 0;
    private int mWidth, mHeight;

    @Override
    public boolean onDown(MotionEvent e) {
        mHorizontalSlide = false;
        firstTouch = true;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        WJLogUitl.d(distanceX + "   " + distanceY);
        float mOldX = e1.getX(), mOldY = e1.getY();
        float deltaY = mOldY - e2.getY();
        float deltaX = mOldX - e2.getX();
        if (firstTouch) {
            horizontalSlide = Math.abs(distanceX) >= Math.abs(distanceY);
            firstTouch = false;
        }


        if (horizontalSlide) {
            // onHorizontalSlide(-deltaX / mWidth);
        } else {
            onVerticalSlideNew(distanceY/2);
    /*
            if (Math.abs(deltaY) > mHeight)
                return false;
            onVerticalSlide(deltaY/4000);*/
        }
        return true;
    }

    private void onVerticalSlideNew(float distanceY) {
        int index = (int) (distanceY + volume);

        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        int i = (int) index;
        String s = i + "%";
        if (i == 0) {
            s = "OFF";
        }
        volume = i;
        setPlayoutVolume(i);
        setVolumeBoxState(true);
        setVolumeText(s);
        mVp.setProgress(i);
    }

    public void onVerticalSlide(float percent) {
        mHorizontalSlide = false;
        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "OFF";
        }
        volume = i;
        setPlayoutVolume(i);
        setVolumeBoxState(true);
        setVolumeText(s);
        mVp.setProgress(i);

    }

    public void setVolumeText(String text) {
        if (mVolumeText != null) {
            mVolumeText.setText(text);
        }
    }

    public void setVolumeBoxState(boolean state) {
        if (mVolumeBox != null) {
            mVolumeBox.setVisibility(state ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public void onEndGesture(MotionEvent e) {
        setVolumeBoxState(false);
    }

}
