package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.R;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/17.
 */

public class DetailDayAdapter extends BaseExpandableListAdapter {


    private Context mContext;
    private List<DayDetailBean> mDatas;
    private LayoutInflater mInflater;


    public DetailDayAdapter(Context mContext, List<DayDetailBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getTagList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getTagList();
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

        if (convertView==null){
//            convertView =
        }

        return null;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.item_detail_three, null);
            holder.imgDate = (ImageView) convertView.findViewById(R.id.img_detail_date);
            holder.tvTag = (TextView) convertView.findViewById(R.id.tv_detail_tag);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_detail_money);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        DetailTagBean data = mDatas.get(groupPosition).getTagList().get(childPosition);
        holder.tvTag.setText(data.getTag());
        holder.tvMoney.setText(data.getMoney());
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {

    }

    class ChildViewHolder {
        ImageView imgDate;
        TextView tvTag;
        TextView tvMoney;
    }
}