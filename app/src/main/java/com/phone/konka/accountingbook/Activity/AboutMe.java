package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.phone.konka.accountingbook.Base.Config;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Service.UpdateService;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

/**
 * 关于我们Activity
 * <p>
 * Created by 廖伟龙 on 2017/12/6.
 */

public class AboutMe extends Activity implements View.OnClickListener {


    private ThreadPoolManager mThreadPool;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

//        设置沉浸式状态栏
        initState();

        initData();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getAction() != null) {
            Log.i("ddd", getIntent().getAction());
        }
        Log.i("ddd", "onResume");
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
     * 初始化数据
     */
    private void initData() {
        mThreadPool = ThreadPoolManager.getInstance();
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        findViewById(R.id.img_aboutMe_back).setOnClickListener(this);
        findViewById(R.id.tv_aboutMe_update).setOnClickListener(this);
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

            case R.id.tv_aboutMe_update:

                if (Config.serverVersion > Config.localVersion) {
                    AlertDialog.Builder build = new AlertDialog.Builder(this)
                            .setMessage("检测到新版本")
                            .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent service = new Intent(AboutMe.this, UpdateService.class);
                                    AboutMe.this.startService(service);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    build.show();
                } else {
                    new AlertDialog.Builder(this).setMessage("当前已是最新版本").show();
                }
                break;
        }
    }
}
