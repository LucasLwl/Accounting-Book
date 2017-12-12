package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.MonthDetailBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ImageLoader;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.View.DateView;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/17.
 */

public class GroupAdapter extends BaseExpandableListAdapter {


    /**
     * 月份账单详情
     */
    private List<MonthDetailBean> mDatas;


    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 布局加载
     */
    private LayoutInflater mInflater;

    private ImageLoader mCache;

    public GroupAdapter(Context mContext, List<MonthDetailBean> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mCache = ImageLoader.getInstance(mContext);
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
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
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tv_detail_moon);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_detain_moon_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_detail_moon_out);
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tv_detail_moon_left);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        MonthDetailBean moonData = mDatas.get(groupPosition);

        holder.tvMonth.setText(moonData.getMonth() + "月");
        holder.tvIn.setText(DoubleTo2Decimal.doubleTo2Decimal(moonData.getIn()));
        holder.tvOut.setText(DoubleTo2Decimal.doubleTo2Decimal(moonData.getOut()));
        holder.tvLeft.setText(DoubleTo2Decimal.doubleTo2Decimal(moonData.getIn() - moonData.getOut()));

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

            holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.chila_expandable, null);
            holder.elv = (ExpandableListView) convertView.findViewById(R.id.lv_child);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        holder.elv.setAdapter(new ChildAdapter(mContext, mDatas.get(groupPosition).getDayList()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

class GroupViewHolder {
    View llHead;
    TextView tvMonth;
    TextView tvIn;
    TextView tvOut;
    TextView tvLeft;
}

class ChildViewHolder {

    ExpandableListView elv;

}
