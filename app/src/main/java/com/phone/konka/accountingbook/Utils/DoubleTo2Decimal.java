package com.phone.konka.accountingbook.Utils;

import java.text.DecimalFormat;

/**
 * 格式化数字
 * Created by 廖伟龙 on 2017/11/22.
 */

public class DoubleTo2Decimal {
    public static String doubleTo2Decimal(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        String str;
        boolean isNegative = false;

        if (num < 0) {
            num = Math.abs(num);
            isNegative = true;
        }

        if (num > 99999999) {
            str = df.format(num / 100000000) + "亿";
        } else if (num > 999999) {
            str = df.format(num / 10000) + "万";
        } else {
            str = df.format(num);
        }

        if (isNegative)
            return "-" + str;
        return str;
    }
}
