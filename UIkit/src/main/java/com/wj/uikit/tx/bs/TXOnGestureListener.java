package com.wj.uikit.tx.bs;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * FileName: TXOnGestureListener
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface TXOnGestureListener extends GestureDetector.OnGestureListener {
    void  onEndGesture(MotionEvent e);
}
