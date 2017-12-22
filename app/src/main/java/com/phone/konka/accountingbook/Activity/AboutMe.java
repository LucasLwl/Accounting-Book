package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phone.konka.accountingbook.Base.Config;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Service.UpdateService;
import com.phone.konka.accountingbook.Utils.NetworkUtil;

/**
 * 关于我们Activity
 * <p>
 * Created by 廖伟龙 on 2017/12/6.
 */

public class AboutMe extends Activity implements View.OnClickListener {


    private TextView mTvCheckUpdate;

    private AlertDialog mStopDownloadDialog;

    private AlertDialog mCheckUpdateDialog;

    private AlertDialog mNetworkWarnDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

//        设置沉浸式状态栏
        initState();

        initView();

        initData();

        initEvent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getAction() != null && getIntent().getAction().equals(UpdateService.ACTION_STOP_DOWNLOAD)) {
            showStopDownloadDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction() != null && intent.getAction().equals(UpdateService.ACTION_STOP_DOWNLOAD)) {
            showStopDownloadDialog();
        }
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
     * 初始化View
     */
    private void initView() {
        mTvCheckUpdate = (TextView) findViewById(R.id.tv_aboutMe_update);
    }


    /**
     * 初始化数据
     */
    private void initData() {
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        findViewById(R.id.img_aboutMe_back).setOnClickListener(this);
        mTvCheckUpdate.setOnClickListener(this);
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


    public void showStopDownloadDialog() {

        if (mStopDownloadDialog == null) {
            mStopDownloadDialog = new AlertDialog.Builder(this).setMessage("是否取消下载")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTvCheckUpdate.setEnabled(true);
                            startUpdateService(UpdateService.ACTION_STOP_DOWNLOAD);
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }

        if (!mStopDownloadDialog.isShowing())
            mStopDownloadDialog.show();

    }


    public void showCheckUpdateDialog() {
        if (mCheckUpdateDialog == null) {
            mCheckUpdateDialog = new AlertDialog.Builder(this)
                    .setMessage("检测到新版本")
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (NetworkUtil.getConnectedType(AboutMe.this) != ConnectivityManager.TYPE_WIFI) {
                                showNetworkWarnDialog();
                            } else {
                                mTvCheckUpdate.setEnabled(false);
                                startUpdateService(UpdateService.ACTION_START_DOWNLOAD);
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }
        if (!mCheckUpdateDialog.isShowing())
            mCheckUpdateDialog.show();
    }


    public void showNetworkWarnDialog() {
        if (mNetworkWarnDialog == null) {
            mNetworkWarnDialog = new AlertDialog.Builder(AboutMe.this)
                    .setTitle("流量提醒")
                    .setMessage("当前网络为非WIFI环境，是否继续使用手机流量下载?")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTvCheckUpdate.setEnabled(false);
                            startUpdateService(UpdateService.ACTION_START_DOWNLOAD);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }
        if (!mNetworkWarnDialog.isShowing())
            mNetworkWarnDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_aboutMe_back:
                finish();
                break;

            case R.id.tv_aboutMe_update:
                if (NetworkUtil.isNetworkConnected(this)) {
                    if (Config.serverVersion > Config.localVersion) {
                        showCheckUpdateDialog();
                    } else {
                        Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startUpdateService(String action) {
        Intent service = new Intent(this, UpdateService.class);
        service.setAction(action);
        startService(service);
    }
}
