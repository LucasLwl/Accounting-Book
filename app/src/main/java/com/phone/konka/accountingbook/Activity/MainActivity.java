package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.R;

public class MainActivity extends Activity implements View.OnClickListener {


    private TextView mTvMoonOut;
    private TextView mTvMoonIn;
    private TextView mTvMoonLeft;


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

        mTvLeastOut = (TextView) findViewById(R.id.tv_main_moonOut);
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
