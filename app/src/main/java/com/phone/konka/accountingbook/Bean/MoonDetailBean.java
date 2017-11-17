package com.phone.konka.accountingbook.Bean;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/16.
 *
 *
 * 月份详情账单
 *
 */

public class MoonDetailBean {

    /**
     * 所在的年份
     */
    private String year;

    /**
     * 月份
     */
    private String moon;


    /**
     * 收入
     */
    private String in;

    /**
     * 支出
     */
    private String out;


    /**
     * 结余
     */
    private String left;

    private List<DayDetailBean> dayList;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMoon() {
        return moon;
    }

    public void setMoon(String moon) {
        this.moon = moon;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public List<DayDetailBean> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayDetailBean> day) {
        this.dayList = day;
    }
}
