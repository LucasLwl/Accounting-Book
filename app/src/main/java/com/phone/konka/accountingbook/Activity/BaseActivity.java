package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/12/15.
 */

public abstract class BaseActivity extends Activity {

    private LinearLayout ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        ll = (LinearLayout) findViewById(R.id.ll_base_bar);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        ll.addView(LayoutInflater.from(this).inflate(layoutResID, null));
        super.setContentView(ll);
    }
}
