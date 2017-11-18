package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class SettingActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initEven();
    }

    private void initEven() {
        findViewById(R.id.img_setting_back).setOnClickListener(this);
        findViewById(R.id.tv_setting_inAccount).setOnClickListener(this);
        findViewById(R.id.tv_setting_outAccount).setOnClickListener(this);
        findViewById(R.id.tv_setting_aboutMe).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_setting_back:

                break;

            case R.id.tv_setting_inAccount:

                break;

            case R.id.tv_setting_outAccount:

                break;

            case R.id.tv_setting_aboutMe:

                break;

        }

    }
}
