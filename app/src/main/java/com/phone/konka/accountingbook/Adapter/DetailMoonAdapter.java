package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.MoonDetailBean;
import com.phone.konka.accountingbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/17.
 */

public class DetailMoonAdapter extends BaseExpandableListAdapter {


    /**
     * 月份账单详情
     */
    private List<MoonDetailBean> mDatas;


    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 布局加载
     */
    private LayoutInflater mInflater;

    public DetailMoonAdapter(Context mContext, List<MoonDetailBean> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getDayList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getDayList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = mInflater.inflate(R.layout.item_detail_one, null);
            holder.llHead = convertView.findViewById(R.id.view_divider);
            holder.tvMoon = (TextView) convertView.findViewById(R.id.tv_detail_moon);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_detain_moon_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_detail_moon_out);
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tv_detail_moon_left);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        MoonDetailBean moonData = mDatas.get(groupPosition);

        holder.tvMoon.setText(moonData.getMoon());
        holder.tvIn.setText(moonData.getIn());
        holder.tvOut.setText(moonData.getOut());
        holder.tvLeft.setText(moonData.getLeft());

        /**
         * 设置父ListView的Divider
         */
        if (groupPosition == 0) {
            holder.llHead.setVisibility(View.GONE);
        } else {
            holder.llHead.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ChildViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_detail_two, null);
            holder = new ChildViewHolder();
            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_detail_two);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.ll.removeAllViews();
        DayDetailBean data = mDatas.get(groupPosition).getDayList().get(childPosition);
        for (int i = 0; i < data.getTagList().size(); i++) {
            RelativeLayout ll = (RelativeLayout) mInflater.inflate(R.layout.item_detail_three, null);
//            ImageView imgDate = (ImageView) ll.findViewById(R.id.img_detail_date);
            TextView tvTag = (TextView) ll.findViewById(R.id.tv_detail_tag);
            TextView tvMoney = (TextView) ll.findViewById(R.id.tv_detail_money);
            tvTag.setText(data.getTagList().get(i).getTag());
            tvMoney.setText(data.getTagList().get(i).getMoney());
            holder.ll.addView(ll);
        }


//        LinearLayout linearLayout = (LinearLayout) mInflater.inflate(R.layout.item_detail_two, null);
//
//        DayDetailBean data = mDatas.get(groupPosition).getDayList().get(childPosition);
//        for (int i = 0; i < data.getTagList().size(); i++) {
//            RelativeLayout ll = (RelativeLayout) mInflater.inflate(R.layout.item_detail_three, null);
////            ImageView imgDate = (ImageView) ll.findViewById(R.id.img_detail_date);
//            TextView tvTag = (TextView) ll.findViewById(R.id.tv_detail_tag);
//            TextView tvMoney = (TextView) ll.findViewById(R.id.tv_detail_money);
//            tvTag.setText(data.getTagList().get(i).getTag());
//            tvMoney.setText(data.getTagList().get(i).getMoney());
//            linearLayout.addView(ll);
//        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

class GroupViewHolder {
    View llHead;
    TextView tvMoon;
    TextView tvIn;
    TextView tvOut;
    TextView tvLeft;
}

class ChildViewHolder {

    LinearLayout ll;
}
