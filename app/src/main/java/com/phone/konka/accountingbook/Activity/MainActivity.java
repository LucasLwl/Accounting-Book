package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.Utils.ProviderManager;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.util.Calendar;


/**
 * 主页Activity
 */
public class MainActivity extends Activity implements View.OnClickListener {


    /**
     * 本月支出TextView
     */
    private TextView mTvMonthOut;


    /**
     * 本月收入TextView
     */
    private TextView mTvMonthIn;


    /**
     * 本月结余TextView
     */
    private TextView mTvMonthLeft;


    /**
     * 最近支出TextView
     */
    private TextView mTvLeastOut;


    /**
     * 今天支出TextView
     */
    private TextView mTvTodayOut;


    /**
     * 本周支出TextView
     */
    private TextView mTvWeekOut;


    /**
     * Provider操作类
     */
    private ProviderManager mDataManager;


    /**
     * 日历
     */
    private Calendar mCalendar;


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;

    /**
     * 本月支出金额
     */
    private double monthOut;


    /**
     * 本月收入金额
     */
    private double monthIn;


    /**
     * 今天支出金额
     */
    private double dayOut;

    /**
     * 最近支出金额
     */
    private double leastOut;


    /**
     * 本周支出金额
     */
    private double weekOut;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                mTvMonthIn.setText(DoubleTo2Decimal.doubleTo2Decimal(monthIn));

                mTvMonthOut.setText(DoubleTo2Decimal.doubleTo2Decimal(monthOut));

                mTvMonthLeft.setText(DoubleTo2Decimal.doubleTo2Decimal(monthIn - monthOut));

                mTvTodayOut.setText(DoubleTo2Decimal.doubleTo2Decimal(dayOut));

                mTvLeastOut.setText(DoubleTo2Decimal.doubleTo2Decimal(leastOut));

                mTvWeekOut.setText(DoubleTo2Decimal.doubleTo2Decimal(weekOut));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initState();

        initView();
        initData();
        initEven();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        获取数据库数据
        getDataFromDB();
    }


    /**
     * 设置状态栏透明,实现沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_main_bar);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll.getLayoutParams();
            lp.height = getStatusBarHeight();
            ll.setLayoutParams(lp);
        }
    }

    /**
     * 初始化View
     */
    private void initView() {

        mTvMonthOut = (TextView) findViewById(R.id.tv_main_moonOut);
        mTvMonthIn = (TextView) findViewById(R.id.tv_main_moonIn);
        mTvMonthLeft = (TextView) findViewById(R.id.tv_main_moonLeft);

        mTvLeastOut = (TextView) findViewById(R.id.tv_main_leastOut);
        mTvTodayOut = (TextView) findViewById(R.id.tv_main_todayOut);
        mTvWeekOut = (TextView) findViewById(R.id.tv_main_weekOut);

    }


    /**
     * 初始化数据
     */
    private void initData() {
        mThreadPool = ThreadPoolManager.getInstance();

        mCalendar = Calendar.getInstance();

        mDataManager = new ProviderManager(this);
    }


    /**
     * 初始化事件
     */
    private void initEven() {
        findViewById(R.id.ll_main_detail).setOnClickListener(this);
        findViewById(R.id.rl_main_addAccount).setOnClickListener(this);
        findViewById(R.id.img_main_setting).setOnClickListener(this);
    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {

        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取数据库数据
     */
    private void getDataFromDB() {

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                monthIn = mDataManager.getMonthIn(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
                monthOut = mDataManager.getMonthOut(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
                dayOut = mDataManager.getDayOut(mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.DAY_OF_MONTH));
                leastOut = mDataManager.getLeastOut();
                weekOut = mDataManager.getWeekOut();
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

//            点击设置
            case R.id.img_main_setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;


//            点击账单详情
            case R.id.ll_main_detail:
                intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                break;

//            点击添加账单
            case R.id.rl_main_addAccount:
                intent = new Intent(MainActivity.this, AddAccountActivity.class);
                startActivity(intent);
                break;

        }
    }


}
