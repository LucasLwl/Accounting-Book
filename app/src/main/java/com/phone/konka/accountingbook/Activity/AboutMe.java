package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.phone.konka.accountingbook.R;

/**
 * 关于我们Activity
 * <p>
 * Created by 廖伟龙 on 2017/12/6.
 */

public class AboutMe extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

//        设置沉浸式状态栏
        initState();

        initEvent();
    }

    /**
     * 设置沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_aboutMe_bar);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll.getLayoutParams();
            lp.height = getStatusBarHeight();
            ll.setLayoutParams(lp);
        }
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        findViewById(R.id.img_aboutMe_back).setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_aboutMe_back:
                finish();
                break;
        }
    }
}
