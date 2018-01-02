package com.phone.konka.accountingbook.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.phone.konka.accountingbook.Activity.AboutMeActivity;
import com.phone.konka.accountingbook.Base.Config;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.NetworkUtil;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import javax.net.ssl.SSLException;

/**
 * 下载更新包Service
 * <p>
 * Created by 廖伟龙 on 2017/12/21.
 */

public class UpdateService extends Service {


    /**
     * 开始下载action
     */
    public static final String ACTION_START_DOWNLOAD = "START_DOWNLOAD";


    /**
     * 暂停下载action
     */
    public static final String ACTION_PAUSE_DOWNLOAD = "PAUSE_DOWNLOAD";


    /**
     * 终止下载action
     */
    public static final String ACTION_STOP_DOWNLOAD = "STOP_DOWNLOAD";


    /**
     * 通知初始化File信息
     */
    private static final int MESSAGE_INIT_FILE = 0;


    /**
     * 通知更新进度信息
     */
    private static final int MESSAGE_UPDATE_PROGRESS = 1;


    /**
     * 停止下载信息
     */
    private static final int MESSAGE_STOP_DOWNLOAD = 2;


    /**
     * 通知下载完成信息
     */
    private static final int MESSAGE_DOWNLOAD_FINISHED = 3;


    /**
     * 下载状态
     */
    public static final int STATE_DOWNLOAD = 0;


    /**
     * 暂停状态
     */
    public static final int STATE_PAUSE = 1;


    /**
     * 停止下载状态
     */
    public static final int STATE_STOP = 2;


    /**
     * 下载完成状态
     */
    public static final int STATE_FINISHED = 3;


    /**
     * 安装包下载路径
     */
    private static final String APK_DIR = Config.BASE_DIR + "/APK";


    /**
     * 安装包名字
     */
    private static final String APK_NAME = "AccountingBook.apk";


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;


    /**
     * 状态栏通知管理类
     */
    private NotificationManager mNotificationManager;


    /**
     * 状态栏通知
     */
    private NotificationCompat.Builder mBuilder;


    /**
     * 状态栏通知显示的ID
     */
    private static final int notifyID = 1;


    /**
     * 状态栏点击Pending意图
     */
    private PendingIntent mUpdatePendingIntent;


    /**
     * 状态栏点击意图
     */
    private Intent mUpdateIntent;


    /**
     * 安装包总大小
     */
    private int mContentLength = 0;


    /**
     * 上一秒完成的大小
     */
    private int mLastFinishedLength = 0;


    /**
     * 当前完成的大小
     */
    private int mFinishedLength = 0;


    /**
     * 当前的下载状态
     */
    private static int mDownloadState = STATE_STOP;


    /**
     * 网络状态广播接收者
     */
    private BroadcastReceiver mNetworkReceiver;


    /**
     * Activity的信使
     */
    private Messenger mActivityMessenger;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


//            安装包文件
            File file;


//            格式化到小数点后一位，用于格式化下载速度
            DecimalFormat format = new DecimalFormat("0.0");


            switch (msg.what) {

//                初始化完成
                case MESSAGE_INIT_FILE:

//                    开始正式下载
                    mThreadPool.execute(new DownloadTask());
                    if (mBuilder != null) {
                        mNotificationManager.notify(notifyID, mBuilder.build());
                    }
                    break;


//                更新下载进度
                case MESSAGE_UPDATE_PROGRESS:

//                    计算下载速度，并格式化
                    int speech = mFinishedLength - mLastFinishedLength;
                    mLastFinishedLength = mFinishedLength;
                    String strSpeech;
                    if (speech >= 1024 * 1024) {
                        strSpeech = format.format(speech / (1024d * 1024d)) + " m/s";
                    } else if (speech >= 1024) {
                        strSpeech = speech / 1024 + " kb/s";
                    } else {
                        strSpeech = speech + " b/s";
                    }

//                    显示下载进度
                    if (mBuilder != null) {
                        mBuilder.setContentText(format.format(mFinishedLength / (1024d * 1024d)) + "M/" +
                                format.format(mContentLength / (1024d * 1024d)) + "M" + "       " + strSpeech);
                        mBuilder.setProgress(100, (int) ((100d / mContentLength) * mFinishedLength), false);
                        mNotificationManager.notify(notifyID, mBuilder.build());
                    }
                    break;

//                停止下载
                case MESSAGE_STOP_DOWNLOAD:
                    mFinishedLength = 0;
                    mLastFinishedLength = 0;
                    mNotificationManager.cancel(notifyID);

//                    删除安装包文件
                    file = new File(APK_DIR, APK_NAME);
                    file.delete();
                    sendStateToActivity();
                    break;


//                下载完成
                case MESSAGE_DOWNLOAD_FINISHED:
                    mDownloadState = STATE_FINISHED;
                    mFinishedLength = 0;
                    mLastFinishedLength = 0;
                    mNotificationManager.cancel(notifyID);

                    sendStateToActivity();

                    if (mBuilder != null) {
                        file = new File(APK_DIR, APK_NAME);
                        Uri uri = Uri.fromFile(file);


//                        开启系统安装功能
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                        mUpdatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);

                        mBuilder.setAutoCancel(true);
                        mBuilder.setTicker("下载完成");
                        mBuilder.setContentText("下载完成，点击安装");
                        mBuilder.setProgress(100, 100, true);
                        mBuilder.setContentIntent(mUpdatePendingIntent);
                        mNotificationManager.notify(notifyID, mBuilder.build());
                    }

                    Log.i("ddd", "下载完成");

                    stopSelf();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        initData();
        initEven();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
        Log.i("ddd", "onDestroy");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        获取Activity的信使
        mActivityMessenger = intent.getParcelableExtra("messenger");


//        开始下载
        if (intent.getAction().equals(ACTION_START_DOWNLOAD)) {

//            判断是否已经开始下载，若无，则开始下载
            if (mDownloadState != STATE_DOWNLOAD) {
                startDownloadTask();
            }

//            暂停下载
        } else if (intent.getAction().equals(ACTION_PAUSE_DOWNLOAD)) {
            if (mDownloadState == STATE_DOWNLOAD)
                pauseDownloadTask();

//            终止下载
        } else if (intent.getAction().equals(ACTION_STOP_DOWNLOAD)) {
            if (mDownloadState != STATE_STOP) {
                stopDownloadTask();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void initData() {


        mThreadPool = ThreadPoolManager.getInstance();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mUpdateIntent = new Intent(this, AboutMeActivity.class);
        mUpdateIntent.setAction(ACTION_STOP_DOWNLOAD);
        mUpdatePendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, 0);

        //初始化状态栏通知
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_application)
                .setContentTitle("记账本")
                .setContentText("0%")
                .setTicker("开始下载")
                .setContentIntent(mUpdatePendingIntent)
                .setProgress(100, 0, false)
                .setOngoing(true);
    }

    private void initEven() {

//        注册网络状态广播
        mNetworkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkReceiver, filter);
    }


    /**
     * 下载安装包
     */
    class DownloadTask implements Runnable {
        @Override
        public void run() {


            Log.i("ddd", "DownloadTask");

            HttpURLConnection conn = null;

            try {
//                安装包链接
                URL url = new URL(Config.URL_PATH);
                conn = (HttpURLConnection) url.openConnection();

//                设置连接超时
                conn.setConnectTimeout(5 * 1000);

//                设置下载范围，用于断点下载
                conn.setRequestProperty("Range", "bytes=" + mFinishedLength + "-" + mContentLength);
                conn.setUseCaches(false);


                int lenght = -1;
                byte[] buffer = new byte[1024 * 4];

                InputStream is = conn.getInputStream();

//                安装包保存的路径
                File dir = new File(APK_DIR);
                if (!dir.exists())
                    dir.mkdirs();

//                安装包保存的文件
                File file = new File(dir, APK_NAME);

//                下载文件保存的位置,用于断点下载
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(mFinishedLength);


//                记录开始时间，每秒更新一次进度
                long startTime = System.currentTimeMillis();

                while ((lenght = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, lenght);
                    mFinishedLength += lenght;
                    if (System.currentTimeMillis() - startTime >= 1000) {
                        startTime = System.currentTimeMillis();
                        Message msg = mHandler.obtainMessage();
                        msg.what = MESSAGE_UPDATE_PROGRESS;
                        mHandler.sendMessage(msg);
                    }

                    if (mDownloadState != STATE_DOWNLOAD)
                        break;
                }

//              下载完成的通知
                if (mFinishedLength == mContentLength) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MESSAGE_DOWNLOAD_FINISHED;
                    mHandler.sendMessage(msg);
                }
                is.close();
                raf.close();
            } catch (SSLException e) {
                Log.i("ddd", "DownloadTask:  SSLException:    " + e.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ddd", "DownloadTask" + e.toString());
            } finally {
                conn.disconnect();
            }

        }
    }


    /**
     * 初始化下载大小
     */
    class InitFileTask implements Runnable {
        @Override
        public void run() {

            Log.i("ddd", "InitFileTask");

            try {
                URL url = new URL(Config.URL_PATH);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setUseCaches(false);


                mContentLength = conn.getContentLength();

                //安装包保存的路径
                File dir = new File(APK_DIR);
                if (!dir.exists())
                    dir.mkdirs();
                //安装包保存的文件
                File file = new File(dir, APK_NAME);
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.setLength(mContentLength);

                Message msg = mHandler.obtainMessage();
                msg.what = MESSAGE_INIT_FILE;
                mHandler.sendMessage(msg);
                raf.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ddd", "InitFileTask" + e.toString());
            }
        }
    }

    /**
     * 开始下载
     */
    private void startDownloadTask() {
        mDownloadState = STATE_DOWNLOAD;
        sendStateToActivity();
        mThreadPool.execute(new InitFileTask());
    }


    /**
     * 暂停下载
     */
    private void pauseDownloadTask() {
        mDownloadState = STATE_PAUSE;
        mBuilder.setContentText("暂停下载");
        mNotificationManager.notify(notifyID, mBuilder.build());
        sendStateToActivity();
    }


    /**
     * 停止下载
     */
    private void stopDownloadTask() {
        mDownloadState = STATE_STOP;
        Message msg = mHandler.obtainMessage();
        msg.what = MESSAGE_STOP_DOWNLOAD;
        mHandler.sendMessage(msg);
    }


    /**
     * 发送下载状态到Activity
     */
    private void sendStateToActivity() {
        Message msg = Message.obtain();
        msg.what = mDownloadState;
        try {
            mActivityMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取下载状态
     *
     * @return
     */
    public static int getDownloadState() {
        return mDownloadState;
    }


    /**
     * 手机网络状态接收器
     */
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = NetworkUtil.getConnectedType(context);


            switch (type) {
                case ConnectivityManager.TYPE_WIFI:
                    if (mDownloadState == STATE_PAUSE) {
                        Log.i("ddd", "尝试重连");
                        startDownloadTask();
                    }
                    Log.i("ddd", type + "TYPE_WIFI");
                    break;

                default:
                    if (mDownloadState == STATE_DOWNLOAD) {
                        pauseDownloadTask();
                        Toast.makeText(UpdateService.this, "当前为非WIFI环境，暂停下载", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("ddd", type + "default");
                    break;
            }
        }
    }

    public static class InstallPackageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String packageName = intent.getDataString();
            Log.i("ddd", packageName);
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) ||
                    intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                if (packageName.contains("qq")) {
                    File file = new File(APK_DIR, APK_NAME);
                    file.delete();
                }
            }
        }
    }
}


