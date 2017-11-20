package com.phone.konka.accountingbook.Bean;

import android.graphics.Bitmap;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class TagBean {

    private String text;
    private Bitmap icon;

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
}
