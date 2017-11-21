package com.phone.konka.accountingbook.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.icu.text.BreakIterator;
import android.icu.text.UnicodeSet;
import android.os.Bundle;
import android.service.carrier.CarrierService;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.phone.konka.accountingbook.Adapter.TagGridViewAdapter;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class InFragment extends Fragment {

    private View rootView;

    private GridView mGvTag;
    private List<TagBean> mList;
    private TagGridViewAdapter mAdapter;

    private PopupWindow mPopupCalculator;
    private View mPopupView;
    private boolean isShow = false;

    private View.OnTouchListener touchListener;

    private float mFirstY;
    private float mCusY;
    private float mTouchSlop;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();

        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_in, null);

        initView();
        initEven();


        return rootView;
    }


    private void initEven() {
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

    private void initView() {
        mGvTag = (GridView) rootView.findViewById(R.id.gv_infragment_tag);

        mList = new ArrayList<>();
        TagBean bean = new TagBean();
        bean.setText("123");
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mList.add(bean);
        mAdapter = new TagGridViewAdapter(getActivity(), mList);
        mGvTag.setAdapter(mAdapter);


        mPopupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_calculator, null);
        mPopupCalculator = new PopupWindow(mPopupView, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        mPopupCalculator.setOutsideTouchable(true);
        mPopupCalculator.setTouchable(true);
        mPopupCalculator.setAnimationStyle(R.style.popupCalculatorStyle);
    }


}
