package com.phone.konka.accountingbook.Bean;

/**
 * Created by 廖伟龙 on 2017/11/16.
 * <p>
 * <p>
 * 收入详情
 */

public class DetailTagBean {


    private int year;

    private int moon;

    private int day;


    /**
     * 收入类型
     */
    private String tag;


    /**
     * 收入金额
     */
    private int money;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMoon() {
        return moon;
    }

    public void setMoon(int moon) {
        this.moon = moon;
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
