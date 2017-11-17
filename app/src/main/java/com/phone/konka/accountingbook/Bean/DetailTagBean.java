package com.phone.konka.accountingbook.Bean;

/**
 * Created by 廖伟龙 on 2017/11/16.
 *
 *
 * 收入详情
 *
 */

public class DetailTagBean {


    /**
     * 收入类型
     */
    private String tag;


    /**
     * 收入金额
     */
    private String money;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
