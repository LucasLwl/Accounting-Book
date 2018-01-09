package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * 计算器管理类
 * Created by 廖伟龙 on 2017/11/22.
 */

public class CalculatorManager {


    /**
     * 上下文
     */
    private Context mContext;


    /**
     * 左操作数
     */
    private String left = "0";


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
                    }
//                    else {
//                        Toast.makeText(mContext, "只能精确到小数后两位", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    left += num;
                }
            }
//            else {
//                Toast.makeText(mContext, "数字长度不能超多12位", Toast.LENGTH_SHORT).show();
//            }
        } else {
            if (right.equals("0")) {
                right = num + "";
            } else if (right.length() < 12) {
                if (right.indexOf(".") != -1) {
                    if (right.indexOf(".") + 2 >= right.length()) {
                        right += num;
                    }

//                    else {
//                        Toast.makeText(mContext, "只能精确到小数后两位", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    right += num;
                }
            }
//            else {
//                Toast.makeText(mContext, "数字长度不能超多12位", Toast.LENGTH_SHORT).show();
//            }
        }
        return left + operator + right;
    }


    /**
     * 处理+、-号
     *
     * @param op
     */
    public String addOperator(String op) {

//        先判断左操作数是否为空
        if (left.equals("") || left.equals("-")) {
//            Toast.makeText(mContext, "请先输入数字", Toast.LENGTH_SHORT).show();
        }
//        在判断右操作数是否为空  不空则进行运算，空则修改操作符
        else if (!right.equals("")) {
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

//        先判断操作符是否为空，为空则操作左操作数，不空则操作右操作数
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

//        先判断操作符是否为空，不空则进行运算，空则进行保存
        if (!operator.equals("")) {
            if (!right.equals("")) {
                calculate();
            } else {
                operator = "";
            }
            return left + operator + right;
        } else if (left.contains("-")) {
            if (!left.equals("-"))
                Toast.makeText(mContext, "金额不能为负", Toast.LENGTH_SHORT).show();
            return left;
        } else if (Double.parseDouble(left) == 0) {
            Toast.makeText(mContext, "金额不能为0", Toast.LENGTH_SHORT).show();
            return left;
        } else {
            left = "0";
            return "save";
        }


//        else if (!left.equals("") && Double.parseDouble(left) != 0) {
//            if (!left.contains("-")) {
//                left = "";
//                return "save";
//            } else {
//                Toast.makeText(mContext, "金额不能为负", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(mContext, "金额不能为空", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 删除一个字符
     *
     * @return
     */
    public String delOne() {

//        先判断右操作数是否为空，不空则删除右操作数
        if (!right.equals("")) {
            right = right.substring(0, right.length() - 1);

//            再判断操作符是否为空，不空则删除操作符
        } else if (!operator.equals("")) {
            operator = "";

//            最后判断左操作数是否为空，不空则删除左操作数
        } else if (!left.equals("")) {
            left = left.substring(0, left.length() - 1);
        }

        if (left.equals(""))
            left = "0";
        return left + operator + right;
    }

    /**
     * 删除所有字符
     *
     * @return
     */
    public String delAll() {
        left = "0";
        right = "";
        operator = "";
        return left;
    }


    /**
     * 进行+、-运算
     * <p>
     * 使用BigDecimal来避免double运算精度问题
     */
    private void calculate() {


        BigDecimal lBig = new BigDecimal(left);
        BigDecimal rBig = new BigDecimal(right);
        if (operator.equals("+"))
            left = lBig.add(rBig).toString();
        else
            left = lBig.subtract(rBig).toString();


        //去掉小数点后无效的0

        if (left.indexOf(".") > 0) {
            left = left.replaceAll("0+?$", "");
            left = left.replaceAll("[.]$", "");
        }

        operator = "";
        right = "";
    }


    /**
     * 大浮点数相加
     * @param left
     * @param right
     * @return
     */
//    public String bigAddition(String left, String right) {
//
//
//        byte[] numRes;
//        byte[] decRes;
//
//        char[] lDec;
//        char[] lNum;
//        char[] rDec;
//        char[] rNum;
//
//
//        if (left == null || right == null || operator == null)
//            return null;
//
//
//        int leftPointIndex = left.indexOf(".");
//        int rightPointIndex = right.indexOf(".");
//
//
//        if (leftPointIndex != -1) {
//            lDec = left.substring(leftPointIndex + 1, left.length()).toCharArray();
//            lNum = left.substring(0, leftPointIndex).toCharArray();
//        } else {
//            lNum = left.toCharArray();
//            lDec = new char[0];
//        }
//
//        if (rightPointIndex != -1) {
//            rDec = right.substring(rightPointIndex + 1, right.length()).toCharArray();
//            rNum = right.substring(0, rightPointIndex).toCharArray();
//        } else {
//            rNum = right.toCharArray();
//            rDec = new char[0];
//        }
//
//        numRes = new byte[Math.max(lNum.length, rNum.length) + 1];
//        decRes = new byte[Math.max(lDec.length, rDec.length) + 1];
//
//
//        for (int i = 0; i < decRes.length; i++) {
//            if (i < lDec.length) {
//                decRes[i] += lDec[lDec.length - i - 1] - '0';
//            }
//            if (i < rDec.length) {
//                decRes[i] += rDec[rDec.length - i - 1] - '0';
//            }
//            if (decRes[i] > 9) {
//                decRes[i] -= 10;
//                decRes[i + 1]++;
//            }
//        }
//
//        numRes[0] += decRes[decRes.length - 1];
//
//        for (int i = 0; i < numRes.length; i++) {
//            if (i < lNum.length) {
//                numRes[i] += lNum[lNum.length - i - 1] - '0';
//            }
//            if (i < rNum.length) {
//                numRes[i] += rNum[rNum.length - i - 1] - '0';
//            }
//
//            if (numRes[i] > 9) {
//                numRes[i] -= 10;
//                numRes[i + 1]++;
//            }
//        }
//
//        StringBuffer sb = new StringBuffer();
//        for (int i = numRes.length - 1; i >= 0; i--) {
//            if (i != numRes.length - 1 || numRes[i] != 0) {
//                sb.append(numRes[i]);
//            }
//        }
//
//        sb.append(".");
//
//        for (int i = decRes.length - 2; i >= 0; i--) {
//            sb.append(decRes[i]);
//        }
//
//        while (sb.lastIndexOf("0") == sb.length() - 1)
//            sb.deleteCharAt(sb.lastIndexOf("0"));
//
//        if (sb.indexOf(".") == sb.length() - 1)
//            sb.deleteCharAt(sb.indexOf("."));
//
//        return sb.toString();
//    }


}
