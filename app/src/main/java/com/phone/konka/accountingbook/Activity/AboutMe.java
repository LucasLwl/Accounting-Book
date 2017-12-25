package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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


    /**
     * 显示检查版本更新
     */
    private TextView mTvCheckUpdate;


    /**
     * 停止下载提示框
     */
    private AlertDialog mStopDownloadDialog;


    /**
     * 提示更新提示框
     */
    private AlertDialog mCheckUpdateDialog;


    /**
     * 非WIFI网络提示框
     */
    private AlertDialog mNetworkWarnDialog;


    /**
     * 信使
     */
    private Messenger mMessenger;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

//                开始下载
                case UpdateService.STATE_DOWNLOAD:
                    mTvCheckUpdate.setText("暂停下载");
                    mTvCheckUpdate.setEnabled(true);
                    break;


//                暂停下载
                case UpdateService.STATE_PAUSE:
                    mTvCheckUpdate.setText("继续下载");
                    mTvCheckUpdate.setEnabled(true);
                    break;

//                停止下载
                case UpdateService.STATE_STOP:
                    mTvCheckUpdate.setText("检查新版本");
                    mTvCheckUpdate.setEnabled(true);
                    break;


//                下载完成
                case UpdateService.STATE_FINISHED:
                    mTvCheckUpdate.setText("下载完成");
                    mTvCheckUpdate.setEnabled(false);
                    break;
            }
        }
    };

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


        Message msg = mHandler.obtainMessage();
        msg.what = UpdateService.getDownloadState();
        mHandler.sendMessage(msg);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mMessenger = new Messenger(mHandler);
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


    /**
     * 显示停止下载提示框
     */
    public void showStopDownloadDialog() {

        if (mStopDownloadDialog == null) {
            mStopDownloadDialog = new AlertDialog.Builder(this).setMessage("是否取消下载")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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


    /**
     * 显示更新提示框
     */
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


    /**
     * 显示非WIFi网络提示框
     */
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

                String str = mTvCheckUpdate.getText().toString();

                if (str.equals("检查新版本")) {
                    if (NetworkUtil.isNetworkConnected(this)) {
                        if (Config.serverVersion > Config.localVersion) {
                            showCheckUpdateDialog();
                        } else {
                            mTvCheckUpdate.setText("当前已是最新版本");
                            mTvCheckUpdate.setEnabled(false);
                            Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
                    }
                } else if (str.equals("继续下载")) {
                    if (NetworkUtil.isNetworkConnected(this)) {
                        startUpdateService(UpdateService.ACTION_START_DOWNLOAD);
                    } else {
                        Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
                    }
                } else if (str.equals("暂停下载")) {
                    startUpdateService(UpdateService.ACTION_PAUSE_DOWNLOAD);
                }
                break;
        }
    }

    /**
     * 开启更新sercive
     *
     * @param action 控制service的下载状态
     */
    private void startUpdateService(String action) {
        Intent service = new Intent(this, UpdateService.class);
        service.setAction(action);
        service.putExtra("messenger", mMessenger);
        startService(service);
    }
}
