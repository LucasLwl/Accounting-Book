package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phone.konka.accountingbook.Adapter.GroupAdapter;
import com.phone.konka.accountingbook.Bean.MonthDetailBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBOperator;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.util.List;

/**
 * 明细界面Activity
 * <p>
 * <p>
 * Created by 廖伟龙 on 2017/11/16.
 */

public class DetailActivity extends Activity implements View.OnClickListener {


    /**
     * 展示账单详情的ExpandableListView
     */
    private ExpandableListView mListView;


    /**
     * 展示账单详情的ExpandableListView的适配器
     */
    private GroupAdapter mAdapter;


    /**
     * 账单详情数据
     */
    private List<MonthDetailBean> mData;

    /**
     * 数据库操作类
     */
    private DBOperator mDBOperator;


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;


    /**
     * 长按Item显示的删除提示栏
     */
    private PopupWindow mPopupWindow;

    /**
     * 长按的Item位置
     */
    private int mLongClickPos = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);

//        设置沉浸式状态栏
        initState();

        initView();
        initEvent();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }


    /**
     * 设置沉浸式状态栏
     * <p>
     * 先设置状态栏为透明
     * <p>
     * 然后设置占位View的高度为状态栏的高度
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_detail_bar);
            ViewGroup.LayoutParams lp = ll.getLayoutParams();
            lp.height = getStatusBarHeight();
            ll.setLayoutParams(lp);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {

        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initView() {
        mListView = (ExpandableListView) findViewById(R.id.lv_detail_one);

    }

    /**
     * 初始化数据
     */
    private void initData() {

        mDBOperator = new DBOperator(this);

        mThreadPool = ThreadPoolManager.getInstance();

//        从数据表中获取数据
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mData = mDBOperator.getDetailList();

//                转到UI线程处理View的显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new GroupAdapter(DetailActivity.this, mData);
                        mListView.setAdapter(mAdapter);
                    }
                });

            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        findViewById(R.id.img_detail_addAccount).setOnClickListener(this);

        findViewById(R.id.img_detail_back).setOnClickListener(this);


        /**
         * 外层ExpandableListView的Group长按事件
         *
         */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int npos = mListView.pointToPosition((int) view.getX(), (int) view.getY());
                if (npos != AdapterView.INVALID_POSITION) {
                    long pos = mListView.getExpandableListPosition(npos);
                    int childPos = ExpandableListView.getPackedPositionChild(pos);
                    int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                    if (childPos == AdapterView.INVALID_POSITION) {
                        mLongClickPos = groupPos;
                        showPopupWindow(parent, view);
                    }
                }
                return true;
            }
        });

        /**
         * 默认设置外层ExpandableListView每次只能有一个Child展开
         */
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < mAdapter.getGroupCount(); i++)
                    if (groupPosition != i)
                        mListView.collapseGroup(i);
            }
        });
    }


    /**
     * 隐藏删除提示栏
     */
    private void dismissPopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }


    /**
     * 显示删除提示栏
     *
     * @param parent
     * @param view
     */
    private void showPopupWindow(ViewGroup parent, View view) {

        if (mPopupWindow == null) {
            Button btn = new Button(this);
            btn.setBackgroundColor(getResources().getColor(R.color.white));
            btn.setText("是否删除该账单");
            btn.setTextColor(getResources().getColor(R.color.item_text));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            btn.setGravity(Gravity.CENTER);
            btn.setPadding(20, 0, 20, 0);
            btn.setLayoutParams(lp);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

//                    在线程池中删除数据表中的账单信息
                    mThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            mDBOperator.delete("account", "year = ? and month = ?",
                                    new String[]{mData.get(mLongClickPos).getYear() + "", mData.get(mLongClickPos).getMonth() + ""});

//                            转到UI线程处理View更新
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mData.remove(mLongClickPos);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                    dismissPopupWindow();
                }
            });
            mPopupWindow = new PopupWindow(btn, RelativeLayout.LayoutParams.WRAP_CONTENT, 150, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setTouchable(true);
        }

        /**
         * 显示删除提示栏
         *
         * 默认显示在长按的Item下方，若下方显示不下，则显示在上方
         */
        if (!mPopupWindow.isShowing()) {
            if (view.getBottom() + mPopupWindow.getHeight() > parent.getHeight())
                mPopupWindow.showAsDropDown(view, (view.getWidth() - mPopupWindow.getWidth()) / 2, -(view.getHeight() + mPopupWindow.getHeight()));
            else {
                mPopupWindow.showAsDropDown(view, (view.getWidth() - mPopupWindow.getWidth()) / 2, 0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_detail_addAccount:
                Intent intent = new Intent(DetailActivity.this, AddAccountActivity.class);
                startActivity(intent);
                break;

            case R.id.img_detail_back:
                finish();
                break;
        }
    }
}