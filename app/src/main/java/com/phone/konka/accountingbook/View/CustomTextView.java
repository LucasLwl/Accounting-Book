package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * 自动调整 TextView 文字大小仅显示一行文字
 * <p>
 * Created by 廖伟龙 on 2017/12/13.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {


    private Paint mPaint;


    /**
     * 当前的字体大小
     */
    private float mTextSize;


    /**
     * 默认的字体大小
     */
    private final float mDefaultTextSize;


    public CustomTextView(Context context) {
        super(context);
        mDefaultTextSize = getTextSize();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDefaultTextSize = getTextSize();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultTextSize = getTextSize();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        refitText(getText().toString(), this.getWidth());
        super.onDraw(canvas);

    }


    /**
     * 通过判断textView的实用宽度来改变显示字体的大小，从而一行内显示完字体
     *
     * @param text      需要显示的字体
     * @param textWidth textView的总宽度
     */
    private void refitText(String text, int textWidth) {

//        当textView的总宽度大于0时才能显示文字
        if (textWidth > 0) {

//            获取当前文字大小，返回的单位为Px
            mTextSize = getTextSize();
            mPaint = new Paint();
            mPaint.set(this.getPaint());

//            获取textView的实用大小
            int availableWidth = textWidth - getPaddingLeft() - getPaddingRight();


//            测量文本需要的大小
            mPaint.setTextSize(mTextSize);
            float textWidths = mPaint.measureText(text);


            /**
             * 判断删除字符时，是否可以增大textSize
             */
            while (textWidths < availableWidth && mTextSize < mDefaultTextSize) {
                mTextSize++;
                mPaint.setTextSize(mTextSize);
                textWidths = mPaint.measureText(text);
            }

            /**
             * 有两种方式跳出上面的while循环
             * 第一种，文字的大小达到了默认大小  不需要处理
             * 第二种，文本需要的大小大于textView实用的大小，则需要将文字大小回退一步
             */
            if (textWidths > availableWidth)
                mTextSize--;


            /**
             * 判断增加字符时，是否需要减小textSize
             */
            while (textWidths > availableWidth) {
                mTextSize--;
                mPaint.setTextSize(mTextSize);
                textWidths = mPaint.measureText(text);
            }

            /**
             * 设置当前textView的文字大小，会重绘文字
             * 单位为Px
             */
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
    }
}
