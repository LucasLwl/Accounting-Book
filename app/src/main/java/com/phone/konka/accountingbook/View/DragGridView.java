package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.phone.konka.accountingbook.Adapter.DragGridAdapter;
import com.phone.konka.accountingbook.R;


/**
 * 可拖动Item位置的GridView
 * <p>
 * Created by 廖伟龙 on 2017/11/20.
 */

public class DragGridView extends GridView implements AdapterView.OnItemLongClickListener {

    /**
     * 模式
     */
    public static final int MODE_NORMAL = 0;
    public static final int MODE_DRAG = 1;


    /**
     * 当前模式
     */
    private int mode = MODE_NORMAL;


    /**
     * 用于在屏幕上拖动
     */
    private View mWinDragView;


    /**
     * 拖动View的LayoutParams
     */
    private WindowManager.LayoutParams mDragViewlp;

    /**
     * 用于添加Window
     */
    private WindowManager mWinManager;


    /**
     * 当前拖动的View
     */
    private View mCusDragView;


    /**
     * View被拖到的位置
     */
    private int mDragContentPos;


    /**
     * View视图被拖到的位置
     */
    private int mDragViewPos;


    /**
     * 点击事件时的位置
     */
    private float mEvX;
    private float mEvY;


    /**
     * 点击事件与控件边的距离
     */
    private float mdX;
    private float mdY;


    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWinManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnItemLongClickListener(this);
    }


    /**
     * 监听手指操作
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mEvX = ev.getRawX();
                mEvY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_DRAG) {
                    updateWindow(ev);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mode == MODE_DRAG) {
                    closeWindow();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 更新拖动winView的位置
     *
     * @param ev
     */
    private void updateWindow(MotionEvent ev) {
        if (mode == MODE_DRAG) {
            float x = ev.getRawX() - mdX;
            float y = ev.getRawY() - mdY;
            if (mDragViewlp != null) {
                mDragViewlp.x = (int) x;
                mDragViewlp.y = (int) y;
                mWinManager.updateViewLayout(mWinDragView, mDragViewlp);
            }

            /**
             * 获取手指活动在GridView的哪个位置，或者在GridView外
             */
            int dropPos = pointToPosition((int) ev.getX(), (int) ev.getY());
            if (dropPos == mDragViewPos || dropPos == GridView.INVALID_POSITION) {
                return;
            }
            itemMove(dropPos);
        }
    }


    /**
     * 动画移动Item
     *
     * @param dropPos
     */
    private void itemMove(int dropPos) {
        TranslateAnimation translate;
        if (dropPos < mDragViewPos) {
            for (int i = dropPos; i < mDragViewPos; i++) {
                View view = getChildAt(i);
                View nextView = getChildAt(i + 1);
                float width = (nextView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float height = (nextView.getTop() - view.getTop()) * 1f / view.getHeight();
                translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, width,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, height);
                translate.setInterpolator(new LinearInterpolator());
                translate.setFillAfter(true);
                translate.setDuration(300);
                if (i == mDragViewPos - 1) {
                    translate.setAnimationListener(animationListener);
                }
                view.startAnimation(translate);
            }
        } else {
            for (int i = mDragViewPos + 1; i <= dropPos; i++) {
                View view = this.getChildAt(i);
                View preView = this.getChildAt(i - 1);
                float width = (preView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float height = (preView.getTop() - view.getTop()) * 1f / view.getHeight();
                translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, width,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, height);
                translate.setInterpolator(new LinearInterpolator());
                translate.setFillAfter(true);
                translate.setDuration(300);

                /**
                 * 设置最后一个移动item的动画监听事件
                 */
                if (i == dropPos) {
                    translate.setAnimationListener(animationListener);
                }
                view.startAnimation(translate);
            }
        }
//        移动完成后，标志的位置
        mDragViewPos = dropPos;
    }

    /**
     * 动画监听事件
     */
    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ListAdapter adapter = getAdapter();
            if (adapter != null && mDragViewPos != mDragContentPos && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(mDragContentPos, mDragViewPos, true);
                mDragContentPos = mDragViewPos;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };


    /**
     * 关闭拖动的winView
     */
    private void closeWindow() {

        if (mWinDragView != null) {
            mWinManager.removeView(mWinDragView);
            mWinDragView = null;
            mDragViewlp = null;
        }
        mode = MODE_NORMAL;
        if (mDragViewPos == mDragContentPos || mDragViewPos == GridView.INVALID_POSITION) {
            getChildAt(mDragContentPos).setVisibility(View.VISIBLE);
        }
        /**
         *  拖动后迅速松开手指的情况，既在移动动画未结束之前
         */
        else {
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(mDragContentPos, mDragViewPos, false);
                mDragContentPos = mDragViewPos;
            }
        }
    }

    /**
     * item的长点击事件，用于拖起item
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (mode == MODE_DRAG) {
            return false;
        }

        mCusDragView = view;
        mDragContentPos = position;
        mDragViewPos = position;
        mdX = mEvX - view.getLeft() - this.getLeft();
        mdY = mEvY - view.getTop() - this.getTop();
        initWindow();
        return true;
    }

    /**
     * 初始化可拖动的View，和LayoutParams
     */
    private void initWindow() {
        if (mWinDragView == null) {
            mWinDragView = LayoutInflater.from(getContext()).inflate(R.layout.item_draggridview, null);
            TextView tvDrag = (TextView) mWinDragView.findViewById(R.id.tv_dragGridView_item_tag);
            ImageView imgDrag = (ImageView) mWinDragView.findViewById(R.id.img_dragGridView_item_tag);
            tvDrag.setText(((TextView) mCusDragView.findViewById(R.id.tv_dragGridView_item_tag)).getText());
            imgDrag.setImageDrawable(((ImageView) mCusDragView.findViewById(R.id.img_dragGridView_item_tag)).getDrawable());
        }
        if (mDragViewlp == null) {
            mDragViewlp = new WindowManager.LayoutParams();
            mDragViewlp.type = WindowManager.LayoutParams.TYPE_PHONE;
            mDragViewlp.format = PixelFormat.RGBA_8888;
            mDragViewlp.gravity = Gravity.TOP | Gravity.LEFT;
            mDragViewlp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mDragViewlp.width = mCusDragView.getWidth();
            mDragViewlp.height = mCusDragView.getHeight();
            mDragViewlp.x = mCusDragView.getLeft() + this.getLeft();
            mDragViewlp.y = mCusDragView.getTop() + this.getTop();
            mCusDragView.setVisibility(View.INVISIBLE);
        }

//        显示WindowView
        mWinManager.addView(mWinDragView, mDragViewlp);
        mode = MODE_DRAG;
    }
}
