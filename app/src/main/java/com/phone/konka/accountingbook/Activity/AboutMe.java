package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/12/6.
 */

public class AboutMe extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.img_aboutMe_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_aboutMe_back:
                finish();
                break;
        }
    }
}
