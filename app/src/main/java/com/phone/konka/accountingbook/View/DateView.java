package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 廖伟龙 on 2017/12/6.
 */

public class DateView extends TextView {


    private int mDate = 10;

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
    }

    public void setDate(int date) {
        mDate = date;
    }

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint;
        int x = getWidth();
        int y = getHeight();
        int r = Math.min(x, y);
        if (mDate != 0) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setStyle(Paint.Style.FILL);

            canvas.drawCircle(x / 2, y / 2, r / 2, paint);
            paint = new Paint();
            paint.setColor(Color.parseColor("#000000"));
            paint.setTextSize(30);
            canvas.drawText(mDate + "", x/2 - 15, y/2 + 15, paint);
        } else {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#778899"));
            canvas.drawLine(x/2, 0, x/2, y * 3 / 4, paint);
            canvas.drawCircle(x/2, y * 3 / 4, y / 4, paint);
        }


    }
}
