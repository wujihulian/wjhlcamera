package com.wj.uikit.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.touch.OnTouchGestureListener;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.ISAPI;
import com.wj.camera.response.TwoWayAudio;
import com.wj.uikit.R;
import com.wj.uikit.player.interfac.OnVolumeChangeListener;

/**
 * FileName: WJGestureCover
 * Author: xiongxiang
 * Date: 2021/8/9
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJGestureCover extends BaseCover implements OnTouchGestureListener {
    private static final String TAG = "WJGestureCover";
    private int mWidth, mHeight;
    ImageView mVolumeIcon;
    TextView mVolumeText;
    View mVolumeBox;
    private boolean mHorizontalSlide;
    private boolean firstTouch;
    private boolean horizontalSlide;
    private int mMaxVolume = 100;
    public String mDeviceSerial;
    private int volume = 0;
    private boolean isGesture = false;
    private OnVolumeChangeListener  mOnVolumeChangeListener;


    public void setOnVolumeChangeListener(OnVolumeChangeListener onVolumeChangeListener) {
        mOnVolumeChangeListener = onVolumeChangeListener;
    }

    public OnVolumeChangeListener getOnVolumeChangeListener() {
        return mOnVolumeChangeListener;
    }

    public void setGesture(boolean gesture) {
        isGesture = gesture;
    }

    public void setDeviceSerial(String deviceSerial) {
        mDeviceSerial = deviceSerial;
        if (!TextUtils.isEmpty(mDeviceSerial)) {
            ISAPI.getInstance().getAudio(mDeviceSerial,new JsonCallback<TwoWayAudio>() {
                @Override
                public void onSuccess(TwoWayAudio data) {
                    String speakerVolume = data.getTwoWayAudioChannel().getSpeakerVolume();
                    try {
                        volume = Integer.valueOf(speakerVolume);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public String getDeviceSerial() {
        return mDeviceSerial;
    }


    public WJGestureCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.wj_layout_gesture_cover, null);
    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = getView().getWidth();
                mHeight = getView().getHeight();
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        mVolumeBox = findViewById(R.id.wj_cover_player_gesture_operation_volume_box);
        mVolumeIcon = findViewById(R.id.wj_cover_player_gesture_operation_volume_icon);
        mVolumeText = findViewById(R.id.wj_cover_player_gesture_operation_volume_text);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onSingleTapConfirmed(MotionEvent event) {

    }


    @Override
    public void onLongPress(MotionEvent event) {

    }

    @Override
    public void onDoubleTap(MotionEvent event) {

    }


    @Override
    public void onDown(MotionEvent event) {
        mHorizontalSlide = false;
        firstTouch = true;
    }

    @Override
    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!isGesture) {
            return;
        }

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
            if (Math.abs(deltaY) > mHeight)
                return;
            onVerticalSlide(deltaY / (mHeight * 5));
        }
        int action = e1.getAction();
    }


    public void onVerticalSlide(float percent) {
        mHorizontalSlide = false;
        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更进度条
        volume = index;
        String s = volume + "%";
        if (volume == 0) {
            s = "OFF";
        }
        // 显示
        setVolumeIcon(volume == 0 ? R.mipmap.wj_ic_volume_off_white : R.mipmap.wj_ic_volume_up_white);
        setVolumeBoxState(true);
        setVolumeText(s);

    }

    public void setVolumeBoxState(boolean state) {
        if (mVolumeBox != null) {
            mVolumeBox.setVisibility(state ? View.VISIBLE : View.GONE);
        }
    }

    public void setVolumeText(String text) {
        if (mVolumeText != null) {
            mVolumeText.setText(text);
        }
    }

    public void setVolumeIcon(int resId) {
        if (mVolumeIcon != null) {
            mVolumeIcon.setImageResource(resId);
        }
    }


    private int getVolume() {
        if (volume < 0)
            volume = 0;
        return volume;
    }

    private void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public void onEndGesture() {
        if (!isGesture){
            return;
        }
        setVolumeBoxState(false);
        ISAPI.getInstance().setVolume(getDeviceSerial(), volume);
        if (getOnVolumeChangeListener()!=null){
            getOnVolumeChangeListener().volumeChange(volume);
        }
    }

    @Override
    public int getCoverLevel() {
        return super.getCoverLevel();
    }
}
