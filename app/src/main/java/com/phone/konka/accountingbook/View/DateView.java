package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Scroller;

import com.phone.konka.accountingbook.R;

/**
 * 显示日期的View
 * <p>
 * Created by 廖伟龙 on 2017/12/6.
 */

public class DateView extends android.support.v7.widget.AppCompatTextView {

    /**
     * 显示的日期
     */
    private String mDate = "11";


    /**
     * 日期文字显示的范围
     */
    private Rect mTextRect;

    /**
     * View内容的有效宽度
     */
    private int mWidth;

    /**
     * View内容的有效高度
     */
    private int mHeight;


    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mTextRect = new Rect(0, 0, mWidth, mHeight);

    }



    /**
     * 设置显示的日期
     *
     * @param date
     */
    public void setDate(String date) {
        mDate = date;
        invalidate();
    }


    /**
     * 先画一层白色的圆底色
     * 然后画出日期
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

//        圆的直径
        int r = Math.min(mWidth, mHeight);

        if (mDate != null && !mDate.equals("")) {

//            画圆
            paint.setColor(getResources().getColor(R.color.white));
            canvas.drawCircle(mWidth / 2, mHeight / 2, r / 2, paint);

            paint.setColor(getResources().getColor(R.color.black));
            paint.setTextSize(36);


//            画日期
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (mTextRect.bottom + mTextRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mDate, mWidth / 2, baseline, paint);
        }
    }
}
