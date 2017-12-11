package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.Adapter.DetailMoonAdapter;
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


    private ExpandableListView mListView;
    private DetailMoonAdapter mAdapter;
    private List<MonthDetailBean> mData;

    /**
     * 数据库操作类
     */
    private DBOperator mDBOperator;


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);

        initState();

        initView();
        initEvent();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_detail_bar);
            ViewGroup.LayoutParams lp = ll.getLayoutParams();
            lp.height = getStatusBarHeight();
            ll.setLayoutParams(lp);
        }
    }

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

    private void initData() {

        mDBOperator = new DBOperator(this);

        mThreadPool = ThreadPoolManager.getInstance();

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mData = mDBOperator.getDetailList();
                mAdapter = new DetailMoonAdapter(DetailActivity.this, mData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setAdapter(mAdapter);
                    }
                });

            }
        });
    }

    private void initEvent() {

        findViewById(R.id.img_detail_addAccount).setOnClickListener(this);

        findViewById(R.id.img_detail_back).setOnClickListener(this);
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