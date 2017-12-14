package com.phone.konka.accountingbook.Bean;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/16.
 * <p>
 * <p>
 * 月份详情账单
 */

public class MonthDetailBean {

    /**
     * 所在的年份
     */
    private int year;

    /**
     * 月份
     */
    private int month;


    /**
     * 收入
     */
    private double in;

    /**
     * 支出
     */
    private double out;


    /**
     * 结余
     */
    private double left;

    private List<DayDetailBean> dayList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int moon) {
        this.month = moon;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public List<DayDetailBean> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayDetailBean> day) {
        this.dayList = day;
    }
}
