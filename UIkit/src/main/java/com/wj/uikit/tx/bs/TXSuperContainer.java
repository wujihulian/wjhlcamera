package com.wj.uikit.tx.bs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wj.camera.uitl.WJLogUitl;

/**
 * FileName: TXSuperContainer
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXSuperContainer extends FrameLayout implements TXOnGestureListener {
    private TXReceiverGroup mReceiverGroup;
    private TXEventDispatcher mEventDispatcher;
    private GestureDetector mGestureDetector;

    public void setReceiverGroup(TXReceiverGroup receiverGroup) {
        mReceiverGroup = receiverGroup;
        mEventDispatcher = new TXEventDispatcher(mReceiverGroup);
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), this);
    }

    public TXSuperContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public TXSuperContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TXSuperContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        WJLogUitl.d("");
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                onEndGesture(event);
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        WJLogUitl.d("onDown");
        mEventDispatcher.onDown(e);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        WJLogUitl.d("onShowPress");
        mEventDispatcher.onShowPress(e);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        WJLogUitl.d("onSingleTapUp");
        return mEventDispatcher.onSingleTapUp(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        WJLogUitl.d("onScroll" + distanceX + "  " + distanceY);
        return mEventDispatcher.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        WJLogUitl.d("onLongPress");
        mEventDispatcher.onLongPress(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        WJLogUitl.d("onFling" + velocityX + "  " + velocityY);
        return mEventDispatcher.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onEndGesture(MotionEvent e) {
        WJLogUitl.d("onEndGesture");
        mEventDispatcher.onEndGesture(e);
    }
}
