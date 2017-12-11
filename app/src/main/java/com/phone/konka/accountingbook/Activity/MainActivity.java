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
import com.phone.konka.accountingbook.Utils.DBOperator;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.util.Calendar;

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
     * 数据库操作类
     */
    private DBOperator mDBOperator;


    /**
     * 日历
     */
    private Calendar mCalendar;


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;

    private double monthOut;
    private double monthIn;
    private double dayOut;
    private double leastOut;
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

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_main_bar);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll.getLayoutParams();
            lp.height = getStatusBarHeight();
            ll.setLayoutParams(lp);
        }
    }

    private int getStatusBarHeight() {

        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromDB();
    }


    private void getDataFromDB() {

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                monthIn = mDBOperator.getMonthIn(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
                monthOut = mDBOperator.getMonthOut(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
                dayOut = mDBOperator.getDayOut(mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.DAY_OF_MONTH));
                leastOut = mDBOperator.getLeastOut();
                weekOut = mDBOperator.getWeekOut();
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private void initView() {

        mTvMonthOut = (TextView) findViewById(R.id.tv_main_moonOut);
        mTvMonthIn = (TextView) findViewById(R.id.tv_main_moonIn);
        mTvMonthLeft = (TextView) findViewById(R.id.tv_main_moonLeft);

        mTvLeastOut = (TextView) findViewById(R.id.tv_main_leastOut);
        mTvTodayOut = (TextView) findViewById(R.id.tv_main_todayOut);
        mTvWeekOut = (TextView) findViewById(R.id.tv_main_weekOut);

    }


    private void initData() {
        mThreadPool = ThreadPoolManager.getInstance();

        mCalendar = Calendar.getInstance();

        mDBOperator = new DBOperator(this);
    }


    private void initEven() {
        findViewById(R.id.ll_main_detail).setOnClickListener(this);
        findViewById(R.id.img_main_addAccount).setOnClickListener(this);
        findViewById(R.id.img_main_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.img_main_setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_main_detail:
                intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                break;
            case R.id.img_main_addAccount:
                intent = new Intent(MainActivity.this, AddAccountActivity.class);
                startActivity(intent);
                break;

        }
    }


}
