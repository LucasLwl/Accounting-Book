package com.phone.konka.accountingbook.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.Bean.MonthDetailBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ContentProvider对数据的具体操作
 * <p>
 * Created by 廖伟龙 on 2017/12/18.
 */

public class ProviderManager {


    /**
     * ContentProvider的authorities
     */
    private static final String PROVIDER = "com.phone.konka.accountingbook.Utils.AccountProvider";


    private Context mContext;


    /**
     * ContentProvider的Uri
     */
    private Uri mUri;


    public ProviderManager(Context mContext) {
        this.mContext = mContext;
        mUri = Uri.parse("content://" + PROVIDER);
    }


    /**
     * 查看数据库是否为空
     *
     * @return
     */
    public boolean isDBEmpty() {
        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"_id"}, null, null, null);
        if (cursor.moveToLast())
            return false;
        return true;
    }


    /**
     * 添加账单信息
     *
     * @param bean
     */
    public synchronized void insertAccount(DetailTagBean bean) {

        ContentValues values = new ContentValues();
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("tag", bean.getTag());
        values.put("iconID", bean.getIconID());
        values.put("money", bean.getMoney());
        mContext.getContentResolver().insert(mUri, values);
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
        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"money"}, "year = ? and month = ? and money > ?", new String[]{year + "", month + "", "0"}, null);
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
        return res;
    }

    /**
     * 获取月份的总支出
     *
     * @param year
     * @param month
     * @return
     */
    public double getMonthOut(int year, int month) {
        double res = 0;
        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"money"}, "year = ? and month = ? and money < ?", new String[]{year + "", month + "", "0"}, null);
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
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
        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"money"}, "year = ? and month = ? and day = ? and money < ?",
                new String[]{year + "", moon + "", day + "", "0"}, null);
        while (cursor.moveToNext()) {
            res += cursor.getDouble(cursor.getColumnIndex("money"));
        }
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
        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"money"}, "money < ?", new String[]{"0"}, "year,month,day");
        if (cursor.moveToLast()) {
            res = cursor.getDouble(cursor.getColumnIndex("money"));
        }
        if (res == 0)
            return res;
        return -res;
    }


    /**
     * 获取数据表中的所有数据
     *
     * @return
     */
    public List<DetailTagBean> getAllData() {
        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"*"}, null, null, null);
        List<DetailTagBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            DetailTagBean bean = new DetailTagBean();
            bean.setYear(cursor.getInt(cursor.getColumnIndex("year")));
            bean.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
            bean.setDay(cursor.getInt(cursor.getColumnIndex("day")));
            bean.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            bean.setIconID(cursor.getInt(cursor.getColumnIndex("iconID")));
            bean.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
            list.add(bean);
        }
        return list;
    }


    /**
     * 删除数据表中的数据
     *
     * @param selection
     * @param selectionArgs
     */
    public synchronized void deleteData(String selection, String[] selectionArgs) {
        mContext.getContentResolver().delete(mUri, selection, selectionArgs);
    }


    /**
     * 删除数据表中的所有数据
     */
    public synchronized void deleteAllData() {
        mContext.getContentResolver().delete(mUri, null, null);
    }


    /**
     * 获取账单详情数据
     *
     * @return
     */
    public List<MonthDetailBean> getDetailList() {

        Cursor cursor = mContext.getContentResolver().query(mUri, new String[]{"*"}, null, null, "  year desc , month desc , day desc , _id desc");

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
                dayBean.setYear(nowYear);
                dayBean.setMonth(nowMonth);
                dayBean.setDate(nowDay);
                dayBean.setTagList(new ArrayList<DetailTagBean>());
                monthBean.getDayList().add(dayBean);
            }
            lastMonth = nowMonth;
            lastYear = nowYear;
            lastDay = nowDay;

            detailBean = new DetailTagBean();
            detailBean.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            detailBean.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            detailBean.setIconID(cursor.getInt(cursor.getColumnIndex("iconID")));
            detailBean.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
            detailBean.setDay(nowDay);
            dayBean.getTagList().add(detailBean);
        }
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
     * 根据当前时间计算本周的日期
     *
     * @return
     */
    private List<Calendar> dateToCurrentWeek() {

        List<Calendar> list = new ArrayList<Calendar>();

        Calendar calendar = Calendar.getInstance();
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        long nowTime = calendar.getTimeInMillis();
        int date = calendar.get(Calendar.DAY_OF_WEEK);
        if (firstDayOfWeek == Calendar.SUNDAY) {
            date--;
            if (date == 0)
                date = 7;
        }
        long time = nowTime - date * 24 * 3600000;
        for (int i = 1; i < 8; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time + i * 24 * 3600000);
            list.add(i - 1, cal);
        }
        return list;
    }


}
