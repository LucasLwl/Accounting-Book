package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phone.konka.accountingbook.DataBase.DBHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 廖伟龙 on 2017/11/30.
 */

public class DBManager {

    private AtomicInteger mOpenCount = new AtomicInteger();

    private static DBHelper mHelper;

    private static DBManager mInstance;

    private SQLiteDatabase mDataBase;

    private DBManager() {

    }

    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBManager();
            mHelper = DBHelper.getInstance(context);
        }
    }

    public static synchronized DBManager getInstance(Context context) {
        if (mInstance == null)
            initializeInstance(context);
        return mInstance;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mOpenCount.incrementAndGet() == 1) {
            // Opening new database
            mDataBase = mHelper.getWritableDatabase();
        }
        return mDataBase;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mOpenCount.incrementAndGet() == 1) {
            // Opening new database
            mDataBase = mHelper.getReadableDatabase();
        }
        return mDataBase;
    }

    public synchronized void closeDatabase() {

        if (mOpenCount.decrementAndGet() == 0) {
            // Closing database
            mDataBase.close();
        }
    }
}



