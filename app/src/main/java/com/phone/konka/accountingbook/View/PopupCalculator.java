package com.phone.konka.accountingbook.View;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.CalculatorManager;

/**
 * 显示计算器的PopupWindow
 * <p>
 * <p>
 * Created by 廖伟龙 on 2017/11/21.
 */

public class PopupCalculator implements View.OnClickListener {


    /**
     * 上下文
     */
    private Context mContext;


    /**
     * 计算器管理类
     */
    private CalculatorManager mCalculator;


    /**
     * 显示计算器的PopupWindow
     */
    private PopupWindow mPopCalculator;

    /**
     * 计算机布局
     */
    private View mCalView;


    /**
     * 标签
     */
    private TextView mTvTag;

    /**
     * 标签的icon
     */
    private ImageView mImgTag;


    /**
     * 结果
     */
    private TextView mTvRes;


    /**
     * 标签icon的资源id
     */
    private int mIconID;


    public PopupCalculator(Context context) {

        mContext = context;

        mCalculator = new CalculatorManager(mContext);

        /**
         * 初始化显示计算机的PopupWindow,高度为屏幕高度的三分之一
         */
        mCalView = LayoutInflater.from(context).inflate(R.layout.popup_calculator, null);
        int height = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() / 3;
        mPopCalculator = new PopupWindow(mCalView, ViewGroup.LayoutParams.MATCH_PARENT, height, false);
        mPopCalculator.setOutsideTouchable(false);
        mPopCalculator.setTouchable(true);
        mPopCalculator.setAnimationStyle(R.style.popupCalculatorStyle);
        initView();

    }


    /**
     * 获取Popup计算器
     *
     * @return
     */
    public PopupWindow getPopCalculator() {
        return mPopCalculator;
    }


    /**
     * 显示Popup计算器
     *
     * @param rootView
     */
    public void showPopCalculator(View rootView) {
        if (mPopCalculator != null && !mPopCalculator.isShowing()) {
            mPopCalculator.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }


    /**
     * 隐藏Popup计算器
     */
    public void dismissPopCalculator() {
        if (mPopCalculator != null && mPopCalculator.isShowing()) {
            mPopCalculator.dismiss();
        }
    }


    /**
     * 返回是否显示了Popup计算器
     *
     * @return
     */
    public boolean isPopCalculatorShow() {
        if (mPopCalculator != null && mPopCalculator.isShowing())
            return true;
        return false;
    }


    private void initView() {

        mImgTag = (ImageView) mCalView.findViewById(R.id.img_popup_calculator_tag);
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
        mCalView.findViewById(R.id.img_popup_calculator_del).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTvRes.setText(mCalculator.delAll());
                return false;
            }
        });
    }

    public void setTagIcon(int iconID) {
        mIconID = iconID;
        mImgTag.setImageResource(iconID);
    }

    public int getTagIconID() {
        return mIconID;
    }

    public void setTagText(String text) {
        mTvTag.setText(text);
    }

    public String getTagText() {
        return mTvTag.getText().toString();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_popup_calculator_one:
                mTvRes.setText(mCalculator.addNum(1));
                break;
            case R.id.tv_popup_calculator_two:
                mTvRes.setText(mCalculator.addNum(2));
                break;
            case R.id.tv_popup_calculator_three:
                mTvRes.setText(mCalculator.addNum(3));
                break;
            case R.id.tv_popup_calculator_four:
                mTvRes.setText(mCalculator.addNum(4));
                break;
            case R.id.tv_popup_calculator_five:
                mTvRes.setText(mCalculator.addNum(5));
                break;
            case R.id.tv_popup_calculator_six:
                mTvRes.setText(mCalculator.addNum(6));
                break;
            case R.id.tv_popup_calculator_seven:
                mTvRes.setText(mCalculator.addNum(7));
                break;
            case R.id.tv_popup_calculator_eight:
                mTvRes.setText(mCalculator.addNum(8));
                break;
            case R.id.tv_popup_calculator_nine:
                mTvRes.setText(mCalculator.addNum(9));
                break;
            case R.id.tv_popup_calculator_zero:
                mTvRes.setText(mCalculator.addNum(0));
                break;
            case R.id.tv_popup_calculator_add:
                mTvRes.setText(mCalculator.addOperator("+"));
                break;
            case R.id.tv_popup_calculator_sub:
                mTvRes.setText(mCalculator.addOperator("-"));
                break;
            case R.id.tv_popup_calculator_point:
                mTvRes.setText(mCalculator.addPoint());
                break;
            case R.id.tv_popup_calculator_ok:
                String res = mCalculator.pressOK();
                if (res.equals("save")) {
                    mListener.addAccount(getTagText(), mIconID, Double.parseDouble(mTvRes.getText().toString()));
                    mTvRes.setText("0");
                } else {
                    mTvRes.setText(res);
                }
                break;
            case R.id.img_popup_calculator_del:
                mTvRes.setText(mCalculator.delOne());
                break;
        }
    }

    private AddAccountListener mListener;

    public void setAddAccountListener(AddAccountListener listener) {
        mListener = listener;
    }

    public interface AddAccountListener {
        void addAccount(String tag, int iconID, double money);
    }
}
