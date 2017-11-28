package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.Bean.MonthDetailBean;
import com.phone.konka.accountingbook.DataBase.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 数据库管理类
 * <p>
 * Created by 廖伟龙 on 2017/11/24.
 */

public class DBManager {


    private SQLiteOpenHelper mHelper;

    /**
     * 数据库
     */
    private SQLiteDatabase mDataBase;
    private Context mContext;


    /**
     * 日历类
     */
    private Calendar mCalendar;

    public DBManager(Context mContext) {
        this.mContext = mContext;
        mHelper = DBHelper.getInstance(mContext);
    }


    /**
     * 判断数据库是否为空
     *
     * @return
     */
    public boolean isDBEmpty() {
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select money from account", new String[]{});
        mDataBase.close();
        if (cursor.moveToNext())
            return false;
        return true;
    }


    /**
     * 添加账单
     *
     * @param bean
     */
    public void insertAccount(DetailTagBean bean) {
        mDataBase = mHelper.getWritableDatabase();
        mDataBase.execSQL("insert into account(year,month,day,tag,money) values(?,?,?,?,?)",
                new Object[]{bean.getYear(), bean.getMoon(), bean.getDay(), bean.getTag(), bean.getMoney()});
        mDataBase.close();
    }


    /**
     * 获取月份的总收入
     *
     * @param year
     * @param month
     * @return
     */
    public double getMonthIn(int year, int month) {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select money from account where year = ? and month = ? and money > ?", new String[]{year + "", month + "", 0 + ""});
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        return res;
    }

    /**
     * 获取月份的总支出
     *
     * @param year
     * @param moon
     * @return
     */
    public double getMonthOut(int year, int moon) {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select money from account where year = ? and month = ? and money < ?", new String[]{year + "", moon + "", 0 + ""});
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        if (res == 0)
            return res;
        return -res;
    }


    /**
     * 获取当日的支出
     *
     * @param year
     * @param moon
     * @param day
     * @return
     */
    public double getDayOut(int year, int moon, int day) {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select money from account where year = ? and month = ? and day = ? and money < ?",
                new String[]{year + "", moon + "", day + "", 0 + ""});
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        if (res == 0)
            return res;
        return -res;
    }


    /**
     * 获取最近的支出
     *
     * @return
     */
    public double getLeastOut() {
        double res = 0;
        mDataBase = mHelper.getReadableDatabase();
        Cursor cursor = mDataBase.rawQuery("select money from account where  money < ? order  by year desc,month desc,day desc ",
                new String[]{0 + ""});
        if (cursor.moveToNext()) {
            res = cursor.getDouble(cursor.getColumnIndex("money"));
        }
        mDataBase.close();
        if (res == 0)
            return res;
        return -res;
    }


    /**
     * 获取账单详情数据
     *
     * @return
     */
    public List<MonthDetailBean> getDetailList() {

        mDataBase = mHelper.getReadableDatabase();

        Cursor cursor = mDataBase.rawQuery("select * from account order by year desc , month desc , day desc", new String[]{});

        List<MonthDetailBean> list = new ArrayList<>();

        int lastYear = 0;
        int lastMonth = 0;
        int lastDay = 0;

        int nowYear;
        int nowMonth;
        int nowDay;


        MonthDetailBean monthBean = null;
        DayDetailBean dayBean = null;
        DetailTagBean detailBean = null;

        while (cursor.moveToNext()) {
            nowYear = cursor.getInt(cursor.getColumnIndex("year"));
            nowMonth = cursor.getInt(cursor.getColumnIndex("month"));
            nowDay = cursor.getInt(cursor.getColumnIndex("day"));

            if (nowMonth != lastMonth || lastYear != nowYear) {
                monthBean = new MonthDetailBean();
                monthBean.setYear(nowYear);
                monthBean.setMonth(nowMonth);
                monthBean.setDayList(new ArrayList<DayDetailBean>());
                monthBean.setIn(getMonthIn(nowYear, nowMonth));
                monthBean.setOut(getMonthOut(nowYear, nowMonth));
                list.add(monthBean);
            }
            if (nowDay != lastDay || nowMonth != lastMonth || nowDay != lastDay) {
                dayBean = new DayDetailBean();
                dayBean.setDate(nowDay);
                dayBean.setTagList(new ArrayList<DetailTagBean>());
                monthBean.getDayList().add(dayBean);
            }
            lastMonth = nowMonth;
            lastYear = nowYear;
            lastDay = nowDay;

            detailBean = new DetailTagBean();
            detailBean.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            detailBean.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
            dayBean.getTagList().add(detailBean);
        }

        mDataBase.close();

        return list;
    }


    /**
     * 获取当前时间所在星期的支出
     *
     * @return
     */
    public double getWeekOut() {
        double res = 0;
        List<Calendar> list = dateToCurrentWeek();
        for (Calendar d : list) {
            res += getDayOut(d.get(Calendar.YEAR), d.get(Calendar.MONTH) + 1, d.get(Calendar.DAY_OF_MONTH));
        }
        return res;
    }


    /**
     * 获取当前时间所在的星期的日期
     * 以星期一开始，星期日结束
     *
     * @return
     */
    public static List<Calendar> dateToCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        int b = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (b == 0) {
            b = 7;
        }
        Calendar fdate;
        List<Calendar> list = new ArrayList<Calendar>();
        Long fTime = cal.getTime().getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = Calendar.getInstance();
            fdate.setTime(new Date(fTime + (a * 24 * 3600000)));
            list.add(a - 1, fdate);
        }
        return list;
    }
}
