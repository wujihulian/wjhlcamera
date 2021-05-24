package com.wj.uikit.uitl;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

/**
 * FileName: OnContinuousClick
 * Author: xiongxiang
 * Date: 2021/5/13
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class OnContinuousClick implements View.OnClickListener {
    final int COUNTS = 4;// 点击次数
    final long DURATION = 1000;// 规定有效时间
    long[] mHits = new long[COUNTS];
    @Override
    public void onClick(View v) {
        continuousClick(v);
    }
    private void continuousClick(View view) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
            click(view);
        }
    }

    public abstract void click(View view);
}
