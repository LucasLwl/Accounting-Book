package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.DataBase.DBHelper;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/24.
 */

public class DBManager {

    private SQLiteOpenHelper mHelper;
    private SQLiteDatabase mDataBase;
    private Context mContext;

    private Calendar mCalendar;

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
        mDataBase.execSQL("insert into account(year,month,day,tag,money) values(?,?,?,?,?)",
                new Object[]{bean.getYear(), bean.getMoon(), bean.getDay(), bean.getTag(), bean.getMoney()});
        mDataBase.close();
    }


    public double getMoonIn(int year, int moon) {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select * from account where year = ? and month = ? and money > ?", new String[]{year + "", moon + "", 0 + ""});
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        return res;
    }

    public double getMoonOut(int year, int moon) {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select * from account where year = ? and month = ? and money < ?", new String[]{year + "", moon + "", 0 + ""});
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        if (res == 0)
            return res;
        return -res;
    }

    public double getDayOut(int year, int moon, int day) {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select * from account where year = ? and month = ? and day = ? and money < ?",
                new String[]{year + "", moon + "", day + "", 0 + ""});
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        if (res == 0)
            return res;
        return -res;
    }


    public double getLeastOut() {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select * from account where  money < ? order  by _id desc",
                new String[]{0 + ""});
        if (cursor.moveToNext()) {
            res = cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        if (res == 0)
            return res;
        return -res;
    }

    public double getWeekOut() {

        double res = 0;

        return res;
    }

    public static List<Date> dateToCurrentWeek(Date mdate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mdate);

        int b = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (b == 0) {
            b = 7;
        }
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a - 1, fdate);
        }
        return list;
    }
}
