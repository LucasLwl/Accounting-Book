package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
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


    /**
     * 添加数字显示格式
     *
     * @param text
     * @param type
     */
    @Override
    public void setText(CharSequence text, BufferType type) {

        super.setText(setTextType(text), type);

    }


    /**
     * 剔除文字中用于显示数字的','
     *
     * @return
     */
    @Override
    public CharSequence getText() {
        StringBuffer sb = new StringBuffer(super.getText());
        while (sb.indexOf(",") != -1)
            sb.deleteCharAt(sb.indexOf(","));
        return sb;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        refitText(setTextType(getText()).toString(), this.getWidth());
        super.onDraw(canvas);

    }


    /**
     * 设置TextView显示格式
     * 处理+、-运算符的影响：以运算符为分隔线，分为左右两个数字进行格式化
     *
     * @param text
     * @return
     */
    private CharSequence setTextType(CharSequence text) {
        String operator;
        int index;

        StringBuffer sb = new StringBuffer(text);
        if (sb.indexOf("+") != -1) {
            index = sb.indexOf("+");
            operator = "+";
        } else if (sb.indexOf("-") != -1) {
            index = sb.indexOf("-");
            operator = "-";
        } else {
            index = -1;
            operator = null;
        }

        if (index != -1) {
            StringBuffer s = new StringBuffer(divideNumber(sb.subSequence(0, index)));
            s.append(operator);
            s.append(divideNumber(sb.subSequence(index + 1, sb.length())));
            return s;
        } else {
            return divideNumber(sb);
        }
    }


    /**
     * 调整数字显示格式
     * 小数点前的数，每三位用','隔开
     *
     * @param text
     * @return
     */
    private CharSequence divideNumber(CharSequence text) {

        if (text == null)
            return null;

        StringBuffer sb = new StringBuffer(text);

        //判断是否有小数点
        int index = sb.indexOf(".");

        if (index == -1)
            //没小数点，将index调整到文末
            index = sb.length() - 1;
        else
            //有小数点，则将index调整到小数点前一位数中
            index--;

        //每三位隔开一次
        index -= 3;
        while (index >= 0) {
            sb.insert(index + 1, ',');
            index -= 3;
        }
        return sb;
    }


    /**
     * 通过判断textView的实用宽度来改变显示字体的大小，从而一行内显示完字体
     *
     * @param text      需要显示的字体
     * @param textWidth textView的总宽度
     */
    private void refitText(String text, int textWidth) {

        Log.i("ddd", text);
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
