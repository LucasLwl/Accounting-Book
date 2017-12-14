package com.phone.konka.accountingbook.Bean;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/16.
 * <p>
 * <p>
 * 每天的账单
 */

public class DayDetailBean {


    private int year;


    private int month;

    /**
     * 日期
     */
    private int date;


    /**
     * 具体的收入情况
     */
    private List<DetailTagBean> tagList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int day) {
        this.month = day;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<DetailTagBean> getTagList() {
        return tagList;
    }

    public void setTagList(List<DetailTagBean> tagList) {
        this.tagList = tagList;
    }
}
