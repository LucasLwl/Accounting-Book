package com.phone.konka.accountingbook.Utils;

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

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (mInstance == null) {
            mInstance = new DBManager();
            mHelper = (DBHelper) helper;
        }
    }

    public static synchronized DBManager getInstance(SQLiteOpenHelper helper) {
        if (mInstance == null)
            initializeInstance(helper);
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



