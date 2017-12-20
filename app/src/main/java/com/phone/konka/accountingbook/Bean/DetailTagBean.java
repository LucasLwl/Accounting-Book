package com.phone.konka.accountingbook.Bean;

/**
 * 具体的收支数据
 * <p>
 * Created by 廖伟龙 on 2017/11/16.
 */

public class DetailTagBean {


    /**
     * 数据表中的id
     */
    private int id;


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
     * 收入字段
     */
    private String tag;


    /**
     * 收入字段的图标ResourcesID
     */
    private int iconID;


    /**
     * s收支金额
     */
    private double money;


    public DetailTagBean() {
    }

    public DetailTagBean(int year, int month, int day, String tag, int iconID, double money) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.tag = tag;
        this.iconID = iconID;
        this.money = money;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
