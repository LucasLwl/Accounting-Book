package com.phone.konka.accountingbook.Bean;

import android.graphics.Bitmap;

import java.util.Iterator;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class TagBean {

    private String text;
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
