package com.wj.uikit.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * FileName: WJProgressView
 * Author: xiongxiang
 * Date: 2021/5/6
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJProgressView extends View {
    //进度画笔
    private Paint mProgressPaint;
    //进度bg画笔
    private Paint mBgPaint;
    //文本画笔
    private Paint mTextPaint;
    private int size = 50;
    private int progress = 0;
    private ObjectAnimator mAnimation;
    private int mHeight;
    private int mWidth;
    private int radius;


    public WJProgressView(Context context) {
        super(context);
    }

    public WJProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        int min = Math.min(mHeight, mWidth);
        radius = min / 2-20;

    }

    private void initPaint() {
        mProgressPaint = new Paint();
        mProgressPaint.setColor(Color.parseColor("#00FF00"));
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(20);
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.parseColor("#108EE9"));
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(20);
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#108EE9"));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(size);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    public int getProgress() {
        return progress;
    }


    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mBgPaint);
        RectF rectF = new RectF();
        rectF.top = mHeight/2-radius;
        rectF.bottom = mHeight/2+radius;
        rectF.left = mWidth / 2 - radius;
        rectF.right = mWidth / 2 + radius;
        canvas.drawArc(rectF, -90, progress * 3.6f, false, mProgressPaint);
        int top = mTextPaint.getFontMetricsInt().top;
        int bottom = mTextPaint.getFontMetricsInt().bottom;
        int baselineY = mHeight / 2 + (bottom - top) / 2 - bottom;
        canvas.drawText(getProgress() + "%", mWidth / 2, baselineY, mTextPaint);
    }

    public void setProgressSmooth(int progress) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
            mAnimation = null;
        }
        mAnimation = ObjectAnimator.ofInt(this, "progress", getProgress(), progress);
        // 1 second
        mAnimation.setDuration(1000);
        //先加速在减速
//        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimation.start();
    }

}
