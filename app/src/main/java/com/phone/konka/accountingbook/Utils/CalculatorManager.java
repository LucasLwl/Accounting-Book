package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import com.phone.konka.accountingbook.Bean.DetailTagBean;

/**
 * 计算器管理类
 * Created by 廖伟龙 on 2017/11/22.
 */

public class CalculatorManager {

    private Context mContext;

    /**
     * 左操作数
     */
    private String left = "";


    /**
     * 右操作数
     */
    private String right = "";


    /**
     * 运算符
     */
    private String operator = "";


    public CalculatorManager(Context mContext) {
        this.mContext = mContext;

    }

    /**
     * 处理数字
     *
     * @param num
     */
    public String addNum(int num) {
        if (operator.equals("")) {
            if (left.equals("0")) {
                left = num + "";
            } else if (left.length() < 12) {
                if (left.indexOf(".") != -1) {
                    if (left.indexOf(".") + 2 >= left.length()) {
                        left += num;
                    } else {
                        Toast.makeText(mContext, "只能精确到小数后两位", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    left += num;
                }
            } else {
                Toast.makeText(mContext, "数字长度不能超多12位", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (right.equals("0")) {
                right = num + "";
            } else if (right.length() < 12) {
                if (right.indexOf(".") != -1) {
                    if (right.indexOf(".") + 2 >= right.length()) {
                        right += num;
                    } else {
                        Toast.makeText(mContext, "只能精确到小数后两位", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    right += num;
                }
            } else {
                Toast.makeText(mContext, "数字长度不能超多12位", Toast.LENGTH_SHORT).show();
            }
        }
        return left + operator + right;
    }


    /**
     * 处理+、-号
     *
     * @param op
     */
    public String addOperator(String op) {

        if (left.equals("")) {
            Toast.makeText(mContext, "请先输入数字", Toast.LENGTH_SHORT).show();
        } else if (!right.equals("")) {
            calculate();
            operator = op;

        } else {
            operator = op;
        }
        return left + operator + right;
    }


    /**
     * 处理小数点
     *
     * @return
     */
    public String addPoint() {
        if (operator.equals("")) {
            if (!left.equals("") && left.indexOf(".") == -1) {
                left += ".";
            }
        } else {
            if (!right.equals("") && right.indexOf(".") == -1) {
                right += ".";
            }
        }
        return left + operator + right;
    }


    /**
     * 处理OK键
     *
     * @return
     */
    public String pressOK() {
        if (!operator.equals("")) {
            if (!right.equals("")) {
                calculate();
            }
            return left + operator + right;
        } else if (!left.equals("")) {
            if (!left.equals("0")) {
                left = "";
                return "save";
            }
        }
        return "";
    }

    /**
     * 删除一个字符
     *
     * @return
     */
    public String delOne() {
        if (!right.equals("")) {
            right = right.substring(0, right.length() - 1);
        } else if (!operator.equals("")) {
            operator = "";
        } else if (!left.equals("")) {
            left = left.substring(0, left.length() - 1);
        }
        return left + operator + right;
    }

    /**
     * 删除所有字符
     *
     * @return
     */
    public String delAll() {
        left = "";
        right = "";
        operator = "";
        return left + operator + right;
    }


    /**
     * 进行+、-运算
     */
    private void calculate() {


        double one = Double.parseDouble(left);
        double two = Double.parseDouble(right);


        if (left.length() > 9 || right.length() > 9) {

            if (left.indexOf(".") == -1) {
                left += ".00";
            } else{
                while (left.indexOf(".") != -1 && left.indexOf(".") + 2 > left.length() - 1) {
                    left += "0";
                }
            }

            if (right.indexOf(".")==-1){
                right+=".00";
            } else{
                while (right.indexOf(".") != -1 && right.indexOf(".") + 2 > right.length() - 1) {
                    right += "0";
                }
            }

            char[] lCh = new StringBuffer(left).reverse().toString().toCharArray();
            char[] rCh = new StringBuffer(right).reverse().toString().toCharArray();

            byte[] res = new byte[Math.max(lCh.length, rCh.length) + 1];

            res[0] += lCh[0] + rCh[0] - '0' * 2;
            res[1] += lCh[1] + rCh[1] - '0' * 2;
            if (res[0] > 9) {
                res[1]++;
                res[0] -= 10;
            }
            if (res[1] > 9) {
                res[3]++;
                res[1] -= 10;
            }
            for (int i = 3; i < res.length; i++) {
                if (i < lCh.length) {
                    res[i] += lCh[i] - '0';
                }
                if (i < rCh.length) {
                    res[i] += rCh[i] - '0';
                }
                if (res[i] > 9) {
                    res[i] -= 10;
                    res[i + 1]++;
                }
            }

            StringBuffer sb = new StringBuffer();

            for (int i = res.length - 1; i >= 0; i--) {
                if (i == 2)
                    sb.append('.');
                else if (res[i] != 0 || i != res.length - 1) {
                    sb.append(res[i]);
                }
            }
            left = sb.toString();
        } else {
            if (operator.equals("+")) {
                one = one + two;
            } else {
                one = one - two;
            }
            if (one == (int) one) {
                left = String.valueOf((int) one);
            } else {
                left = String.valueOf(one);
            }
        }

        operator = "";
        right = "";
    }
}
