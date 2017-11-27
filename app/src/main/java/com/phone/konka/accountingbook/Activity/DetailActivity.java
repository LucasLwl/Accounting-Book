package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ExpandableListView;

import com.phone.konka.accountingbook.Adapter.DetailMoonAdapter;
import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.Bean.MoonDetailBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBManager;

import java.util.ArrayList;
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
    private List<MoonDetailBean> mDatas;

    private DBManager mDBManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置取消title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        mDatas = new ArrayList<>();

        MoonDetailBean moon = new MoonDetailBean();
        moon.setYear("2017");
        moon.setMoon(1 + "");
        moon.setIn("10000");
        moon.setOut("2000");
        moon.setLeft("8000");

        List<DayDetailBean> dayList = new ArrayList<>();
        DayDetailBean day = new DayDetailBean();
        day.setDate(1 + "");

        List<DetailTagBean> tagList = new ArrayList<>();
        DetailTagBean tag = new DetailTagBean();
        tag.setTag("买手机");
        tag.setMoney(-5388);

        tagList.add(tag);
        tagList.add(tag);
        tagList.add(tag);

        day.setTagList(tagList);
        dayList.add(day);
        dayList.add(day);
        dayList.add(day);
        dayList.add(day);


        moon.setDayList(dayList);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);
        mDatas.add(moon);


        mListView = (ExpandableListView) findViewById(R.id.lv_detail_one);
        mAdapter = new DetailMoonAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);

    }
}