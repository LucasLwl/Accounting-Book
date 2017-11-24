package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.widget.Toast;

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
            } else if (left.length() <= 12) {
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
            } else if (right.length() <= 12) {
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
     * @return
     */
    public String pressOK() {
        if (!operator.equals("")) {
            if (!right.equals("")) {
                calculate();
            }
            return left + operator + right;
        } else if (!left.equals("")) {

        }
        return "";

    }

    /**
     * 删除一个字符
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
        if (operator.equals("+")) {
            one = one + two;
        } else {
            one = one - two;
        }
        if (one == (int) one) {
            left = (int) one + "";
        } else {
            left = one + "";
        }
        operator = "";
        right = "";
    }
}
