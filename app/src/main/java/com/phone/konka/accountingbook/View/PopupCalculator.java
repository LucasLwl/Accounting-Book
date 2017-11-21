package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/11/21.
 */

public class PopupCalculator implements View.OnClickListener {


    private Context mContext;

    private PopupWindow mPopCalcualator;
    private View mCalView;


    private TextView mTvTag;
    private TextView mTvRes;

    private String left = "";
    private String right = "";
    private String operator = "";


    public PopupCalculator(Context context) {

        mContext = context;
        mCalView = LayoutInflater.from(context).inflate(R.layout.popup_calculator, null);
        mPopCalcualator = new PopupWindow(mCalView, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        mPopCalcualator.setOutsideTouchable(true);
        mPopCalcualator.setTouchable(true);
        mPopCalcualator.setAnimationStyle(R.style.popupCalculatorStyle);

        initView();
    }

    private void initView() {

        mTvTag = (TextView) mCalView.findViewById(R.id.tv_popup_calculator_tag);
        mTvRes = (TextView) mCalView.findViewById(R.id.tv_popup_calculator_res);
        mCalView.findViewById(R.id.tv_popup_calculator_one).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_two).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_three).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_four).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_five).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_six).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_seven).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_eight).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_nine).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_zero).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_add).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_sub).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_point).setOnClickListener(this);
        mCalView.findViewById(R.id.tv_popup_calculator_ok).setOnClickListener(this);
        mCalView.findViewById(R.id.img_popup_calculator_del).setOnClickListener(this);
    }


    public void setTagText(String text) {
        mTvTag.setText(text);
    }


    /**
     * 处理数字
     *
     * @param num
     */
    private void calcualate(int num) {
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
        mTvRes.setText(left + operator + right);
    }

    /**
     * 处理+、-号
     *
     * @param op
     */
    private void toOperate(String op) {

        if (left.equals("")) {
            Toast.makeText(mContext, "请先输入数字", Toast.LENGTH_SHORT).show();
        } else if (!right.equals("")) {
            if (operator.equals("+")) {
                left = (Double.parseDouble(left) + Double.parseDouble(right)) + "";
            } else {
                left = (Double.parseDouble(left) - Double.parseDouble(right)) + "";
            }
            operator = op;
            right = "";
        } else {
            operator = op;
        }
        mTvRes.setText(left + operator + right);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_popup_calculator_one:
                calcualate(1);
                break;
            case R.id.tv_popup_calculator_two:
                calcualate(2);
                break;
            case R.id.tv_popup_calculator_three:
                calcualate(3);
                break;
            case R.id.tv_popup_calculator_four:
                calcualate(4);
                break;
            case R.id.tv_popup_calculator_five:
                calcualate(5);
                break;
            case R.id.tv_popup_calculator_six:
                calcualate(6);
                break;
            case R.id.tv_popup_calculator_seven:
                calcualate(7);
                break;
            case R.id.tv_popup_calculator_eight:
                calcualate(8);
                break;
            case R.id.tv_popup_calculator_nine:
                calcualate(9);
                break;
            case R.id.tv_popup_calculator_zero:
                calcualate(0);
                break;
            case R.id.tv_popup_calculator_add:
                toOperate("+");
                break;
            case R.id.tv_popup_calculator_sub:
                toOperate("-");
                break;
            case R.id.tv_popup_calculator_point:
                if (operator.equals("")) {
                    if (!left.equals("") && left.indexOf(".") == -1) {
                        left += ".";
                    }
                } else {
                    if (!right.equals("") && right.indexOf(".") == -1) {
                        right += ".";
                    }
                }
                break;
            case R.id.tv_popup_calculator_ok:
                if (!operator.equals("")) {
                    if (operator.equals("+")) {
                        left = (Double.parseDouble(left) + Double.parseDouble(right)) + "";
                    } else {
                        left = (Double.parseDouble(left) - Double.parseDouble(right)) + "";
                    }
                    operator = "";
                    right = "";
                    mTvRes.setText(left + operator + right);
                } else {

//                    入表
                }
                break;
            case R.id.img_popup_calculator_del:
                String str = (String) mTvRes.getText();
                if (str != null && !str.isEmpty()) {
                    mTvRes.setText(str.substring(0, str.length() - 1));
                }
                break;
        }
    }
}
