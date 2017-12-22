package com.phone.konka.accountingbook.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.phone.konka.accountingbook.Activity.AboutMe;
import com.phone.konka.accountingbook.Base.Config;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.NetworkUtil;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

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


    private static final int MESSAGE_STOP_DOWNLOAD = 2;


    /**
     * 通知下载完成信息
     */
    private static final int MESSAGE_DOWNLOAD_FINISHED = 3;


    private static final int STATE_DOWNLOAD = 0;
    private static final int STATE_PAUSE = 1;
    private static final int STATE_STOP = 2;


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


    private int mContentLength = 0;


    private int mFinishedLength = 0;


    private int mDownloadState = -1;

    private BroadcastReceiver mNetworkReceiver;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case MESSAGE_INIT_FILE:
                    Log.i("ddd", "开始下载");
                    mThreadPool.execute(new DownloadTask());
                    mNotificationManager.notify(notifyID, mBuilder.build());

                    break;

                case MESSAGE_UPDATE_PROGRESS:
                    if (mBuilder != null) {
                        mBuilder.setContentText(mFinishedLength / 1024 + "M/" + mContentLength / 1024 + "M");
                        mBuilder.setProgress(100, mFinishedLength * 100 / mContentLength, false);
                        mNotificationManager.notify(notifyID, mBuilder.build());
                    }
                    break;


                case MESSAGE_STOP_DOWNLOAD:
                    mFinishedLength = 0;
                    mNotificationManager.cancel(notifyID);
                    File file = new File(APK_DIR, APK_NAME);
                    file.delete();
                    break;

                case MESSAGE_DOWNLOAD_FINISHED:
                    mDownloadState = STATE_STOP;
                    Log.i("ddd", "下载完成");
                    Toast.makeText(UpdateService.this, "下载完成", Toast.LENGTH_SHORT).show();
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
    }

    private void initEven() {

        mNetworkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkReceiver, filter);
    }


    private void initData() {


        mThreadPool = ThreadPoolManager.getInstance();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mUpdateIntent = new Intent(this, AboutMe.class);
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


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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

                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(mFinishedLength);


//                记录开始时间，每个一秒更新一次进度
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


                    if (mDownloadState == STATE_STOP) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = MESSAGE_STOP_DOWNLOAD;
                        mHandler.sendMessage(msg);
                        break;
                    }

                    if (mDownloadState == STATE_PAUSE)
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
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ddd", "DownloadTask" + e.toString());
            } finally {
                conn.disconnect();
            }

        }
    }


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

    private void startDownloadTask() {
        mDownloadState = STATE_DOWNLOAD;
        mThreadPool.execute(new InitFileTask());
    }

    private void pauseDownloadTask() {
        mDownloadState = STATE_PAUSE;
        Toast.makeText(this, "当前为非WIFI环境，暂停下载", Toast.LENGTH_SHORT).show();
    }

    private void stopDownloadTask() {
        mDownloadState = STATE_STOP;
        Toast.makeText(UpdateService.this, "终止下载", Toast.LENGTH_SHORT).show();
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = NetworkUtil.getConnectedType(context);


            switch (type) {
                case ConnectivityManager.TYPE_WIFI:
                    if (mDownloadState == STATE_PAUSE) {
                        Log.i("ddd", "尝试重连");
//                        startUpdateService(UpdateService.ACTION_START_DOWNLOAD);
                        startDownloadTask();
                    }
                    Log.i("ddd", type + "TYPE_WIFI");
                    break;

                default:
                    if (mDownloadState == STATE_DOWNLOAD) {
                        pauseDownloadTask();
                    }
                    Log.i("ddd", type + "default");
                    break;
            }
        }
    }
}
