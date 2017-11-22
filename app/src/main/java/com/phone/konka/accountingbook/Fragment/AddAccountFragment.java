package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Adapter.TagGridViewAdapter;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.View.PopupCalculator;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountFragment extends Fragment implements View.OnClickListener {

    /**
     * fragment显示的View
     */
    private View rootView;


    /**
     * 显示日期
     */
    private TextView mTvDate;


    /**
     * 显示标签的view
     */
    private GridView mGvTag;

    /**
     * 标签数据
     */
    private List<TagBean> mList;
    private TagGridViewAdapter mAdapter;

    /**
     * 封装带有计算器的PopupWindow
     */
    private PopupCalculator mCalculator;


    /**
     * 显示计算器的PopupWindow
     */
    private PopupWindow mPopupCalculator;

    /**
     * 计算器是否为显示状态
     */
    private boolean isShow = false;


    /**
     * 鉴定GridView的手指滑动情况，用于控制计算器的显示
     */
    private View.OnTouchListener touchListener;

    /**
     * 手指按下的Y位置
     */
    private float mFirstY;

    /**
     * 手指滑动后的Y位置
     */
    private float mCusY;

    /**
     * 系统最小滑动距离
     */
    private float mTouchSlop;

    /**
     * 标志当前为支出还是收入
     * 0--支出
     * 1--收入
     */
    private int mIndex = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_account, null);
        mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();

        mCalculator = new PopupCalculator(getActivity());

        initView();
        initData();
        initEven();

        return rootView;
    }


    private void initView() {

        mGvTag = (GridView) rootView.findViewById(R.id.gv_inFragment_tag);
        mTvDate = (TextView) rootView.findViewById(R.id.tv_inFragment_date);

        mPopupCalculator = mCalculator.getPopCalculator();
    }

    private void initData() {
        if (mIndex == 0)
            mList = ((AddAccountActivity) getActivity()).mOutList;
        else
            mList = ((AddAccountActivity) getActivity()).mInList;

        mGvTag.setAdapter(mAdapter);
    }


    private void initEven() {


        rootView.findViewById(R.id.tv_fragment_out).setOnClickListener(this);
        rootView.findViewById(R.id.tv_fragment_in).setOnClickListener(this);
        rootView.findViewById(R.id.tv_fragment_date).setOnClickListener(this);
        rootView.findViewById(R.id.img_fragment_edit).setOnClickListener(this);

        touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        mCusY = event.getY();
                        if (mCusY - mFirstY > mTouchSlop) {
                            if (!isShow && mPopupCalculator != null) {
                                mPopupCalculator.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                                isShow = true;
                            }
                        } else if (mFirstY - mCusY > mTouchSlop) {
                            if (isShow && mPopupCalculator.isShowing()) {
                                mPopupCalculator.dismiss();
                                isShow = false;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        };
        mGvTag.setOnTouchListener(touchListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fragment_out:
                if (mIndex == 1) {
                    mList = ((AddAccountActivity) getActivity()).mOutList;
                    mAdapter.notifyDataSetChanged();
                    mIndex = 0;
                }
                break;
            case R.id.tv_fragment_in:
                if (mIndex == 0) {
                    mList = ((AddAccountActivity) getActivity()).mInList;
                    mAdapter.notifyDataSetChanged();
                    mIndex = 1;
                }
                break;
            case R.id.tv_fragment_date:
                break;

            case R.id.img_fragment_edit:
                ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.ADD_ACCOUNT_FRAGMENT, AddAccountActivity.EDIT_TAG_FRAGMENT);
                break;

        }
    }
}
