package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 直线圆圈View
 * <p>
 * Created by 廖伟龙 on 2017/12/12.
 */

public class LineCircleTextView extends android.support.v7.widget.AppCompatTextView {


    /**
     * 是否为最有一个
     */
    private boolean isEnd;


    /**
     * 是否为第一个
     */
    private boolean isFirst;

    private Paint mPaint;


    /**
     * View内容的有效宽度
     */
    private int mWidth;


    /**
     * View内容的有效高度
     */
    private int mHeight;


    public LineCircleTextView(Context context) {
        super(context);
    }

    public LineCircleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineCircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 设置是否为最后一个
     *
     * @param end
     */
    public void setEnd(boolean end) {
        isEnd = end;
        invalidate();
    }


    /**
     * 设置是否为第一个
     *
     * @param first
     */
    public void setFirst(boolean first) {
        isFirst = first;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }


    /**
     * 若为第一个,则只画圆
     * 若为最后一个,则画圆,和圆上直线
     * 若为中间的,则画圆,和圆上,圆下的直线
     *
     * @param canvas
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#778899"));

        canvas.drawCircle(mWidth / 2, mHeight / 2, Math.min(mWidth, mHeight) / 6, mPaint);

        if (isFirst && isEnd) {
            return;
        } else if (isFirst) {
            canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight, mPaint);
        } else if (isEnd) {
            canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight / 2, mPaint);
        } else {
            canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
        }

    }
}
