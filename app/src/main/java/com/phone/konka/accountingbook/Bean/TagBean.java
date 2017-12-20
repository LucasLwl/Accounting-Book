package com.phone.konka.accountingbook.Bean;

/**
 * 字段数据
 * <p>
 * Created by 廖伟龙 on 2017/11/20.
 */

public class TagBean {


    /**
     * 字段名称
     */
    private String text;


    /**
     * 字段图标的ResourcesID
     */
    private int iconID;


    public TagBean() {
    }

    public TagBean(String text, int iconID) {
        this.text = text;
        this.iconID = iconID;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
}
