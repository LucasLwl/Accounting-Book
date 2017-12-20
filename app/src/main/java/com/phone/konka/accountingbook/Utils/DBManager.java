package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.phone.konka.accountingbook.DataBase.DBHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 廖伟龙 on 2017/11/30.
 */

public class DBManager {


    /**
     * 记录数据库打开的次数
     */
    private AtomicInteger mOpenCount = new AtomicInteger();


    /**
     * 数据库帮助类
     */
    private static DBHelper mHelper;


    /**
     * 该类的私有对象
     */
    private static DBManager mInstance;


    /**
     * 数据库
     */
    private SQLiteDatabase mDataBase;

    private DBManager() {
    }


    /**
     * 初始化方法
     *
     * @param context
     */
    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBManager();
            mHelper = DBHelper.getInstance(context);
        }
    }

    /**
     * 获取当前类的对象
     *
     * @param context
     * @return
     */
    public static synchronized DBManager getInstance(Context context) {
        if (mInstance == null)
            initializeInstance(context);
        return mInstance;
    }


    /**
     * 以可写方式打开数据库
     *
     * @return
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mOpenCount.incrementAndGet() == 1) {
            mDataBase = mHelper.getWritableDatabase();
        }
        return mDataBase;
    }


    /**
     * 以可读方式打开数据库
     *
     * @return
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mOpenCount.incrementAndGet() == 1) {
            mDataBase = mHelper.getReadableDatabase();
        }
        return mDataBase;
    }


    /**
     * 关闭数据库
     */
    public synchronized void closeDatabase() {

        if (mOpenCount.decrementAndGet() == 0) {
            mDataBase.close();
        }
    }
}



