package com.phone.konka.accountingbook.Utils;

import java.text.DecimalFormat;

/**
 * 格式化数字
 * 从第7位数开始显示字体“万”，从第9位开始显示“亿”,并且保留两位小数
 * Created by 廖伟龙 on 2017/11/22.
 */

public class DoubleTo2Decimal {


    public static String doubleTo2Decimal(double num) {

//        设置格式为小数点后2位
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
