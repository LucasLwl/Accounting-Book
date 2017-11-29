package com.phone.konka.accountingbook.Bean;

/**
 * Created by 廖伟龙 on 2017/11/16.
 * <p>
 * <p>
 * 收入详情
 */

public class DetailTagBean {


    private int year;

    private int month;

    private int day;


    /**
     * 收入类型
     */
    private String tag;


    /**
     * 收入金额
     */
    private double money;


    public DetailTagBean() {
    }

    public DetailTagBean(int year, int month, int day, String tag, double money) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.tag = tag;
        this.money = money;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
