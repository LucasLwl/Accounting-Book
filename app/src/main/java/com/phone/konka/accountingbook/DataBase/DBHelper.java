package com.phone.konka.accountingbook.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * <p>
 * Created by 廖伟龙 on 2017/11/24.
 */

public class DBHelper extends SQLiteOpenHelper {


    /**
     * 数据库名称
     */
    private static final String DB_NAME = "account.db";


    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;


    /**
     * 当前类的私对象
     */
    private static DBHelper mInstance;


    /**
     * 创建数据表语句
     */
    private static final String CREATE_TABLE = "create table if not exists account(_id integer primary key autoincrement," +
            "year integer,month integer,day integer,tag text, iconID integer,money text)";


    /**
     * 删除数据表语句
     */
    private static final String DROP_TABLE = "drop table if exits account";


    /**
     * 获取当前类的对象
     *
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBHelper.class) {
                if (mInstance == null) {
                    mInstance = new DBHelper(context);
                }
            }
        }
        return mInstance;
    }


    /**
     * 私有构造方法
     *
     * @param context
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }
}
