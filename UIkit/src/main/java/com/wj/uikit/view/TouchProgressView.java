package com.wj.uikit.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * FileName: TouchProgressView
 * Author: xiongxiang
 * Date: 2021/3/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */

public class TouchProgressView extends View {

    private Paint linePaint;
    private Paint pointPaint;

    private int pointRadius = 0;
    private int progress = 0;
    private final int PROGRESS_MIN = 0;
    private final int PROGRESS_MAX = 100;

    private OnProgressChangedListener progressChangedListener;

    public interface OnProgressChangedListener {
        void onProgressChanged(View view, int progress);

        void onUpProgressChanged(View view, int progress);
    }

    public TouchProgressView(Context context) {
        super(context, null);
    }

    public TouchProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置百分比
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("progress 不可以小于0 或大于100");
        }
        if (this.progress != progress) {
            this.progress = progress;
            invalidate();
            if (progressChangedListener != null) {
                progressChangedListener.onProgressChanged(this, progress);
            }
        }

    }

    /**
     * 设置进度变化监听器
     *
     * @param onProgressChangedListener
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < pointRadius) {
            setProgress(PROGRESS_MIN);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (progressChangedListener != null) {
                    progressChangedListener.onUpProgressChanged(this, progress);
                }
            }
            return true;
        } else if (event.getX() > getWidth() - pointRadius) {
            setProgress(PROGRESS_MAX);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (progressChangedListener != null) {
                    progressChangedListener.onUpProgressChanged(this, progress);
                }
            }
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setProgress(calculProgress(event.getX()));
                    return true;
                case MotionEvent.ACTION_MOVE:
                    setProgress(calculProgress(event.getX()));
                    return true;
                case MotionEvent.ACTION_UP:
                    if (progressChangedListener != null) {
                        progressChangedListener.onUpProgressChanged(this, progress);
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private static final String TAG = "TouchProgressView";

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);

        float rx = getHeight() / 2;
        float ry = getHeight() / 2;
        int lineHeight = getHeight() / 6;

        RectF rect = new RectF(0, ry - lineHeight, getWidth(), ry + lineHeight);

        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.parseColor("#F8FCFF"));
        canvas.drawRoundRect(rect, rx, ry, linePaint);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#E7F3FF"));
        canvas.drawRoundRect(rect, rx, ry, linePaint);

        if (progress <= 0) {
            rect.right = 0;
        } else {
            float mP = (float) getWidth() / 100;
            rect.right = mP * progress;
        }

        int[] colors = {Color.parseColor("#FFE2F2FF"), Color.parseColor("#FF108EE9")};
        LinearGradient linearGradient = new LinearGradient(0, 0, rect.right, 0, colors, null, Shader.TileMode.CLAMP);

        linePaint.setShader(linearGradient);

        Log.i(TAG, "draw: " + progress);
        linePaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rect, rx, ry, linePaint);

        pointRadius = getMeasuredHeight() / 2;
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.parseColor("#A9DBFF"));
        canvas.drawCircle(getCx(), getHeight() / 2, pointRadius, pointPaint);
        pointPaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(getCx(), getHeight() / 2, pointRadius / 3 * 2, pointPaint);
    }

    /**
     * 获取圆点的x轴坐标
     *
     * @return
     */
    private float getCx() {
        float cx = 0.0f;
        cx = (getWidth() - pointRadius * 2);
        if (cx < 0) {
            throw new IllegalArgumentException("TouchProgressView 宽度不可以小于 2 倍 pointRadius");
        }
        return cx / 100 * progress + pointRadius;
    }

    /**
     * 计算触摸点的百分比
     *
     * @param eventX
     * @return
     */
    private int calculProgress(float eventX) {
        float proResult = (eventX - pointRadius) / (getWidth() - pointRadius * 2);
        return (int) (proResult * 100);
    }
}

