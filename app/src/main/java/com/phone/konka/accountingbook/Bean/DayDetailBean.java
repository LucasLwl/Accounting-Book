package com.phone.konka.accountingbook.Bean;

import java.util.List;

/**
 * 每天的收支数据
 * <p>
 * <p>
 * Created by 廖伟龙 on 2017/11/16.
 */

public class DayDetailBean {


    /**
     * 年份
     */
    private int year;


    /**
     * 月份
     */
    private int month;

    /**
     * 日期
     */
    private int day;


    /**
     * 每天具体的收入情况
     */
    private List<DetailTagBean> tagList;


    /**
     * 获取年份
     *
     * @return
     */
    public int getYear() {
        return year;
    }


    /**
     * 设置年份
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }


    /**
     * 获取月份
     *
     * @return
     */
    public int getMonth() {
        return month;
    }


    /**
     * 设置月份
     *
     * @param day
     */
    public void setMonth(int day) {
        this.month = day;
    }


    /**
     * 获取日期
     *
     * @return
     */
    public int getDate() {
        return day;
    }


    /**
     * 设置日期
     *
     * @param date
     */
    public void setDate(int date) {
        this.day = date;
    }


    /**
     * 获取具体的收支情况
     *
     * @return
     */
    public List<DetailTagBean> getTagList() {
        return tagList;
    }


    /**
     * 设置具体的收支情况
     *
     * @param tagList
     */
    public void setTagList(List<DetailTagBean> tagList) {
        this.tagList = tagList;
    }
}
