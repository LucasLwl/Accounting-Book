package com.phone.konka.accountingbook.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 廖伟龙 on 2017/11/24.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "account.db";

    private static final int DB_VERSION = 1;

    private static DBHelper mInstance;

    private static final String CREATE_TABLE = "create table if not exists account(_id integer primary key autoincrement," +
            "year integer,month integer,day integer,tag text,money text)";

    private static final String DROP_TABLE = "drop table if exits account";


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
