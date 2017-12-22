package com.phone.konka.accountingbook.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.phone.konka.accountingbook.Activity.AboutMe;
import com.phone.konka.accountingbook.Base.Config;
import com.phone.konka.accountingbook.R;
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


    private static final int MESSAGE_INIT_FILE = 0;


    /**
     * 通知更新进度信息
     */
    private static final int MESSAGE_UPDATE_PROGRESS = 1;


    /**
     * 通知下载完成信息
     */
    private static final int MESSAGE_DOWNLOAD_FINISHED = 2;


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


    private DownloadTask mDownloadTask;


    /**
     * 是否暂停下载
     */
    private boolean isPause = false;


    /**
     * 是否终止下载
     */
    private boolean isStop = true;


    private int mContentLength = 0;


    private int mFinishedLength = 0;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case MESSAGE_INIT_FILE:
                    mThreadPool.execute(new DownloadTask());

                    break;

                case MESSAGE_UPDATE_PROGRESS:
                    if (mBuilder != null) {
                        mBuilder.setContentText(mFinishedLength / 1024 + "M/" + mContentLength / 1024 + "M");
                        mBuilder.setProgress(100, mFinishedLength * 100 / mContentLength, false);
                        mNotificationManager.notify(notifyID, mBuilder.build());
                    }
                    break;

                case MESSAGE_DOWNLOAD_FINISHED:
                    isStop = true;
                    break;

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mThreadPool = ThreadPoolManager.getInstance();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mUpdateIntent = new Intent(this, AboutMe.class);
        mUpdateIntent.setAction(ACTION_STOP_DOWNLOAD);
        mUpdatePendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, 0);

        mDownloadTask = new DownloadTask();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        开始下载
        if (intent.getAction().equals(ACTION_START_DOWNLOAD)) {

//            初始化状态栏通知
            if (mBuilder == null) {
                mBuilder = new NotificationCompat.Builder(this);
                mBuilder.setSmallIcon(R.drawable.icon_application);
                mBuilder.setContentTitle("记账本");
                mBuilder.setContentText("0%");
                mBuilder.setTicker("开始下载");
                mBuilder.setContentIntent(mUpdatePendingIntent);
                mBuilder.setProgress(100, 0, false);
                mBuilder.setOngoing(true);
            }

//            判断是否已经开始下载，若无，则开始下载
            if (isStop || isPause) {
                isStop = false;
                mNotificationManager.notify(notifyID, mBuilder.build());
//                mThreadPool.execute(mDownloadTask);
                mThreadPool.execute(new InitFileTask());
            }

//            else if (isPause) {
//                isPause = false;
//            }

//            暂停下载
        } else if (intent.getAction().equals(ACTION_PAUSE_DOWNLOAD)) {
            if (!isPause) {
                isPause = true;
            }

//            终止下载
        } else if (intent.getAction().equals(ACTION_STOP_DOWNLOAD)) {
            if (!isStop) {
                isStop = true;
                mFinishedLength = 0;
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

            HttpURLConnection conn = null;

            try {
//                安装包链接
                URL url = new URL(Config.URL_PATH);
                conn = (HttpURLConnection) url.openConnection();

//                设置连接超时
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestProperty("Range", "bytes=" + mFinishedLength + "-" + mContentLength);
                conn.setUseCaches(false);

////                已下载的bytes数
//                int finished = 0;


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


                    if (isPause || isStop)
                        break;

//                    if (isPause) {
//                    }
//
////                  判断是否停止下载
//                    if (isStop) {
//
////                        关闭状态栏通知
//                        mNotificationManager.cancel(notifyID);
////                        删除下载的安装包文件
//                        file.delete();
//                        break;
//                    }
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
                Log.i("ddd", e.toString());
            } finally {
                conn.disconnect();
            }

        }
    }


    class InitFileTask implements Runnable {
        @Override
        public void run() {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
