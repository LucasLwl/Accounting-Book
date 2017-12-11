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
    private Paint mTextPaint;


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


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(getResources().getColor(R.color.black));
        mTextPaint.setTextSize(36);
        mTextRect = new Rect();
        mTextPaint.getTextBounds(mDate, 0, mDate.length(), mTextRect);
    }

    public void setDate(String date) {
        mDate = date;
    }


    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint;
        int x = getWidth();
        int y = getHeight();
        int r = Math.min(x, y);
        if (mDate != null && !mDate.equals("")) {
            paint = new Paint();
            paint.setColor(getResources().getColor(R.color.white));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x / 2, y / 2, r / 2, paint);

            canvas.drawText(mDate, (x - mTextRect.width()) / 2, (y + mTextRect.height()) / 2, mTextPaint);


        } else {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#778899"));
            canvas.drawLine(x / 2, 0, x / 2, y * 3 / 4, paint);
            canvas.drawCircle(x / 2, y * 3 / 4, y / 4, paint);
        }
    }
}
