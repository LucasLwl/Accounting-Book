package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.DataBase.DBHelper;

/**
 * Created by 廖伟龙 on 2017/11/24.
 */

public class DBManager {

    private SQLiteOpenHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Context mContext;

    public DBManager(Context mContext) {
        this.mContext = mContext;
        mHelper = DBHelper.getInstance(mContext);
    }


    public boolean isDBEmpty() {
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select * from account", new String[]{});
        mDataBase.close();
        if (cursor.moveToNext())
            return false;
        return true;
    }


    public void insertAccount(DetailTagBean bean) {
        mDataBase = mHelper.getWritableDatabase();
        mDataBase.execSQL("insert into account(year,moon,day,tag,money) values(?,?,?,?,?)",
                new Object[]{bean.getYear(),bean.getMoon(),bean.getDay(),bean.getTag(),bean.getMoney()});


    }
}
