package com.wj.uikit.tx.bs;

import android.view.MotionEvent;

/**
 * FileName: TXEventDispatcher
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXEventDispatcher  implements TXOnGestureListener {
    public TXReceiverGroup mReceiverGroup;

    public TXEventDispatcher(TXReceiverGroup txReceiverGroup) {
        this.mReceiverGroup = txReceiverGroup;
    }
    @Override
    public void onLongPress(MotionEvent event) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onLongPress(event);
                }
            }
        });
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver  instanceof  TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onFling(e1,e2,velocityX,velocityY);
                }
            }
        });

        return false;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver  instanceof  TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onDown(e);
                }
            }
        });
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof  TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onShowPress(e);
                }
            }
        });
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof  TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onSingleTapUp(e);
                }
            }
        });
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof  TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onScroll(e1,e2,distanceX,distanceY);
                }
            }
        });
        return false;
    }


    @Override
    public void onEndGesture(MotionEvent e) {
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof TXOnGestureListener){
                    ((TXOnGestureListener) receiver).onEndGesture(e);
                }
            }
        });
    }
}
