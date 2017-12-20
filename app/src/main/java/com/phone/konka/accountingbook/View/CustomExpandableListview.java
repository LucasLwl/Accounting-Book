package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 嵌套的ExpandableListView，避免内容显示不全
 * <p>
 * Created by 廖伟龙 on 2017/12/12.
 */

public class CustomExpandableListview extends ExpandableListView {


    public CustomExpandableListview(Context context) {
        super(context);
    }

    public CustomExpandableListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExpandableListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 计算ExpandableListView的高度，避免内容显示不全
     * <p>
     * 设置为AT_MOST模式，并且最大值设置为30位能表示的最大值
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
