package com.phone.konka.accountingbook.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class UpdateService extends Service {


    private static final String PATH = Environment.getExternalStorageDirectory() + "/Accounting";


    private ThreadPoolManager mThreadPool;


    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mBuilder;

    private static final int notifyID = 1;

    private PendingIntent mUpdatePendingIntent;

    private Intent mUpdateIntent;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                mBuilder.setContentText((msg.arg1) / 1024 + "M/" + msg.arg2 / 1024 + "M");
                mBuilder.setProgress(100, msg.arg1 * 100 / msg.arg2, false);
                mNotificationManager.notify(notifyID, mBuilder.build());
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mThreadPool = ThreadPoolManager.getInstance();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mUpdateIntent = new Intent(this, AboutMe.class);
        mUpdateIntent.setAction("action");
        mUpdatePendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, 0);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.icon_application);
            mBuilder.setContentTitle("记账本");
            mBuilder.setContentText("0%");
            mBuilder.setTicker("开始下载");
            mBuilder.setContentIntent(mUpdatePendingIntent);
            mBuilder.setProgress(100, 0, false);

            mNotificationManager.notify(notifyID, mBuilder.build());


            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(Config.URL_PATH);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5 * 1000);
                        conn.setUseCaches(false);

                        int contentLength = conn.getContentLength();
                        int lenght = -1;
                        int finished = 0;
                        byte[] buffer = new byte[1024 * 4];
                        InputStream is = conn.getInputStream();

                        File dir = new File(PATH);
                        if (!dir.exists())
                            dir.mkdirs();
                        File file = new File(dir, "test.jpg");

                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;

                        FileOutputStream fos = new FileOutputStream(file);
                        long startTime = System.currentTimeMillis();
                        while ((lenght = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, lenght);
                            finished += lenght;
                            if (System.currentTimeMillis() - startTime > 1000) {
                                startTime = System.currentTimeMillis();

                                msg.arg1 = finished;
                                msg.arg2 = contentLength;
                                msg.sendToTarget();
                            }
                        }
                        msg.arg1 = contentLength;
                        msg.arg2 = contentLength;
                        msg.sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
