package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 廖伟龙 on 2017/12/12.
 */

public class LineCircleView extends android.support.v7.widget.AppCompatTextView {


    private boolean isEnd;
    private boolean isFirst;

    private Paint mPaint;

    private int mWidth;
    private int mHeight;


    public LineCircleView(Context context) {
        super(context);
    }

    public LineCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setEnd(boolean end) {
        isEnd = end;
        invalidate();
    }

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