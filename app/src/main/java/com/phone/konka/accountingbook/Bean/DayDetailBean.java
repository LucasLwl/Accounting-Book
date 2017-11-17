package com.phone.konka.accountingbook.Bean;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/16.
 *
 *
 * 每天的账单
 */

public class DayDetailBean {


    /**
     * 日期
     */
    private String date;


    /**
     * 具体的收入情况
     */
    private List<DetailTagBean> tagList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DetailTagBean> getTagList() {
        return tagList;
    }

    public void setTagList(List<DetailTagBean> tagList) {
        this.tagList = tagList;
    }
}
