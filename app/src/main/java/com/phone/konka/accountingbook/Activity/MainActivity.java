package com.phone.konka.accountingbook.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mImgSetting;
    private LinearLayout mLlDetail;
    private ImageView mImgAddAccount;
    private TextView mTvLeastOut;
    private TextView mTvTodayOut;
    private TextView mTvWeekOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEven();
    }


    private void initView() {
        mImgSetting = (ImageView) findViewById(R.id.img_main_setting);
        mLlDetail = (LinearLayout) findViewById(R.id.ll_main_detail);
        mImgAddAccount = (ImageView) findViewById(R.id.img_main_addAccount);
        mTvLeastOut = (TextView) findViewById(R.id.tv_main_leastOut);
        mTvTodayOut = (TextView) findViewById(R.id.tv_main_todayOut);
        mTvWeekOut = (TextView) findViewById(R.id.tv_main_weekOut);
    }


    private void initEven() {

        mLlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
