package com.phone.konka.accountingbook.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Adapter.TagGridViewAdapter;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBOperator;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;
import com.phone.konka.accountingbook.View.PopupCalculator;

import java.util.Calendar;
import java.util.Date;
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
    private int mIndex = AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT;


    private ThreadPoolManager mThreadPool;

    private DBOperator mDBOperator;
    private Calendar mCalendar;

    private int mYear;
    private int mMonth;
    private int mDay;

    private DatePickerDialog mDialog;


//    private TagBean mBeanAdd;


    private String TAG = "AddAccountFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Log.i("ddd", TAG + ":onCreateView");
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_account, null);
        mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();

        mCalculator = new PopupCalculator(getActivity());

        initView();
        initData();
        initEven();

        return rootView;
    }


    /**
     * 建通Fragment显示状态的改变
     * <p>
     * 当状态变为显示时，修改List的内容，因为可能在EditFragment中进行了Tag的修改
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("ddd", TAG + ":onHiddenChanged");
        if (!hidden && mAdapter != null) {
            if (mIndex == 0) {
                mList = ((AddAccountActivity) getActivity()).mOutList;
            } else {
                mList = ((AddAccountActivity) getActivity()).mInList;
            }
//            mList.add(mBeanAdd);
            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        }

        if (hidden && mPopupCalculator != null && mPopupCalculator.isShowing())
            dismissPopupCalculator();
    }

    private void initView() {

        mGvTag = (GridView) rootView.findViewById(R.id.gv_fragment_tag);
        mTvDate = (TextView) rootView.findViewById(R.id.tv_fragment_date);

        mPopupCalculator = mCalculator.getPopCalculator();
    }

    private void initData() {

        //初始化线程池
        mThreadPool = ThreadPoolManager.getInstance();

        //初始化数据库操作类
        mDBOperator = new DBOperator(getActivity());

        //获取日历
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mTvDate.setText(mMonth + "月" + mDay + "日");

        if (mIndex == 0)
            mList = ((AddAccountActivity) getActivity()).mOutList;
        else
            mList = ((AddAccountActivity) getActivity()).mInList;
//        mList.add(mBeanAdd);

        mAdapter = new TagGridViewAdapter(getActivity(), mList);

        mGvTag.setAdapter(mAdapter);
    }


    private void initEven() {

        /**
         * 设置计算器的保存账单回调
         *
         */
        mCalculator.setAddAccountListener(new PopupCalculator.AddAccountListener() {
            @Override
            public void addAccount(String tag, double money) {
                final DetailTagBean bean;
                if (mIndex == AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT) {
                    bean = new DetailTagBean(mYear, mMonth, mDay, tag, -money);
                    Toast.makeText(getActivity(), "支出 " + tag + ":" + money + "已保存", Toast.LENGTH_SHORT).show();
                } else {
                    bean = new DetailTagBean(mYear, mMonth, mDay, tag, money);
                    Toast.makeText(getActivity(), "收入 " + tag + ":" + money + "已保存", Toast.LENGTH_SHORT).show();
                }
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDBOperator.insertAccount(bean);
                    }
                });
            }
        });

        rootView.findViewById(R.id.tv_fragment_out).setOnClickListener(this);
        rootView.findViewById(R.id.tv_fragment_in).setOnClickListener(this);
        rootView.findViewById(R.id.tv_fragment_date).setOnClickListener(this);
        rootView.findViewById(R.id.img_fragment_edit).setOnClickListener(this);

        /**
         * 监听gridview的触摸事件，用于显示下滑显示计算器，上滑隐藏计算器
         */
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
                            showPopupCalculator();
                        } else if (mFirstY - mCusY > mTouchSlop) {
                            dismissPopupCalculator();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        };
        mGvTag.setOnTouchListener(touchListener);

        /**
         * gridview的itme点击事件，用于点击item显示计算器
         */
        mGvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mList.size() - 1) {
                    dismissPopupCalculator();
                    ((AddAccountActivity) getActivity()).showFragment(mIndex,
                            AddAccountActivity.ADD_TAG_FRAGMENT);
                } else {
                    mAdapter.setSelected(position);
                    showPopupCalculator();
                    mCalculator.setTagText(mList.get(position).getText());
                }
            }
        });
    }

    private void showPopupCalculator() {
        if (!isShow && mPopupCalculator != null) {
            mPopupCalculator.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
            isShow = true;
        }
    }

    private void dismissPopupCalculator() {
        if (isShow && mPopupCalculator.isShowing()) {
            mPopupCalculator.dismiss();
            isShow = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fragment_out:
                if (mIndex == AddAccountActivity.ADD_ACCOUNT_FRAGMENT_IN) {
                    mList = ((AddAccountActivity) getActivity()).mOutList;
//                    mList.add(mBeanAdd);
                    mAdapter.setList(mList);
                    mAdapter.notifyDataSetChanged();
                    mIndex = AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT;
                }
                break;
            case R.id.tv_fragment_in:
                if (mIndex == AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT) {
                    mList = ((AddAccountActivity) getActivity()).mInList;
//                    mList.add(mBeanAdd);
                    mAdapter.setList(mList);
                    mAdapter.notifyDataSetChanged();
                    mIndex = AddAccountActivity.ADD_ACCOUNT_FRAGMENT_IN;
                }
                break;
            case R.id.tv_fragment_date:

                mDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        mYear = i;
                        mMonth = ++i1;
                        mDay = i2;
                        mTvDate.setText(mMonth + "月" + mDay + "日");
                    }
                }, mYear, mMonth - 1, mDay);
                mDialog.getDatePicker().setMaxDate(new Date().getTime());
                mDialog.show();
                break;

            case R.id.img_fragment_edit:
                ((AddAccountActivity) getActivity()).showFragment(mIndex, AddAccountActivity.EDIT_TAG_FRAGMENT);
                break;
        }
    }
}
