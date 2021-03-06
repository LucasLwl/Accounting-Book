package com.phone.konka.accountingbook.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Activity.DetailActivity;
import com.phone.konka.accountingbook.Adapter.TagGridViewAdapter;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ProviderManager;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;
import com.phone.konka.accountingbook.View.PopupCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 添加账单的Fragment
 * <p>
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountFragment extends Fragment implements View.OnClickListener {

    /**
     * fragment显示的View
     */
    private View rootView;


    /**
     * 显示支出
     */
    private TextView mTvOut;


    /**
     * 显示收入
     */
    private TextView mTvIn;


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


    /**
     * 适配器
     */
    private TagGridViewAdapter mAdapter;


    /**
     * 封装带有计算器的PopupWindow
     */
    private PopupCalculator mCalculator;


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


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;


    /**
     * 数据库操作
     */
    private ProviderManager mDataManager;


    /**
     * 日历
     */
    private Calendar mCalendar;


    /**
     * 当前年份
     */
    private int mYear;


    /**
     * 当前月份
     */
    private int mMonth;


    /**
     * 当前日期
     */
    private int mDay;


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


    /**
     * 建通Fragment显示状态的改变
     * <p>
     * 当状态变为显示时，修改List的内容，因为可能在EditFragment中进行了Tag的修改
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden && mCalculator != null && mCalculator.isPopCalculatorShow()) {
            mCalculator.dismissPopCalculator();
        }

        if (!hidden && mAdapter != null) {
            if (mIndex == 0) {
                mList = ((AddAccountActivity) getActivity()).mOutList;
            } else {
                mList = ((AddAccountActivity) getActivity()).mInList;
            }
            mAdapter.setList(mList, mIndex);

            if (mList.size() > 1) {
                mCalculator.showPopCalculator(rootView);
                mCalculator.setTagText(mList.get(0).getText());
                mCalculator.setTagIcon(mList.get(0).getIconID());
            }
        }

        super.onHiddenChanged(hidden);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCalculator != null && mCalculator.isPopCalculatorShow()) {
            mCalculator.dismissPopCalculator();
        }
    }


    /**
     * 初始化View
     */
    private void initView() {

        mTvOut = (TextView) rootView.findViewById(R.id.tv_fragment_out);
        mTvIn = (TextView) rootView.findViewById(R.id.tv_fragment_in);

        mGvTag = (GridView) rootView.findViewById(R.id.gv_fragment_tag);
        mTvDate = (TextView) rootView.findViewById(R.id.tv_fragment_date);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        //初始化线程池
        mThreadPool = ThreadPoolManager.getInstance();

        //初始化Provider操作类
        mDataManager = new ProviderManager(getActivity());

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

        mAdapter = new TagGridViewAdapter(getActivity(), mList);

        mGvTag.setAdapter(mAdapter);

        if (mList.size() > 1) {
            mCalculator.showPopCalculator(rootView);
            mCalculator.setTagText(mList.get(0).getText());
            mCalculator.setTagIcon(mList.get(0).getIconID());
        }
    }


    /**
     * 初始化事件
     */
    private void initEven() {


        mTvIn.setOnClickListener(this);
        mTvOut.setOnClickListener(this);
        rootView.findViewById(R.id.tv_fragment_date).setOnClickListener(this);
        rootView.findViewById(R.id.img_fragment_edit).setOnClickListener(this);


        /**
         * 设置计算器的保存账单回调
         *
         */
        mCalculator.setAddAccountListener(new PopupCalculator.AddAccountListener() {
            @Override
            public void addAccount(String tag, int iconID, double money) {
                final DetailTagBean bean;
                if (mIndex == AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT) {
                    bean = new DetailTagBean(mYear, mMonth, mDay, tag, iconID, -money);
//                    Toast.makeText(getActivity(), "支出 " + tag + ":" + DoubleTo2Decimal.doubleTo2Decimal(money) + "已保存", Toast.LENGTH_SHORT).show();
                } else {
                    bean = new DetailTagBean(mYear, mMonth, mDay, tag, iconID, money);
//                    Toast.makeText(getActivity(), "收入 " + tag + ":" + DoubleTo2Decimal.doubleTo2Decimal(money) + "已保存", Toast.LENGTH_SHORT).show();
                }
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDataManager.insertAccount(bean);
                    }
                });
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                getActivity().startActivity(intent);
            }
        });


        /**
         * 监听gridview的触摸事件，用于显示下滑显示计算器，上滑隐藏计算器
         */

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mList.size() > 1) {
                            mCusY = event.getY();
                            if (mCusY - mFirstY > mTouchSlop) {
                                mCalculator.showPopCalculator(rootView);
                                mCalculator.setTagText(mList.get(mAdapter.getSelected()).getText());
                                mCalculator.setTagIcon(mList.get(mAdapter.getSelected()).getIconID());
                            } else if (mFirstY - mCusY > mTouchSlop) {
                                mCalculator.dismissPopCalculator();
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

        /**
         * GridView的itme点击事件，用于点击item显示计算器
         */
        mGvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mList.size() - 1) {
                    mCalculator.dismissPopCalculator();
                    ((AddAccountActivity) getActivity()).showFragment(mIndex,
                            AddAccountActivity.ADD_TAG_FRAGMENT);
                } else {
                    mAdapter.setSelected(position);
                    mCalculator.showPopCalculator(rootView);
                    mCalculator.setTagText(mList.get(position).getText());
                    mCalculator.setTagIcon(mList.get(position).getIconID());
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            点击支出
            case R.id.tv_fragment_out:
                if (mIndex == AddAccountActivity.ADD_ACCOUNT_FRAGMENT_IN) {

                    mIndex = AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT;
                    //设置高亮
                    mTvOut.setAlpha(1.0f);
                    mTvIn.setAlpha(0.3f);

                    mList = ((AddAccountActivity) getActivity()).mOutList;
                    mAdapter.setList(mList, mIndex);

                    if (mList.size() == 1)
                        mCalculator.dismissPopCalculator();
                    else {
                        mCalculator.showPopCalculator(rootView);
                        mCalculator.setTagText(mList.get(mAdapter.getSelected()).getText());
                        mCalculator.setTagIcon(mList.get(mAdapter.getSelected()).getIconID());
                    }

                }
                break;

//            点击收入
            case R.id.tv_fragment_in:

                if (mIndex == AddAccountActivity.ADD_ACCOUNT_FRAGMENT_OUT) {

                    mIndex = AddAccountActivity.ADD_ACCOUNT_FRAGMENT_IN;

                    //设置高亮
                    mTvOut.setAlpha(0.3f);
                    mTvIn.setAlpha(1.0f);

                    mList = ((AddAccountActivity) getActivity()).mInList;
                    mAdapter.setList(mList, mIndex);

                    if (mList.size() == 1)
                        mCalculator.dismissPopCalculator();
                    else {
                        mCalculator.showPopCalculator(rootView);
                        mCalculator.setTagText(mList.get(mAdapter.getSelected()).getText());
                        mCalculator.setTagIcon(mList.get(mAdapter.getSelected()).getIconID());
                    }

                }
                break;

//            点击日期
            case R.id.tv_fragment_date:


                DatePickerDialog mDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mYear = year;
                        mMonth = ++month;
                        mDay = day;
                        mTvDate.setText(mMonth + "月" + mDay + "日");
                    }
                }, mYear, mMonth - 1, mDay);
                mDialog.getDatePicker().setMaxDate(new Date().getTime());
                mDialog.show();
                break;

//            点击编辑
            case R.id.img_fragment_edit:
                ((AddAccountActivity) getActivity()).showFragment(mIndex, AddAccountActivity.EDIT_TAG_FRAGMENT);
                break;
        }
    }
}
