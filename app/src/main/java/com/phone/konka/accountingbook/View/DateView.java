package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/12/6.
 */

public class DateView extends android.support.v7.widget.AppCompatTextView {


    private String mDate = "";

    private Rect mTextRect;

    private int width;
    private int height;

    private boolean isEnd = true;


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


        width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mTextRect = new Rect(0, 0, width, height);

    }

    public void setDate(String date) {
        mDate = date;
    }


    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }


    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        int r = Math.min(width, height);

        if (mDate != null && !mDate.equals("")) {
            paint.setColor(getResources().getColor(R.color.white));
            canvas.drawCircle(width / 2, height / 2, r / 2, paint);


            paint.setColor(getResources().getColor(R.color.black));
            paint.setTextSize(36);

            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (mTextRect.bottom + mTextRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(mDate, width / 2, baseline, paint);

        } else {
            paint.setColor(Color.parseColor("#778899"));
            if (isEnd) {
                canvas.drawLine(width / 2, 0, width / 2, height / 2, paint);
            } else {
                canvas.drawLine(width / 2, 0, width / 2, height, paint);
            }
            canvas.drawCircle(width / 2, height / 2, height / 6, paint);
        }
    }
}
