package com.phone.konka.accountingbook.Tool;

import java.text.DecimalFormat;

/**
 *
 * 格式化数字
 * Created by 廖伟龙 on 2017/11/22.
 */

public class DoubleTo2Decimal {
    public static String doubleTo2Decimal(double num) {
        DecimalFormat df = new DecimalFormat("#.00");
        String str;
        if (num > 99999999) {
            int head = (int) (num / 100000000);
            num %= 100000000;
            int tail = (int) (num / 100000);
            double temp = head + 0.001 * tail;
            str = df.format(temp) + "亿";
        } else if (num > 999999) {
            int head = (int) (num / 10000);
            num %= 10000;
            int tail = (int) (num / 10);
            double temp = head + 0.001 * tail;
            str = df.format(temp) + "万";
        } else {
            str = df.format(num);
        }
        return str;
    }
}
