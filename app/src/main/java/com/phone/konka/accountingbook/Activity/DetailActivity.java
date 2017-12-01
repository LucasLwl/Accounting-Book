package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;

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

public class DetailActivity extends Activity {


    private ExpandableListView mListView;
    private DetailMoonAdapter mAdapter;
    private List<MonthDetailBean> mDatas;

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

        //设置取消title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        initView();
        initData();
        initEvent();


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
                mDatas = mDBOperator.getDetailList();
                mAdapter = new DetailMoonAdapter(DetailActivity.this, mDatas);
                mListView.setAdapter(mAdapter);
            }
        });
    }

    private void initEvent() {

        findViewById(R.id.img_detail_addAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, AddAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}