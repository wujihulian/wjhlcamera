package com.wj.uikit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * FileName: VolumeProgressView
 * Author: xiongxiang
 * Date: 2021/8/9
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class VolumeProgressView extends FrameLayout {


    public VolumeProgressView(@NonNull Context context) {
        super(context);
    }

    public VolumeProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VolumeProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint;
    int w, h;
    float average;
    int progress;

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getMeasuredWidth();
        h = getMeasuredHeight();
        average = h / 100f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(5);
        }
        float y = 0;
        if (progress == 0) {
            y = h;
        } else {
            float yy = average * progress;
            y = h - yy;
            if (y <= 0) {
                y = paint.getStrokeWidth();
            } else if (progress == 100) {
                y = h - paint.getStrokeWidth();

            }
        }
        canvas.drawLine(0, y, w, y, paint);

    }
}
