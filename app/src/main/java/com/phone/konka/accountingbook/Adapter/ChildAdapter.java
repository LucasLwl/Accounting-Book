package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.Utils.ImageLoader;
import com.phone.konka.accountingbook.View.DateView;
import com.phone.konka.accountingbook.View.LineCircleView;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/12/12.
 */

public class ChildAdapter extends BaseExpandableListAdapter {


    private Context mContext;

    private List<DayDetailBean> mList;

    private ImageLoader mCache;


    public ChildAdapter(Context mContext, List<DayDetailBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mCache = ImageLoader.getInstance(mContext);
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mList.get(groupPosition).getTagList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getTagList().get(childPosition);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_detail_two, null);
            holder.dateView = (DateView) convertView.findViewById(R.id.dv_two_date);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_two_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_two_out);
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tv_two_left);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        DayDetailBean bean = mList.get(groupPosition);
        holder.dateView.setDate(bean.getDate() + "");

        double in = 0, out = 0;
        for (DetailTagBean child : bean.getTagList()) {
            if (child.getMoney() > 0)
                in += child.getMoney();
            else
                out -= child.getMoney();
        }

        holder.tvIn.setText(DoubleTo2Decimal.doubleTo2Decimal(in));
        holder.tvOut.setText(DoubleTo2Decimal.doubleTo2Decimal(out));
        holder.tvLeft.setText(DoubleTo2Decimal.doubleTo2Decimal(in - out));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_detail_three, null);

            holder.lineCircleView = (LineCircleView) convertView.findViewById(R.id.img_detail_date);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_detail_tag);
            holder.tvTag = (TextView) convertView.findViewById(R.id.tv_detail_tag);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_detail_money);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }


        DetailTagBean bean = mList.get(groupPosition).getTagList().get(childPosition);

        if (childPosition == 0) {
            holder.lineCircleView.setFirst(true);
        } else {
            holder.lineCircleView.setFirst(false);
        }

        if (getChildrenCount(groupPosition) == childPosition + 1) {
            holder.lineCircleView.setEnd(true);
        } else {
            holder.lineCircleView.setEnd(false);
        }

        holder.imgIcon.setImageBitmap(mCache.getBitmap(bean.getIconID(), holder.imgIcon.getWidth(), holder.imgIcon.getHeight()));

        holder.tvTag.setText(bean.getTag());
        if (bean.getMoney() > 0) {
            holder.tvMoney.setText("收入+" + DoubleTo2Decimal.doubleTo2Decimal(bean.getMoney()));
        } else {
            holder.tvMoney.setText("支出" + DoubleTo2Decimal.doubleTo2Decimal(bean.getMoney()));
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        DateView dateView;
        TextView tvIn;
        TextView tvOut;
        TextView tvLeft;
    }

    class ChildViewHolder {
        LineCircleView lineCircleView;
        ImageView imgIcon;
        TextView tvTag;
        TextView tvMoney;
    }
}
