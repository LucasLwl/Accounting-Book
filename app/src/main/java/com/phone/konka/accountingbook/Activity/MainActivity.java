package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBManager;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;

import java.util.Calendar;

public class MainActivity extends Activity implements View.OnClickListener {


    private TextView mTvMoonOut;
    private TextView mTvMoonIn;
    private TextView mTvMoonLeft;


    private TextView mTvLeastOut;
    private TextView mTvTodayOut;
    private TextView mTvWeekOut;

    private DBManager mDBManager;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEven();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        mCalendar = Calendar.getInstance();

        mDBManager = new DBManager(this);
        double in = mDBManager.getMonthIn(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
        double out = mDBManager.getMonthOut(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
        mTvMoonIn.setText(DoubleTo2Decimal.doubleTo2Decimal(in));
        mTvMoonOut.setText(DoubleTo2Decimal.doubleTo2Decimal(out));
        mTvMoonLeft.setText((DoubleTo2Decimal.doubleTo2Decimal(in - out)));
        mTvTodayOut.setText(DoubleTo2Decimal.doubleTo2Decimal(mDBManager.getDayOut(mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.DAY_OF_MONTH))));
        mTvLeastOut.setText(DoubleTo2Decimal.doubleTo2Decimal(mDBManager.getLeastOut()));
        mTvWeekOut.setText(DoubleTo2Decimal.doubleTo2Decimal(mDBManager.getWeekOut()));

        Log.i("ddd", "DAY_OF_WEEK: " + mCalendar.get(Calendar.DAY_OF_WEEK));
        Log.i("ddd", "getFirstDayOfWeek: " + mCalendar.getFirstDayOfWeek() + "");
    }


    private void initView() {

        mTvMoonOut = (TextView) findViewById(R.id.tv_main_moonOut);
        mTvMoonIn = (TextView) findViewById(R.id.tv_main_moonIn);
        mTvMoonLeft = (TextView) findViewById(R.id.tv_main_moonLeft);

        mTvLeastOut = (TextView) findViewById(R.id.tv_main_leastOut);
        mTvTodayOut = (TextView) findViewById(R.id.tv_main_todayOut);
        mTvWeekOut = (TextView) findViewById(R.id.tv_main_weekOut);
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
