package com.phone.konka.accountingbook.Bean;

import android.graphics.Bitmap;

import java.util.Iterator;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class TagBean {

    private String text;
    private Bitmap icon;
    private int iconID;

    public TagBean() {
    }

    public TagBean(String text, Bitmap icon) {
        this.text = text;
        this.icon = icon;
    }

    public TagBean(String text, int iconID) {
        this.text = text;
        this.iconID = iconID;
    }


    //    private int

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
}
